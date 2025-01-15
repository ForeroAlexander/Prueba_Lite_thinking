package com.forero.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AwsService {

    private final AmazonS3 amazonS3;  // Descomentado esta línea
    private final AmazonSimpleEmailService sesClient;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.ses.from-email}")
    private String fromEmail;

    @Autowired
    public AwsService(AmazonS3 amazonS3, AmazonSimpleEmailService sesClient) {
        this.amazonS3 = amazonS3;
        this.sesClient = sesClient;
    }

    public String subirArchivoS3(File archivo, String nombreArchivo) {
        try {
            // Validar tipo de archivo
            if (!nombreArchivo.toLowerCase().endsWith(".pdf")) {
                throw new IllegalArgumentException("Solo se permiten archivos PDF");
            }

            // Validar tamaño del archivo (por ejemplo, máximo 10MB)
            if (archivo.length() > 10_000_000) {
                throw new IllegalArgumentException("El archivo excede el tamaño máximo permitido");
            }

            PutObjectRequest request = new PutObjectRequest(bucketName, nombreArchivo, archivo)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(request);
            return amazonS3.getUrl(bucketName, nombreArchivo).toString();
        } catch (AmazonS3Exception e) {
            throw new RuntimeException("Error al subir archivo a S3: " + e.getMessage(), e);
        }
    }

    public void enviarEmail(String emailDestino, String asunto, String contenido, String urlArchivo) {
        try {
            String htmlContent = String.format("""
                <html>
                <body>
                    <h2>Reporte de Inventario</h2>
                    <p>%s</p>
                    <p>Puede descargar el reporte en el siguiente enlace:</p>
                    <a href="%s">Descargar Reporte</a>
                </body>
                </html>
                """, contenido, urlArchivo);

            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(emailDestino))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8")
                                            .withData(htmlContent)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8")
                                    .withData(asunto)))
                    .withSource(fromEmail);

            sesClient.sendEmail(request);
        } catch (AmazonSimpleEmailServiceException e) {
            throw new RuntimeException("Error al enviar email: " + e.getMessage(), e);
        }
    }
}
