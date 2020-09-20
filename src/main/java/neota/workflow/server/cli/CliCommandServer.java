package neota.workflow.server.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.MessageFormat;

import lombok.Getter;
import lombok.Setter;
import neota.workflow.commands.Command;
import neota.workflow.commands.CommandProcessor;
import neota.workflow.commands.CommandStatus;
import neota.workflow.commands.Status;
import neota.workflow.server.CommandServer;
import neota.workflow.server.WorkflowHandler;
import neota.workflow.server.cli.commands.CliCommandProcessor;


/**
 * @author leto
 */
@Getter @Setter
public class CliCommandServer extends Thread implements CommandServer
{
	WorkflowHandler workflows;
	CommandProcessor commander;
	
	boolean running = false;
	
	
	public CliCommandServer(WorkflowHandler handler)
	{
		workflows = handler;
		commander = new CliCommandProcessor(workflows);
	}
	
	
	@Override
	public void run()
	{
		super.run();
		
		try
		{
			running = true;
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			String line;
			while ((line = reader.readLine()) != null)
			{
				try
				{
					Command command = commander.makeCommand(line);
					CommandStatus status = command.execute();
					
					PrintStream output = status.getStatus() == Status.ERROR ? System.err : System.out;
					
					if (!status.getMessage().isEmpty()) 
					{
						output.println(status.getMessage());
					}
				}
				catch (Exception e)
				{
					System.err.println(MessageFormat.format(
							"An error occurred while executing the command {0}, message = {1}", line, e.getMessage()));
				}
				
				if (!running)
				{
					break;
				}
			}
		}
		catch (IOException e) {
			System.err.println(MessageFormat.format(
					"An error occurred while reading from the command line, message = {0}", e.getMessage()));
		}
		
		running = false;
	}
	
	
	@Override
	public synchronized void startServer()
	{
		if (!isAlive())
		{
			start();
		}
	}

	
	@Override
	public synchronized void stopServer()
	{
		if (isAlive())
		{
			setRunning(false);
		}
	}

	
	@Override
	public void getServerState()
	{
	}
	

	@Override
	public void getCommandStats()
	{
	}
}
