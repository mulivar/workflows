package neota.workflow.server;

import neota.workflow.server.cli.CliCommandServer;


/**
 * The server that offers interface(s) to the outside world.
 */
public class Server
{
	WorkflowHandler workflows = new WorkflowHandler();
	
	CommandServer cli;
	CommandServer rest;
	
	
	/**
	 * Starts the server until interrupted from outside.
	 */
	public void startCliInterface()
	{
		cli = new CliCommandServer(workflows);
		cli.startServer();
	}
	
	
	/**
	 * Start the REST interface for processing of the commands.
	 */
	public void startRestInterface()
	{
		// some idea of starting a REST server that would also process the commands
		/*
		rest = new RestCommandServer(workflows);
		rest.startServer();
		*/
	}
}
