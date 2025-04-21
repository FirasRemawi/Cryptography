package Project3;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
	private String username = "firasremawi26@gmail.com";
	private String password = "znhx gmfp lwcs vxpc\r\n";

	public void sendEmail(String recipient, String subject, String content) {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true"); // Ensure STARTTLS is enabled
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587"); // Correct port for TLS

		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password); // Ensure you're using the correct username and
																		// password
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			message.setSubject(subject);
			message.setText(content);

			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace(); // This will give more details on what went wrong
		}
	}

	public static void main(String[] args) {
		EmailService emailService = new EmailService();
		// Example encrypted message and key
		String encryptedMessage = "kol e7trem";
		String key = "SecretKeyHere";

		// Send via email
		emailService.sendEmail("ferasremawi26@gmail.com", "Encrypted Message", encryptedMessage);

	}
}
