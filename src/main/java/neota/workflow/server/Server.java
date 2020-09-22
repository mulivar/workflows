package neota.workflow.server;

import neota.workflow.server.cli.CliCommandServer;


/**
 * The server that offers interface(s) to the outside world.
 */
public class Server
{
	/** The single workflow handler for all interfaces. */
	WorkflowHandler workflows = new WorkflowHandler();
	
	/** The command server that processes CLI commands. */
	CommandServer cli;
	
	
	/**
	 * Starts the CLI command server in the background.
	 */
	public void startCliInterface()
	{
		cli = new CliCommandServer(workflows);
		cli.startServer();
	}
}
