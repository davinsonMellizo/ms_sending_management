package co.com.bancolombia.ses.adapter;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;

import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.model.message.gateways.SesGateway;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message;
import com.amazonaws.services.simpleemail.model.RawMessage;
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
    private final AmazonSimpleEmailService client;
    private static String CHARSET = "UTF-8";

    @Override
    public Mono<Response> sendEmail(TemplateEmail templateEmail, Alert alert){
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(session);

        try {
        message.setSubject(templateEmail.getSubject(), "UTF-8");
        message.setFrom(new InternetAddress(alert.getFrom()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(alert.getDestination().getToAddress()));

        MimeMultipart msg_body = new MimeMultipart("alternative");
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(templateEmail.getBodyHtml(),"text/html; charset=UTF-8");

        msg_body.addBodyPart(htmlPart);
        message.setContent(msg_body);

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);
            RawMessage rawMessage =
                    new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

            SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);

            //return Mono.just(Response.builder().code(202).description("response.getMessageId()").build());
            return Mono.just(client.sendRawEmail(rawEmailRequest))
                    .map(response -> Response.builder().code(200).description(response.getMessageId()).build());

        } catch (Exception e) {
            return Mono.just(Response.builder().code(1).description(e.getMessage()).build());
        }
        } catch (MessagingException e) {
            return Mono.just(Response.builder().code(1).description(e.getMessage()).build());

        }

    }
}
