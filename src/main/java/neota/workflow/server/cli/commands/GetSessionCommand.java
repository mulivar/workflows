package neota.workflow.server.cli.commands;

import lombok.Getter;
import neota.workflow.commands.CliCommand;
import neota.workflow.commands.CommandStatus;
import neota.workflow.commands.Status;
import neota.workflow.elements.Session;
import neota.workflow.elements.nodes.Node;
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
		
		String output = "";
		switch (session.getState())
		{
		case COMPLETED:
			output = "Ended";
			break;
		case EXECUTING:
			if (session.getCurrentNode().getType() == Node.Type.NOP)
			{
				output = "NOP";
			}
			else
			{
				output = session.getCurrentNode().getName() + " in progress";
			}
			break;
		case WAITING:
			output = session.getCurrentLane().getName();
			break;
		case NOT_STARTED:
			throw new IllegalStateException("The session hasn't been started yet");
		}
		
		return new CommandStatus(output, Status.SUCCESS);
	}
}
