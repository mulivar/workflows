package neota.workflow.server.cli.commands;

import lombok.Getter;
import neota.workflow.commands.CliCommand;
import neota.workflow.commands.CommandStatus;
import neota.workflow.commands.Status;
import neota.workflow.elements.Session;
import neota.workflow.server.WorkflowHandler;


/**
 * 
 * @author leto
 */
@Getter
public class GetSessionCommand extends CliCommand
{
	public GetSessionCommand(WorkflowHandler workflows, String argument)
	{
		super(workflows, argument);
	}
	

	@Override
	public CommandStatus execute()
	{
		Session session = workflows.getSession(argument);
		return new CommandStatus(session.getLane(), Status.SUCCESS);
	}
}
