package neota.workflow.server.cli.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import neota.workflow.commands.CliCommand;
import neota.workflow.commands.CommandProcessor;
import neota.workflow.server.WorkflowHandler;


/**
 * @author leto
 */
public class CliCommandProcessor implements CommandProcessor
{
	Map<CommandType, AvailableCommand> commands = new HashMap<>();
	
	WorkflowHandler workflows;
	
	
	public CliCommandProcessor(WorkflowHandler handler)
	{
		// add all the commands
		commands.put(CommandType.CREATE_WORKFLOW, new AvailableCommand(1, "create", "workflow"));
		commands.put(CommandType.CREATE_SESSION, new AvailableCommand(1, "create", "session"));
		commands.put(CommandType.RESUME_SESSION, new AvailableCommand(1, "resume", "session"));
		commands.put(CommandType.GET_SESSION, new AvailableCommand(1, "get", "session"));
		commands.put(CommandType.SET_TIMEOUT, new AvailableCommand(1, "set", "timeout"));
		
		workflows = handler;
	}
	
	
	@Override
	public CliCommand makeCommand(String data)
	{
		data = data.toLowerCase();
		String[] tokens = data.split("\\s");
		if (tokens.length != 3)
		{
			throw new RuntimeException("Invalid command supplied");
		}
		
		List<String> actualTokens = Arrays.asList(tokens[0], tokens[1]);
		String parameter = tokens[2];
		
		CommandType type = null;
		for (Map.Entry<CommandType, AvailableCommand> entry : commands.entrySet())
		{
			if (entry.getValue().isMatch(1, actualTokens))
			{
				type = entry.getKey();
				break;
			}
		}
		
		CliCommand command;
		
		switch(type)
		{
		case CREATE_SESSION:
			command = new CreateSessionCommand(workflows, parameter);
			break;
		case CREATE_WORKFLOW:
			command = new CreateWorkflowCommand(workflows, parameter);
			break;
		case GET_SESSION:
			command = new GetSessionCommand(workflows, parameter);
			break;
		case RESUME_SESSION:
			command = new ResumeSessionCommand(workflows, parameter);
			break;
		case SET_TIMEOUT:
			command = new SetTimeoutCommand(workflows, parameter);
			break;
		default:
			throw new RuntimeException("Invalid command supplied");
		}
		
		return command;
	}
}
