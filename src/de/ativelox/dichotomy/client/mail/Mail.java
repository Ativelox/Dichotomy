package de.ativelox.dichotomy.client.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.ativelox.dichotomy.client.mail.properties.EMailServer;
import de.ativelox.dichotomy.settings.MailSettingsProvider;

/**
 *
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public class Mail {

	public static void send(final MailSettingsProvider mailSettingsProvider, final String subject,
			final String content) {

		if (mailSettingsProvider.getMailServer() != EMailServer.WEB) {
			System.err.println("Unsupported Mail server, not processing the given data.");
			return;

		}

		final Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.web.de");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.port", "587");

		final Session session = Session.getDefaultInstance(properties, new SMTPAuthenticator(mailSettingsProvider));

		try {
			final Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailSettingsProvider.getSender()));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailSettingsProvider.getRecipient()));
			message.setSubject(subject);
			message.setText(content);

			Transport.send(message);

		} catch (final MessagingException e) {
			System.err.println("An error occured while trying to send a message from "
					+ mailSettingsProvider.getSender() + " to " + mailSettingsProvider.getRecipient() + ".");

			e.printStackTrace();

		}
	}

	public static void send(final MailSettingsProvider mailSettingsProvider, final String content) {
		Mail.send(mailSettingsProvider, "", content);

	}
}
