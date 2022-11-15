package co.com.bancolombia.ses.adapter;


import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.model.message.gateways.SesGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class SesAdapter implements SesGateway {

    private final SesAsyncClient client;

    private final LoggerBuilder loggerBuilder;

    private final int codigoResponse=200;


    @Override
    public Mono<Response> sendEmail(TemplateEmail templateEmail, Alert alert) {

        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(session);
        try {
            message.setSubject(templateEmail.getSubject(), "UTF-8");
            message.setFrom(new InternetAddress(alert.getFrom()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(alert.getDestination().getToAddress()));
            MimeMultipart msg_body = new MimeMultipart("alternative");
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(templateEmail.getBodyHtml(), "text/html; charset=UTF-8");

            msg_body.addBodyPart(htmlPart);
            message.setContent(msg_body);

            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                message.writeTo(outputStream);
                RawMessage rawMessage = RawMessage.builder()
                        .data(SdkBytes.fromByteBuffer(ByteBuffer
                                .wrap(outputStream.toByteArray()))).build();
                SendRawEmailRequest rawEmailRequest = SendRawEmailRequest.builder()
                        .rawMessage(rawMessage).build();

                return Mono.just(client.sendRawEmail(rawEmailRequest))
                        .map(response -> Response.builder().code(codigoResponse).description("ses sendRawEmail").build());

            } catch (Exception e) {
                return Mono.just(Response.builder().code(1).description(e.getMessage()).build());
            }} catch (MessagingException e) {
            return Mono.just(Response.builder().code(1).description(e.getMessage()).build());
        }
    }
}
