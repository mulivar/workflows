package neota.workflow.server.cli.commands;

import java.io.IOException;
import java.text.MessageFormat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.Getter;
import neota.workflow.commands.CliCommand;
import neota.workflow.commands.CommandInfo;
import neota.workflow.exceptions.ValidationException;
import neota.workflow.server.WorkflowHandler;


/**
 * @author iackar
 */
@Getter
public class CreateWorkflowCommand extends CliCommand
{
	public CreateWorkflowCommand(WorkflowHandler workflows, String argument)
	{
		super(workflows, argument);
	}

	
	@Override
	public CommandInfo execute()
	{
		try
		{
			final String workflowId = workflows.loadFromJson(argument);
			return new CommandInfo("Created workflow, ID = " + workflowId, CommandInfo.Status.SUCCESS);
		}
		catch (JsonParseException e)
		{
			return new CommandInfo(MessageFormat.format("Unable to parse the input JSON, reason = {0}",
					e.getMessage()), CommandInfo.Status.ERROR);
		}
		catch (JsonMappingException e)
		{
			return new CommandInfo(MessageFormat.format("Unable to map the input JSON to data structures, reason = {0}",
					e.getMessage()), CommandInfo.Status.ERROR);
		}
		catch (IOException e)
		{
			return new CommandInfo(MessageFormat.format("Unable to open the file {0} for reading, reason = {1}",
					argument, e.getMessage()), CommandInfo.Status.ERROR);
		}
		catch (ValidationException e)
		{
			return new CommandInfo(MessageFormat.format("Unable to validate the loaded workflow, reason = {0}",
					e.getMessage()), CommandInfo.Status.WARNING);
		}
	}
}
