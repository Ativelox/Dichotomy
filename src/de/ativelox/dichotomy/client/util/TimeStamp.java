package de.ativelox.dichotomy.client.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public class TimeStamp {

	public static String defaultFormat(final LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' HH:mm:ss");
		String text = date.format(formatter);

		return text;

	}

	/**
	 * 
	 */
	private TimeStamp() {

	}

}
