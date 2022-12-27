package co.com.bancolombia.ses.adapter;

import co.com.bancolombia.commons.constants.AttachmentType;
import co.com.bancolombia.model.log.LoggerBuilder;
import co.com.bancolombia.model.message.Alert;
import co.com.bancolombia.model.message.Attachment;
import co.com.bancolombia.model.message.Response;
import co.com.bancolombia.model.message.TemplateEmail;
import co.com.bancolombia.model.message.gateways.SesGateway;
import co.com.bancolombia.s3bucket.S3AsyncOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.PreencodedMimeBodyPart;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class SesAdapter implements SesGateway {

    private final SesAsyncClient client;
    private final S3AsyncOperations s3AsyncOperations;
    private final LoggerBuilder logger;
    @Value("${aws.s3.attachmentBucket}")
    private String attachmentBucket;
    private final LoggerBuilder loggerBuilder;
    private final int codigoResponse = 200;

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
            alert.setAttachments(alert.getAttachments() == null ? Collections.emptyList() : alert.getAttachments());
            alert.getAttachments().forEach(attachment -> {
                try {
                    msg_body.addBodyPart(retrieveAttachment(attachment));
                } catch (MessagingException | MalformedURLException e) {
                    logger.error(e);
                }
            });
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
            }
        } catch (MessagingException e) {
            return Mono.just(Response.builder().code(1).description(e.getMessage()).build());
        }
    }

    private MimeBodyPart retrieveAttachment(Attachment attachment) throws MalformedURLException, MessagingException {
        switch (attachment.getType()) {
            case AttachmentType.PATH:
                return retrieveFromPath(attachment.getValue());
            case AttachmentType.URL:
                return retrieveFromUrl(attachment.getValue());
            case AttachmentType.BASE64:
                return retrieveFromBase64(attachment);
            default:
                return new MimeBodyPart();
        }
    }

    private MimeBodyPart retrieveFromPath(String urlString) throws MessagingException {
        InputStream attachment = s3AsyncOperations.getFileAsInputStream(attachmentBucket, urlString).block();
        return new MimeBodyPart(attachment);
    }

    private MimeBodyPart retrieveFromUrl(String urlString) throws MalformedURLException, MessagingException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        URL url = new URL(urlString);
        attachmentPart.setDataHandler(new DataHandler(url));
        attachmentPart.setDisposition(Part.ATTACHMENT);
        attachmentPart.setFileName(url.getFile());
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
