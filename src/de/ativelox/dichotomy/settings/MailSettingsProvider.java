package de.ativelox.dichotomy.settings;

import de.ativelox.dichotomy.client.mail.properties.EMailServer;
import de.ativelox.dichotomy.settings.properties.ASettingsProvider;

/**
 * A mail settings provider used to store authenticated information to send
 * emails.
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public final class MailSettingsProvider extends ASettingsProvider {

	/**
	 * The comment of the file created by this object.
	 */
	private static final String FILE_COMMENT = "Configurations for the mails sent by dichotomy.";

	/**
	 * The name of the file created.
	 */
	private static final String FILE_PATH = "mail.ini";

	/**
	 * The key for the sender in the internal map
	 */
	private static final String SENDER_KEY_IDENTIFIER = "SENDER";

	/**
	 * The key for the recipient in the internal map
	 */
	private static final String RECIPIENT_KEY_IDENTIFIER = "RECIPIENT";

	/**
	 * The key for the user in the internal map
	 */
	private static final String SMTP_AUTH_USER_KEY_IDENTIFIER = "SMTP_AUTH_USER";

	/**
	 * The key for the password in the internal map
	 */
	private static final String SMTP_AUTH_PWD_KEY_IDENTIFIER = "SMTP_AUTH_KEY";
	/**
	 * The key for the mail server in the internal map
	 */
	private static final String MAIL_SERVER_KEY_IDENTIFIER = "MAIL_SERVER";

	/**
	 * 
	 */
	public MailSettingsProvider() {
		super(FILE_COMMENT, FILE_PATH);

	}

	public void setSender(final String sender) {
		this.settingsStore.put(SENDER_KEY_IDENTIFIER, sender);

	}

	public void setRecipient(final String recipient) {
		this.settingsStore.put(RECIPIENT_KEY_IDENTIFIER, recipient);

	}

	public void setUser(final String user) {
		this.settingsStore.put(SMTP_AUTH_USER_KEY_IDENTIFIER, user);

	}

	public void setPassword(final String password) {
		this.settingsStore.put(SMTP_AUTH_PWD_KEY_IDENTIFIER, password);

	}

	public void setMailServer(final EMailServer mailServer) {
		this.settingsStore.put(MAIL_SERVER_KEY_IDENTIFIER, mailServer.toString());

	}

	public String getSender() {
		return this.settingsStore.get(SENDER_KEY_IDENTIFIER);

	}

	public String getRecipient() {
		return this.settingsStore.get(RECIPIENT_KEY_IDENTIFIER);

	}

	public String getUser() {
		return this.settingsStore.get(SMTP_AUTH_USER_KEY_IDENTIFIER);

	}

	public String getPassword() {
		return this.settingsStore.get(SMTP_AUTH_PWD_KEY_IDENTIFIER);

	}

	public EMailServer getMailServer() {
		return EMailServer.valueOf(this.settingsStore.get(MAIL_SERVER_KEY_IDENTIFIER).toUpperCase());

	}
}
