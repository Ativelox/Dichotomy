package de.ativelox.dichotomy.client.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import de.ativelox.dichotomy.settings.MailSettingsProvider;

/**
 *
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public class SMTPAuthenticator extends Authenticator {

	private final String mUser;

	private final String mPassword;

	public SMTPAuthenticator(final MailSettingsProvider mailSettingsProvider) {
		this.mUser = mailSettingsProvider.getUser();
		this.mPassword = mailSettingsProvider.getPassword();
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.mUser, this.mPassword);

	}

}
