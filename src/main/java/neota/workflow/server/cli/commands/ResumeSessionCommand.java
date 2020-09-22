package neota.workflow.server.cli.commands;

import java.text.MessageFormat;

import lombok.Getter;
import neota.workflow.commands.CliCommand;
import neota.workflow.commands.CommandInfo;
import neota.workflow.exceptions.ValidationException;
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
	public CommandInfo execute()
	{
		try
		{
			workflows.resumeSession(argument);
			return new CommandInfo("", CommandInfo.Status.SUCCESS);
		}
		catch (ValidationException e)
		{
			return new CommandInfo(MessageFormat.format("Unable to resume the session, reason = {0}",
					e.getMessage()), CommandInfo.Status.WARNING);
		}
	}
}
