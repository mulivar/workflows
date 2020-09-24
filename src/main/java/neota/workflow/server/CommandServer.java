package neota.workflow.server;


/**
 * The command interface models a generic version of a command-handling router.
 */
public interface CommandServer
{
	/**
	 * Starts a server, nonblocking mode.
	 */
	void startServer();
	
	/**
	 * Stops a server, i.e. the server stops taking incoming commands/connections.
	 */
	void stopServer();
}
