package de.ativelox.dichotomy.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import de.ativelox.dichotomy.settings.properties.ASettingsProvider;

import java.util.Properties;

/**
 * Provides methods to {@link Settings#saveSettings(ASettingsProvider)} and
 * {@link Settings#loadSettings(ASettingsProvider)} for a given
 * {@link ASettingsProvider} which has all the values.
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public final class Settings {

	/**
	 * The header-comment of the properties file.
	 */
	private final String mFileComment;

	/**
	 * The relative filepath for the properties file.
	 */
	private final String mFilePath;

	/**
	 * The properties for these settings.
	 */
	private final Properties properties;

	/**
	 * Provides methods to {@link Settings#saveSettings(ASettingsProvider)} and
	 * {@link Settings#loadSettings(ASettingsProvider)} for a given
	 * {@link ASettingsProvider} which has all the values.
	 */
	public Settings(final String fileComment, final String filePath) {
		this.mFileComment = fileComment;
		this.mFilePath = filePath;
		this.properties = new Properties();

	}

	/**
	 * Loads the Settings of {@link Settings#FILEPATH} and loads them in the
	 * given settingsProvider. If the file does not exists this method will ask
	 * for specific user-input.
	 * 
	 * @param mProvider
	 *            The provider into which to load the settings.
	 */
	public void loadSettings(final ASettingsProvider mProvider) {

		try (final FileInputStream fis = new FileInputStream(this.mFilePath)) {
			try {
				this.properties.load(fis);

			} catch (final FileNotFoundException e) {

				try (final FileInputStream anotherFis = new FileInputStream(this.mFilePath)) {
					this.properties.load(anotherFis);

				}
			}

			for (final Entry<Object, Object> entry : this.properties.entrySet()) {
				mProvider.setSetting((String) entry.getKey(), (String) entry.getValue());
			}

		} catch (final IOException e) {
			// no file was found at the specified location, thus create an empty
			// file.
			this.saveSettings(mProvider);
			this.loadSettings(mProvider);

		}
	}

	/**
	 * Gets all the values given by the settings provider and saves them to
	 * {@link Settings#FILEPATH}.
	 * 
	 * @param mProvider
	 *            The settings provider of which to get the settings to be
	 *            saved.
	 */
	public void saveSettings(final ASettingsProvider mProvider) {
		for (final Entry<String, String> entry : mProvider.getAllSettings().entrySet()) {
			this.properties.put(entry.getKey(), entry.getValue());

		}

		try (final FileOutputStream target = new FileOutputStream(new File(this.mFilePath))) {
			this.properties.store(target, this.mFileComment);

		} catch (final IOException e) {
			e.printStackTrace();

		}
	}

	public void removeKeyFromProperties(final String key) {
		this.properties.remove(key);

	}
}
