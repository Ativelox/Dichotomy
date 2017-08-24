package de.ativelox.dichotomy.client.exceptions;

import de.ativelox.dichotomy.client.DiscordClient;

/**
 *
 *
 * @author Ativelox {@literal <ativelox.dev@web.de>}
 *
 */
public class ChangedAdminRoleNameException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ChangedAdminRoleNameException() {
		super("The name of the Admin role has most likely changed. Check the roles for further information. Admin-Role naming so far: "
				+ DiscordClient.ADMIN_ROLE_NAME);
	}

}
