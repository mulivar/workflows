package neota.workflow.server.cli.commands;

import lombok.Getter;
import neota.workflow.commands.CliCommand;
import neota.workflow.commands.CommandInfo;
import neota.workflow.server.WorkflowHandler;


/**
 * 
 * @author iackar
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
