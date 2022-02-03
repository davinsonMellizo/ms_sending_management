package co.com.bancolombia.ibmmq.jms;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.ibmmq.config.MQProperties;
import co.com.bancolombia.ibmmq.model.ConnectionDTO;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.secretsmanager.SecretsManager;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.net.ssl.CertPathTrustManagerParameters;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertificateException;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.X509CertSelector;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static com.ibm.msg.client.jms.JmsConstants.PASSWORD;
import static com.ibm.msg.client.wmq.common.CommonConstants.WMQ_CLIENT_RECONNECT;
import static com.ibm.msg.client.wmq.common.CommonConstants.WMQ_CM_CLIENT;
import static com.ibm.msg.client.wmq.common.CommonConstants.WMQ_TEMPORARY_MODEL;

@Component
@RequiredArgsConstructor
public class JmsExtConnectionFactory {

    private final SecretsManager secretManager;
    private final LoggerBuilder loggerBuilder;

    private Map<String,JmsConnectionFactory> mapConnFactory;
    private static final String TLS = "TLSv1.3";

    @PostConstruct
    private void init(){
        mapConnFactory = new HashMap<>();
    }

    public JmsConnectionFactory connectionFactory(ConnectionDTO conn) {
        if(!mapConnFactory.containsKey(conn.getName())){
            try {
                MQProperties properties = secretManager.getSecret(conn.getSecret(), MQProperties.class).block();
                System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "false");
                System.out.println("cuarto");
                JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
                System.out.println("quinto");
                JmsConnectionFactory cf = ff.createConnectionFactory();
                System.out.println("sexto");
                cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQ_CM_CLIENT);
                cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, conn.getQmGroup());
                cf.setStringProperty(WMQConstants.USERID, properties.getUser());
                if (conn.getTemporaryQueueModel() != null) {
                    cf.setStringProperty(WMQ_TEMPORARY_MODEL, conn.getTemporaryQueueModel());
                }

                if (properties.getPassword() != null && !properties.getPassword().isEmpty()) {
                    cf.setStringProperty(PASSWORD, properties.getPassword());
                }
                MQConnectionFactory mqConnection = (MQConnectionFactory) cf;
                URL ccdtUrl = new File(conn.getCcdtPath()).toURI().toURL();
                URL jksUrl = new File(conn.getJksPath()).toURI().toURL();

                SSLSocketFactory sslSocketFactory = sslContext(jksUrl, properties.getTrustStorePass()).getSocketFactory();

                mqConnection.setCCDTURL(ccdtUrl);
                mqConnection.setTransportType(WMQ_CM_CLIENT);
                mqConnection.setClientReconnectOptions(WMQ_CLIENT_RECONNECT);
                mqConnection.setSSLSocketFactory(sslSocketFactory);
                mapConnFactory.put(conn.getName(),mqConnection);
                return mqConnection;
            } catch (JMSException | MalformedURLException e) {
                loggerBuilder.error(e);
                throw new TechnicalException(e, TechnicalExceptionEnum.TECHNICAL_MQ_ERROR);
            }
        }
        return mapConnFactory.get(conn.getName());
    }

    private SSLContext sslContext(URL jkstUrl, String keyStorePassword) {
        try (InputStream cert = new FileInputStream(jkstUrl.getPath())) {
            final KeyStore caCertsKeyStore = KeyStore.getInstance("JKS");
            if (keyStorePassword != null) {
                caCertsKeyStore.load(cert, keyStorePassword.toCharArray());
            }
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            CertPathBuilder cpb = CertPathBuilder.getInstance("PKIX");
            PKIXRevocationChecker rc = (PKIXRevocationChecker) cpb.getRevocationChecker();
            rc.setOptions(EnumSet.of(
                    PKIXRevocationChecker.Option.PREFER_CRLS,
                    PKIXRevocationChecker.Option.ONLY_END_ENTITY,
                    PKIXRevocationChecker.Option.SOFT_FAIL,
                    PKIXRevocationChecker.Option.NO_FALLBACK));
            PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(caCertsKeyStore, new X509CertSelector());
            pkixParams.addCertPathChecker(rc);
            if (keyStorePassword != null) {
                kmf.init(caCertsKeyStore, keyStorePassword.toCharArray());
            }
            tmf.init(new CertPathTrustManagerParameters(pkixParams));
            final SSLContext sslContext = SSLContext.getInstance(TLS);
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
            return sslContext;
        } catch (KeyStoreException | InvalidAlgorithmParameterException | NoSuchAlgorithmException |
                UnrecoverableKeyException | KeyManagementException | IOException | CertificateException e) {
            loggerBuilder.error(e);
            throw new TechnicalException(e, TechnicalExceptionEnum.TECHNICAL_SSL_CONTEXT_ERROR);
        }
    }
}
