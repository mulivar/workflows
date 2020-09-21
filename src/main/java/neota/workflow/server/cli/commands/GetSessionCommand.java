package neota.workflow.server.cli.commands;

import lombok.Getter;
import neota.workflow.commands.CliCommand;
import neota.workflow.commands.CommandStatus;
import neota.workflow.commands.Status;
import neota.workflow.elements.Session;
import neota.workflow.server.WorkflowHandler;


/**
 * 
 * @author iackar
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
		String output = session.getCurrentLane().getName();
		if (session.getState() == Session.State.COMPLETED)
		{
			output = "Ended";
		}
		
		return new CommandStatus(output, Status.SUCCESS);
	}
}
