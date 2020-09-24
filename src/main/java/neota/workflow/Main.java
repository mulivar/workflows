package neota.workflow;

import neota.workflow.server.CommandServer;
import neota.workflow.server.SessionExecutor;
import neota.workflow.server.WorkflowHandler;
import neota.workflow.cli.CliCommandServer;


/**
 * Jumpstart a single or a series of interfaces that will process commands.
 */
public class Main
{
	public static void main(String[] args)
	{
		/** The session executor that handles the sessions. */
		SessionExecutor executor = new SessionExecutor();
		
		/** The single workflow handler for all interfaces. */
		WorkflowHandler workflows = new WorkflowHandler(executor);
		
		/** The command server that processes CLI commands. */
		CommandServer cli = new CliCommandServer(workflows);
		
		cli.startServer();
	}
}
