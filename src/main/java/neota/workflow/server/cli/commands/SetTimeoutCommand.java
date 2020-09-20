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
public class SetTimeoutCommand extends CliCommand
{
	public SetTimeoutCommand(WorkflowHandler workflows, String argument)
	{
		super(workflows, argument);
	}

	
	@Override
	public CommandStatus execute()
	{
		workflows.setTimeout(Integer.valueOf(argument));
		return new CommandStatus("", Status.SUCCESS);
	}
}
