package neota.workflow.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neota.workflow.server.CommandServer;
import neota.workflow.server.WorkflowHandler;
import neota.workflow.cli.commands.Command;
import neota.workflow.cli.commands.CommandInfo;


/**
 * The CLI command server responsible for reading and executing commands.
 */
public class CliCommandServer implements CommandServer
{
	private Logger log = LoggerFactory.getLogger(CliCommandServer.class);
	
	/** Keeps track of all the workflows in the system. */
	WorkflowHandler workflows;
	
	/** Reacts to the task complete and session complete events. */
	CliReactor reactor = new CliReactor();
	
	/** The commander that knows how to actually instantiate concrete commands. */
	CommandProcessor commander;
	
	/** Runs the command reading thread. */
	ExecutorService commandExecutor = Executors.newSingleThreadExecutor();
	
	
	/**
	 * Creates a new CLI command server based on the provided workflow handler.
	 * @param handler the workflow handler that hosts the workflows
	 */
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
		
		commandExecutor.submit(() -> {
			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				
				String line;
				while ((line = reader.readLine()) != null)
				{
					if (line.isEmpty())
					{
						continue;
					}
					
					try
					{
						Command command = commander.makeCommand(line);
						CommandInfo info = command.execute();

						if (!info.getMessage().isEmpty())
						{
							if (info.getStatus() == CommandInfo.Status.ERROR)
							{
								System.err.println(info.getMessage());
								log.error(info.getMessage());
							}
							else
							{
								System.out.println(info.getMessage());
								log.info(info.getMessage());
							}
						}
					}
					catch (Exception e)
					{
						final String message = MessageFormat.format(
								"An error occurred while executing the command {0}, message = {1}", line, e.getMessage());
						System.err.println(message);
						log.error(message);
					}
				}

				workflows.shutdown();
			}
			catch (IOException e) {
				final String message = MessageFormat.format(
						"An error occurred while reading from the command line, message = {0}", e.getMessage());
				System.err.println(message);
				log.error(message);
			}
		});
	}

	
	@Override
	public void stopServer()
	{
		workflows.shutdown();
		commandExecutor.shutdown();
	}
}
