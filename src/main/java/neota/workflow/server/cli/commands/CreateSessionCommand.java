package neota.workflow.server.cli.commands;

import lombok.Getter;
import neota.workflow.commands.CliCommand;
import neota.workflow.commands.CommandStatus;
import neota.workflow.commands.Status;
import neota.workflow.server.WorkflowHandler;


/**
 * 
 * @author leto
 *
 */
@Getter
public class CreateSessionCommand extends CliCommand
{
	public CreateSessionCommand(WorkflowHandler workflows, String argument)
	{
		super(workflows, argument);
	}

	
	@Override
	public CommandStatus execute()
	{
		final String sessionId = workflows.createSession(Integer.valueOf(argument));
		return new CommandStatus("Created session, ID = " + sessionId, Status.SUCCESS);
	}
}
