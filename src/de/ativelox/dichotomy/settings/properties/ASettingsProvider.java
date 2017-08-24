package de.ativelox.dichotomy.settings.properties;

import java.util.HashMap;
import java.util.Map;

import de.ativelox.dichotomy.settings.Settings;

/**
 * The most basic implementation for a settings provider.
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public abstract class ASettingsProvider implements ISettingsProvider {

	/**
	 * The settings used to load from or save to.
	 */
	private final Settings settings;

	/**
	 * The internal map used to handle the settings.
	 */
	protected final Map<String, String> settingsStore;

	/**
	 * The settings provider which holds all the settings needed throughout this
	 * project.
	 */
	public ASettingsProvider(final String fileComment, final String filePath) {
		this.settingsStore = new HashMap<>();
		this.settings = new Settings(fileComment, filePath);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ativelox.dichotomy.settings.ISettingsProvider#getAllSettings()
	 */
	@Override
	public Map<String, String> getAllSettings() {
		return this.settingsStore;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ativelox.dichotomy.settings.ISettingsProvider#getSetting(java.lang.
	 * String)
	 */
	@Override
	public String getSetting(String key) {
		String value = this.settingsStore.get(key);
		if (value == null) {
			value = UNKNOWN_KEY_VALUE;

		}
		return value;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ativelox.dichotomy.settings.ISettingsProvider#initialize()
	 */
	@Override
	public void initialize() {
		this.settings.loadSettings(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ativelox.dichotomy.settings.ISettingsProvider#saveSettings()
	 */
	@Override
	public void saveSettings() {
		this.settings.saveSettings(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ativelox.dichotomy.settings.ISettingsProvider#setSetting(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public void setSetting(String key, String value) {
		this.settingsStore.put(key, value);

	}
	
	/* (non-Javadoc)
	 * @see de.ativelox.dichotomy.settings.properties.ISettingsProvider#removeSetting(java.lang.String)
	 */
	@Override
	public void removeSetting(String key) {
		this.settingsStore.remove(key);
		this.settings.removeKeyFromProperties(key);
		
	}
}
