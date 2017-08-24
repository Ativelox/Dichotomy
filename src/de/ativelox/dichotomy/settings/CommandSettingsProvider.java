package de.ativelox.dichotomy.settings;

import de.ativelox.dichotomy.settings.properties.ASettingsProvider;

/**
 *
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public class CommandSettingsProvider extends ASettingsProvider {

	/**
	 * The comment of the file created by this object.
	 */
	private static final String FILE_COMMENT = "The commands and their description available for dichotomy.";

	/**
	 * The name of the file created.
	 */
	private static final String FILE_PATH = "commands.ini";

	/**
	 * 
	 */
	public CommandSettingsProvider() {
		super(FILE_COMMENT, FILE_PATH);

	}

}
