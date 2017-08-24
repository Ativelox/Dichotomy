package de.ativelox.dichotomy;

import de.ativelox.dichotomy.client.DiscordClient;
import de.ativelox.dichotomy.settings.ClientSettingsProvider;
import de.ativelox.dichotomy.settings.CommandSettingsProvider;
import de.ativelox.dichotomy.settings.MailSettingsProvider;

/**
 *
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public class Launcher {

	public static void main(final String[] args) {
		final MailSettingsProvider mailSettingsProvider = new MailSettingsProvider();
		mailSettingsProvider.initialize();

		final ClientSettingsProvider clientSettingsProvider = new ClientSettingsProvider();
		clientSettingsProvider.initialize();

		final CommandSettingsProvider commandSettingsProvider = new CommandSettingsProvider();
		commandSettingsProvider.initialize();

		DiscordClient client = new DiscordClient(clientSettingsProvider, mailSettingsProvider, commandSettingsProvider);
		client.login();

	}

	/**
	 * 
	 */
	public Launcher() {
		// TODO Auto-generated constructor stub
	}

}
