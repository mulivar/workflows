package neota.workflow.server.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import neota.workflow.commands.Command;
import neota.workflow.commands.CommandProcessor;
import neota.workflow.commands.CommandStatus;
import neota.workflow.commands.Status;
import neota.workflow.server.CommandServer;
import neota.workflow.server.WorkflowHandler;
import neota.workflow.server.cli.commands.CliCommandProcessor;


/**
 * @author iackar
 */
public class CliCommandServer implements CommandServer
{
	CliReactor reactor = new CliReactor();
	
	WorkflowHandler workflows;
	CommandProcessor commander;
	
	ExecutorService executor = Executors.newSingleThreadExecutor();
	
	
	public CliCommandServer(WorkflowHandler handler)
	{
		workflows = handler;
		commander = new CliCommandProcessor(workflows);
		
		workflows.registerSessionObserver(reactor);
		workflows.registerTaskObserver(reactor);
	}
	
	
	@Override
	public void startServer()
	{
		System.out.println("Available commands:");
		System.out.println(((CliCommandProcessor) commander).getAvailableCommandsAsString());
		
		executor.submit(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
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
					}

					workflows.shutdown();
				}
				catch (IOException e) {
					System.err.println(MessageFormat.format(
							"An error occurred while reading from the command line, message = {0}", e.getMessage()));
				}
			}
		});
	}

	
	@Override
	public void stopServer()
	{
		workflows.shutdown();
		executor.shutdown();
	}
}
