package neota.workflow.cli.commands;

import lombok.Getter;
import neota.workflow.server.WorkflowHandler;


/**
 * 
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
	public CommandInfo execute()
	{
		workflows.setTimeout(Integer.valueOf(argument));
		return new CommandInfo("", CommandInfo.Status.SUCCESS);
	}
}
