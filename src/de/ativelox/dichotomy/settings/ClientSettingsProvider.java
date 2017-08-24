package de.ativelox.dichotomy.settings;

import de.ativelox.dichotomy.settings.properties.ASettingsProvider;

/**
 *
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public class ClientSettingsProvider extends ASettingsProvider {

	/**
	 * The comment of the file created by this object.
	 */
	private static final String FILE_COMMENT = "Configurations for various keys/ids/etc. used by dichotomy.";

	/**
	 * The name of the file created.
	 */
	private static final String FILE_PATH = "config.ini";

	/**
	 * The key identifier for the token used by this bot.
	 */
	private static final String TOKEN_KEY_IDENTIFIER = "TOKEN";

	/**
	 * The key identifier for the guild id for which this bot operates.
	 */
	private static final String GUILD_ID_KEY_IDENTIFIER = "GUILD";

	/**
	 * 
	 */
	public ClientSettingsProvider() {
		super(FILE_COMMENT, FILE_PATH);

	}

	public void setToken(final String token) {
		this.settingsStore.put(TOKEN_KEY_IDENTIFIER, token);

	}

	public void setGuildID(final long guildID) {
		this.settingsStore.put(GUILD_ID_KEY_IDENTIFIER, "" + guildID);

	}

	public String getToken() {
		return this.settingsStore.get(TOKEN_KEY_IDENTIFIER);

	}

	public long getGuildID() {
		return Long.parseLong(this.settingsStore.get(GUILD_ID_KEY_IDENTIFIER));

	}

}
