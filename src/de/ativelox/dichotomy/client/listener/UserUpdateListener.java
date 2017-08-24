package de.ativelox.dichotomy.client.listener;

import de.ativelox.dichotomy.client.DiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.user.UserEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;

/**
 *
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public class UserUpdateListener implements IListener<UserEvent> {

	private final DiscordClient mClient;

	/**
	 * 
	 */
	public UserUpdateListener(final DiscordClient client) {
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
	public void handle(UserEvent event) {
		final IUser user = event.getUser();

		StatusType oldStatus = this.mClient.getPresence().get(user);
		oldStatus = (oldStatus == null) ? StatusType.UNKNOWN : oldStatus;

		final StatusType currentStatus = event.getUser().getPresence().getStatus();

		for (final StatusType status : StatusType.values()) {
			if (currentStatus == status) {
				// TODO: Remove Debug Print!
				System.out.println(user.getName() + ": " + oldStatus.toString() + " -> " + currentStatus.toString());

			}
		}
		this.mClient.updatePresence(user, currentStatus);

	}
}
