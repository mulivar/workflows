package neota.workflow.cli.commands;

import lombok.Getter;
import neota.workflow.exceptions.ValidationException;
import neota.workflow.server.WorkflowHandler;


/**
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
	public CommandInfo execute()
	{
		final String sessionId = workflows.createSession(argument);
		return new CommandInfo("Created session, ID = " + sessionId, CommandInfo.Status.SUCCESS);
	}
}
