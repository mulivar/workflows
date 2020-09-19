package neota.workflow;

import neota.workflow.server.Server;


/**
 * Jumpstart a single or a series of interfaces that will process commands.
 * @author iackar
 */
public class Main
{
	public static void main(String[] args)
	{
		Server server = new Server();
		
		server.startCliInterface();
	}
}
