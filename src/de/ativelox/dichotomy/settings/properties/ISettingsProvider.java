package de.ativelox.dichotomy.settings.properties;

import java.util.Map;

/**
 * An interface for settings provider.
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public interface ISettingsProvider {

	/**
	 * The unknown key value.
	 */
	public static final String UNKNOWN_KEY_VALUE = "";

	/**
	 * Returns all the settings stored in the internal map.
	 * 
	 * @return the Map mentioned.
	 */
	public Map<String, String> getAllSettings();

	/**
	 * Gets a setting from the internal map.
	 * 
	 * @param mKey
	 *            The key which to lookup.
	 * 
	 * @return The value corresponding to the key given.
	 */
	public String getSetting(final String key);

	/**
	 * Initializes this settings provider by loading all the settings from the
	 * settings object.
	 */
	public void initialize();

	/**
	 * Saves all the settings in the internal map.
	 */
	public void saveSettings();

	/**
	 * Sets a setting to the internal map.
	 * 
	 * @param mKey
	 *            The key which to set
	 * 
	 * @param mValue
	 *            The corresponding value to set
	 */
	public void setSetting(final String key, final String value);
	
	/**
	 * Removes the setting specified by the key in the internal map.
	 * @param key
	 */
	public void removeSetting(final String key);
}
