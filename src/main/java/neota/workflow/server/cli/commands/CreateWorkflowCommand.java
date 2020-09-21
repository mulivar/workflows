package neota.workflow.server.cli.commands;

import java.io.IOException;
import java.text.MessageFormat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
public class CreateWorkflowCommand extends CliCommand
{
	public CreateWorkflowCommand(WorkflowHandler workflows, String argument)
	{
		super(workflows, argument);
	}

	
	@Override
	public CommandStatus execute()
	{
		try
		{
			final int workflowId = workflows.loadFromJson(argument);
			return new CommandStatus("Created workflow, ID = " + workflowId, Status.SUCCESS);
		}
		catch (JsonParseException e)
		{
			return new CommandStatus(MessageFormat.format("Unable to parse the input JSON, reason = {0}",
					e.getMessage()), Status.ERROR);
		}
		catch (JsonMappingException e)
		{
			return new CommandStatus(MessageFormat.format("Unable to map the input JSON to data structures, reason = {0}",
					e.getMessage()), Status.ERROR);
		}
		catch (IOException e)
		{
			return new CommandStatus(MessageFormat.format("Unable to open the file {0} for reading, reason = {1}",
					argument, e.getMessage()), Status.ERROR);
		}
	}
}
