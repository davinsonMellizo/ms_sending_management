package co.com.bancolombia.ses.adapter;

import co.com.bancolombia.commons.constants.AttachmentType;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Attachment;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.model.message.gateways.SesGateway;
import co.com.bancolombia.s3bucket.S3AsyncOperations;
import lombok.RequiredArgsConstructor;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.sesv2.SesV2AsyncClient;
import software.amazon.awssdk.services.sesv2.model.EmailContent;
import software.amazon.awssdk.services.sesv2.model.RawMessage;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.PreencodedMimeBodyPart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class SesAdapter implements SesGateway {

    private final SesV2AsyncClient client;
    private final S3AsyncOperations s3AsyncOperations;
    @Value("${aws.s3.attachmentBucket}")
    private String attachmentBucket;
    private static final int RESPONSE_CODE = 200;
    private final Log logger = LogFactory.getLog(SesAdapter.class);

    @Override
    public Mono<Response> sendEmail(TemplateEmail templateEmail, Alert alert) {
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(session);
        try {
            message.setSubject(templateEmail.getSubject(), "UTF-8");
            message.setFrom(new InternetAddress(alert.getFrom()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(alert.getDestination().getToAddress()));
            MimeMultipart msgBody = new MimeMultipart("alternative");
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(templateEmail.getBodyHtml(), "text/html; charset=UTF-8");
            msgBody.addBodyPart(htmlPart);
            alert.setAttachments(alert.getAttachments() == null ? Collections.emptyList() : alert.getAttachments());
            alert.getAttachments().forEach(attachment -> {
                try {
                    msgBody.addBodyPart(retrieveAttachment(attachment));
                } catch (MessagingException | MagicException | MagicParseException |
                        MagicMatchNotFoundException | IOException e) {
                    logger.error(e);
                }
            });
            message.setContent(msgBody);

            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                message.writeTo(outputStream);
                RawMessage rawMessage = RawMessage.builder()
                        .data(SdkBytes.fromByteBuffer(ByteBuffer
                                .wrap(outputStream.toByteArray()))).build();
                SendEmailRequest emailRequest = SendEmailRequest.builder()
                        .content(EmailContent.builder().raw(rawMessage).build())
                        .build();
                return Mono.just(client.sendEmail(emailRequest))
                        .map(response -> Response.builder().code(RESPONSE_CODE).description("ses sendRawEmail").build());
            } catch (Exception e) {
                return Mono.just(Response.builder().code(1).description(e.getMessage()).build());
            }
        } catch (MessagingException e) {
            return Mono.just(Response.builder().code(1).description(e.getMessage()).build());
        }
    }

    private MimeBodyPart retrieveAttachment(Attachment attachment) throws IOException, MessagingException,
            MagicParseException, MagicException, MagicMatchNotFoundException {
        switch (attachment.getType()) {
            case AttachmentType.PATH:
                return retrieveFromPath(attachment);
            case AttachmentType.URL:
                return retrieveFromUrl(attachment);
            case AttachmentType.BASE64:
                return retrieveFromBase64(attachment);
            default:
                return new MimeBodyPart();
        }
    }

    private MimeBodyPart retrieveFromPath(Attachment attachment) throws MessagingException, MagicParseException,
            MagicException, MagicMatchNotFoundException {
        byte[] byteArray = s3AsyncOperations.getFileAsByteArray(attachmentBucket, attachment.getValue()).block();
        MagicMatch match = Magic.getMagicMatch(byteArray);
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(byteArray, match.getMimeType());
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(attachment.getFilename());
        return attachmentPart;
    }

    private MimeBodyPart retrieveFromUrl(Attachment attachment) throws MalformedURLException, MessagingException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        URL url = new URL(attachment.getValue());
        attachmentPart.setDataHandler(new DataHandler(new URLDataSource(url)));
        attachmentPart.setHeader("Content-Transfer-Encoding", "base64");
        attachmentPart.setDisposition(Part.ATTACHMENT);
        attachmentPart.setFileName(attachment.getFilename());
        return attachmentPart;
    }

    private MimeBodyPart retrieveFromBase64(Attachment attachment) throws MessagingException {
        MimeBodyPart attachmentPart = new PreencodedMimeBodyPart("base64");
        attachmentPart.setContent(attachment.getValue(), attachment.getContentType());
        attachmentPart.setDisposition(Part.ATTACHMENT);
        attachmentPart.setFileName(attachment.getFilename());
        return attachmentPart;
    }
}
