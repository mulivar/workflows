package neota.workflow.server;

import neota.workflow.server.cli.CliCommandServer;


/**
 * The server that offers interface(s) to the outside world.
 */
public class Server
{
	WorkflowHandler workflows = new WorkflowHandler();
	
	CommandServer cli;
	
	
	/**
	 * Starts the server until interrupted from outside.
	 */
	public void startCliInterface()
	{
		cli = new CliCommandServer(workflows);
		cli.startServer();
	}
}
