package co.com.bancolombia.pinpoint.adapter;

import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Response;

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
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class SesAdapter implements SesGateway {
    private final AmazonSimpleEmailService client;
    private static String CHARSET = "UTF-8";

    @Override
    public Mono<Response> sendEmail(Alert alert){
        Session session = Session.getDefaultInstance(new Properties());

        // Create a new MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        try {
        // Add subject, from and to lines.
        message.setSubject(alert.getMessage().getSubject(), "UTF-8");
        message.setFrom(new InternetAddress(alert.getFrom()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(alert.getDestination().getToAddress()));

        // Create a multipart/alternative child container.
        MimeMultipart msg_body = new MimeMultipart("alternative");

        // Create a wrapper for the HTML and text parts.
        MimeBodyPart wrap = new MimeBodyPart();

        // Define the HTML part.
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(alert.getMessage().getBody(),"text/html; charset=UTF-8");

        // Add the text and HTML parts to the child container.
        msg_body.addBodyPart(htmlPart);

        // Add the child container to the wrapper object.
        wrap.setContent(msg_body);

        // Create a multipart/mixed parent container.
        MimeMultipart msg = new MimeMultipart("mixed");

        // Add the parent container to the message.
        message.setContent(msg);

        // Add the multipart/alternative part to the message.

            msg.addBodyPart(wrap);


        /*// Define the attachment
        MimeBodyPart att = new MimeBodyPart();
        DataSource fds = new FileDataSource(ATTACHMENT);
        att.setDataHandler(new DataHandler(fds));
        att.setFileName(fds.getName());

        // Add the attachment to the message.
        msg.addBodyPart(att);*/

        // Try to send the email.
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);
            RawMessage rawMessage =
                    new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

            SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);

            return Mono.just(Response.builder().code(0).description("response.getMessageId()").build());
            /*return Mono.just(client.sendRawEmail(rawEmailRequest))
                    .map(response -> Response.builder().code(0).description(response.getMessageId()).build());*/

        } catch (Exception ex) {
            System.out.println("Email Failed");
            System.err.println("Error message: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }

    }
}
