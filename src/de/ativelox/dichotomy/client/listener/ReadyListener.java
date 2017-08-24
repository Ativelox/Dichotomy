package de.ativelox.dichotomy.client.listener;

import de.ativelox.dichotomy.client.DiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;

/**
 *
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public class ReadyListener implements IListener<ReadyEvent> {

	private final DiscordClient mClient;

	/**
	 * 
	 */
	public ReadyListener(final DiscordClient client) {
		this.mClient = client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sx.blah.discord.api.events.IListener#handle(sx.blah.discord.api.events.
	 * Event)
	 */
	@Override
	public void handle(final ReadyEvent arg0) {
		this.mClient.ready();

	}
}
