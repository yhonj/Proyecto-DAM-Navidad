package com.yhon.dam.proyectodamnavidad.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Utilidad para el envío de correos electrónicos.
 * <p>
 * Esta clase encapsula la configuración y el uso de Jakarta Mail
 * para enviar correos SMTP desde la aplicación, siendo utilizada
 * principalmente en el proceso de recuperación de contraseña.
 * </p>
 *
 * <p><strong>Nota de seguridad:</strong> Las credenciales están
 * incluidas únicamente con fines de prueba en un entorno académico.
 * En un entorno de producción se deberían utilizar variables de
 * entorno o servicios de gestión de secretos.</p>
 *
 * @author yhon
 */
public class EmailUtil {

    /**
     * Dirección de correo remitente.
     * <p>
     * Utilizada únicamente para pruebas académicas.
     * </p>
     */
    private static final String FROM = "yhonjustoyz@gmail.com";

    /**
     * Contraseña del correo remitente.
     * <p>
     * Solo válida para pruebas. No debe usarse en producción.
     * </p>
     */
    private static final String PASSWORD = "hoaq fmet jsgs aolj";

    /**
     * Envía un correo electrónico mediante SMTP.
     * <p>
     * Configura una sesión segura con autenticación TLS y envía
     * un mensaje de texto simple al destinatario indicado.
     * </p>
     *
     * @param destino dirección de correo del destinatario
     * @param asunto asunto del mensaje
     * @param texto contenido del mensaje
     * @throws MessagingException si ocurre un error durante el envío
     */
    public static void enviar(
            String destino,
            String asunto,
            String texto
    ) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(
                props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM, PASSWORD);
                    }
                }
        );

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(destino)
        );
        message.setSubject(asunto);
        message.setText(texto);

        Transport.send(message);
    }
}
