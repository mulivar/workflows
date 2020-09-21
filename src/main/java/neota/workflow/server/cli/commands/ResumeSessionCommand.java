package neota.workflow.server.cli.commands;

import lombok.Getter;
import neota.workflow.commands.CliCommand;
import neota.workflow.commands.CommandStatus;
import neota.workflow.commands.Status;
import neota.workflow.server.WorkflowHandler;


/**
 * 
 * @author iackar
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
		workflows.resumeSession(argument);
		return new CommandStatus("", Status.SUCCESS);
	}
}
