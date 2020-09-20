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
 *
 */
@Getter
public class ResumeSessionCommand extends CliCommand
{
	public ResumeSessionCommand(WorkflowHandler workflows, String argument)
	{
		super(workflows, argument);
	}
	

	@Override
	public CommandStatus execute()
	{
		Session session = workflows.resumeSession(argument);
		return new CommandStatus("", Status.SUCCESS);
	}
}
