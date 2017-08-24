package de.ativelox.dichotomy.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.Map.Entry;

import de.ativelox.dichotomy.client.exceptions.ChangedAdminRoleNameException;
import de.ativelox.dichotomy.client.listener.ReadyListener;
import de.ativelox.dichotomy.client.listener.UserUpdateListener;
import de.ativelox.dichotomy.client.mail.Mail;
import de.ativelox.dichotomy.client.util.TimeStamp;
import de.ativelox.dichotomy.settings.ClientSettingsProvider;
import de.ativelox.dichotomy.settings.CommandSettingsProvider;
import de.ativelox.dichotomy.settings.MailSettingsProvider;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageHistory;

/**
 * An implementation for the Discord API.
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public class DiscordClient {

	public static final String ADMIN_ROLE_NAME = "Admin";

	private static final String NO_PERMISSION_NOTIFIER = "you do not have permission to use this command.";

	private static final String COMMAND_IDENTIFIER = "!";

	private static final int WAIT_PER_CYCLE = 1000;

	/**
	 * The API on which is being operated.
	 */
	private IDiscordClient mClient;

	/**
	 * The event dispatcher used to manage events.
	 */
	private EventDispatcher mDispatcher;

	private IGuild mGuild;

	/**
	 * A map storing the current status type for every user on the server.
	 */
	private final Map<IUser, StatusType> mPresence;

	/**
	 * A flag determining whether the API is ready to be used, e.g. the
	 * connection is finished.
	 */
	private boolean mReady;

	private int maxChannels;

	/**
	 * The mail settings provider, used to get the data needed to send mails.
	 */
	private final MailSettingsProvider mMailSettingsProvider;

	private final ClientSettingsProvider mClientSettingsProvider;

	private final CommandSettingsProvider mCommandSettingsProvider;

	private Map<String, String> commands;

	/**
	 * An implementation for the Discord API.
	 * 
	 * @param token
	 *            The token of the bot, used to authorize access.
	 */
	public DiscordClient(final ClientSettingsProvider clientSettingsProvider,
			final MailSettingsProvider mailSettingsProvider, final CommandSettingsProvider commandSettingsProvider) {

		this.mPresence = new HashMap<>();
		this.mReady = false;

		this.mClientSettingsProvider = clientSettingsProvider;
		this.mMailSettingsProvider = mailSettingsProvider;
		this.mCommandSettingsProvider = commandSettingsProvider;

		this.buildClient();
	}

	public Map<IUser, StatusType> getPresence() {
		return this.mPresence;

	}

	/**
	 * Initializes this client by creating a new instance of it and registering
	 * its listeners. {@link DiscordClient#login()} should be called to connect
	 * to discord gateway.
	 */
	private void buildClient() {
		final ClientBuilder clientBuilder = new ClientBuilder();
		clientBuilder.withToken(this.mClientSettingsProvider.getToken());
		try {
			this.mClient = clientBuilder.build();
			this.mDispatcher = this.mClient.getDispatcher();
			this.mDispatcher.registerListener(new ReadyListener(this));
			this.mDispatcher.registerListener(new UserUpdateListener(this));

		} catch (final DiscordException e) {
			System.err.println("Error occured while trying to build the client.");

		}
	}

	/**
	 * Returns the ready flag which signals whether the API is connected and
	 * ready to use.
	 * 
	 * @return The flag mentioned.
	 */
	public boolean isReady() {
		return this.mReady;

	}

	private void fetchData() {
		this.commands = this.mCommandSettingsProvider.getAllSettings();

		for (final IUser user : this.mClient.getUsers()) {
			this.mPresence.put(user, user.getPresence().getStatus());
			this.maxChannels++;

		}
		this.maxChannels += this.mClient.getChannels(false).size();

		this.start();
	}

	/**
	 * Logs this client in. Connects to the discord gateway and returns
	 * <tt>true</tt> if the connection went through, <tt>false</tt> if an error
	 * occured.
	 * 
	 * @return <tt>True</tt> if the connection went through, <tt>false</tt> if
	 *         an error occured.
	 */
	public boolean login() {
		try {
			this.mClient.login();
			return true;

		} catch (final DiscordException e) {
			return false;

		}
	}

	/**
	 * Logs this client out. Disconnects from the discord gateway and returns
	 * <tt>true</tt> if the disconnect went trough, <tt>false</tt> if an error
	 * occured.
	 * 
	 * @return <tt>True</tt> if the disconnect went trough, <tt>false</tt> if an
	 *         error occured.
	 */
	public boolean logout() {
		try {
			this.mClient.logout();
			return true;

		} catch (final DiscordException e) {
			return false;

		}
	}

	/**
	 * Sets the ready flag to <tt>true</tt>, signaling that the API is connected
	 * to the gateway and ready to use. Also calls {@link DiscordClient#start()}
	 * to start the routine.
	 */
	public void ready() {
		this.mReady = true;
		this.fetchData();

	}

	public void updatePresence(final IUser user, final StatusType statusType) {
		this.mPresence.put(user, statusType);

	}

	private void serveRequest(final IMessage message) {
		if (message.getAuthor().equals(this.mClient.getOurUser())) {
			return;

		}

		// the message given is a private message, so we respond in another way.
		if (message.getChannel().isPrivate()) {
			message.reply("please refrain from sending direct messages to me unless you're reporting "
					+ "a bug or anything the like. If you're trying to use bot-commands, type !help in "
					+ "any text channel where this bot is present. An email containing your query will "
					+ "be sent to my administrator. He will fix the problem ASAP.");

			Mail.send(this.mMailSettingsProvider, "Bugfix (PM/DM)", message.getAuthor().getName() + ": \""
					+ message.getContent() + "\" on the " + TimeStamp.defaultFormat(message.getTimestamp()));
			return;

		}

		if (!message.getContent().startsWith(COMMAND_IDENTIFIER)) {
			return;

		}
		final List<IRole> authorRoles = message.getAuthor().getRolesForGuild(this.mGuild);
		// final IChannel messageChannel = message.getChannel();

		boolean hasPermission = false;

		for (final IRole role : authorRoles) {
			if (role.getName().equals(ADMIN_ROLE_NAME)) {
				hasPermission = true;

			}

		}

		final String input[] = message.getContent().substring(1).split("\\s", 2);
		final String command = input[0].toLowerCase();
		final String additionalCommands = input.length > 1 ? input[1] : "";

		if (this.commands.containsKey(command.toUpperCase())) {
			final String description = this.commands.get(command.toUpperCase());

			// implemention for admin commands
			if (description.startsWith("[" + ADMIN_ROLE_NAME + "]")) {
				if (!hasPermission) {
					message.reply(NO_PERMISSION_NOTIFIER);
					return;
				}

				if (command.equals("logout")) {
					message.reply("Logout successful.");
					this.logout();

				} else if (command.equals("send")) {
					message.reply("Sending email...");

					Mail.send(this.mMailSettingsProvider, "Admin-Message", additionalCommands);

					message.reply("Email sent.");

				} else if (command.equals("addcommand")) {
					if (this.commands.containsKey(additionalCommands.toUpperCase())) {
						message.reply("failed to create the following command: " + additionalCommands
								+ ", since it is already present.");
						return;
					}

					this.mCommandSettingsProvider.setSetting(additionalCommands.toUpperCase(),
							"[unspecified] !" + additionalCommands + ": Added but not yet implemented.");
					this.mCommandSettingsProvider.saveSettings();

					this.commands = this.mCommandSettingsProvider.getAllSettings();

					Mail.send(this.mMailSettingsProvider, "Implementation of new command", additionalCommands);

					message.reply(
							"successfully added the command. Also sent an email to the administrator for an implementation.");

				} else if (command.equals("removecommand")) {
					if (!this.commands.containsKey(additionalCommands.toUpperCase())) {
						message.reply("failed to remove the following command: " + additionalCommands
								+ ", since it is not present");
						return;

					}
					this.mCommandSettingsProvider.removeSetting(additionalCommands.toUpperCase());
					this.mCommandSettingsProvider.saveSettings();

					this.commands = this.mCommandSettingsProvider.getAllSettings();

					message.reply("successfully removed the command.");

				}

				// implementation for not admin commands
			} else {
				if (command.equals("help")) {
					if (additionalCommands.length() > 0) {
						// help was called with its optional parameter
						if (!this.commands.containsKey(additionalCommands.toUpperCase())) {
							message.reply("didn't recognize the following command: " + additionalCommands);
							return;

						}
						message.reply(this.commands.get(additionalCommands.toUpperCase()));
						return;

					}
					final StringJoiner joiner = new StringJoiner("\n\n");
					joiner.add("The syntax for the following descriptions is as follows:\n\n"
							+ "[], the query between these parantheses signals which role is atleast needed to execute the command.\n"
							+ "<>, the query between the diamond operator signals that this has to be specified, otherwise the command won't work, for example: !send hello.\n"
							+ "<<>>, the query between the doubled diamond operator signals that this argument doesn't have to be specified.\n");

					for (final Entry<String, String> entry : this.commands.entrySet()) {
						joiner.add(entry.getValue());

					}
					message.reply(joiner.toString());
					return;

				}
				message.reply(this.commands.get(command.toUpperCase()));

			}
		}
	}

	private void checkForChangedRoles() throws ChangedAdminRoleNameException {
		for (final IRole role : this.mGuild.getRoles()) {
			if (role.getName().equals(ADMIN_ROLE_NAME)) {
				return;

			}
		}
		throw new ChangedAdminRoleNameException();
	}

	private void start() {
		final int[] lastBufferSizes = new int[this.maxChannels];

		this.mGuild = this.mClient.getGuildByID(this.mClientSettingsProvider.getGuildID());

		try {
			checkForChangedRoles();

		} catch (final ChangedAdminRoleNameException e1) {
			e1.printStackTrace();

		}

		while (true) {
			for (int j = 0; j < this.mClient.getChannels(true).size(); j++) {
				MessageHistory history = this.mClient.getChannels(true).get(j).getMessageHistory();

				for (int i = 0; i < history.size() - lastBufferSizes[j]; i++) {
					this.serveRequest(history.get(i));

				}
				lastBufferSizes[j] = history.size();

			}

			try {
				Thread.sleep(WAIT_PER_CYCLE);

			} catch (final InterruptedException e) {
				System.err.println("This thread got interrupted while sleeping.");

			}
		}
	}
}
