package neota.workflow.server.cli.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import neota.workflow.commands.CliCommand;
import neota.workflow.commands.CommandProcessor;
import neota.workflow.server.WorkflowHandler;


/**
 * @author iackar
 */
public class CliCommandProcessor implements CommandProcessor
{
	private Map<CommandType, AvailableCommand> commands = new HashMap<>();
	
	private WorkflowHandler workflows;
	
	
	public CliCommandProcessor(WorkflowHandler handler)
	{
		// add all the commands
		commands.put(CommandType.CREATE_WORKFLOW,
				new AvailableCommand(Arrays.asList("create", "workflow"), Arrays.asList("path/to/workflow.json")));
		commands.put(CommandType.CREATE_SESSION,
				new AvailableCommand(Arrays.asList("create", "session"), Arrays.asList("sessionId")));
		commands.put(CommandType.RESUME_SESSION,
				new AvailableCommand(Arrays.asList("resume", "session"), Arrays.asList("sessionId")));
		commands.put(CommandType.GET_SESSION,
				new AvailableCommand(Arrays.asList("get", "session"), Arrays.asList("sessionId")));
		commands.put(CommandType.SET_TIMEOUT,
				new AvailableCommand(Arrays.asList("set", "timeout"), Arrays.asList("timeout_in_seconds")));
		
		workflows = handler;
	}
	
	
	public Collection<AvailableCommand> getAvailableCommands()
	{
		return commands.values();
	}
	
	
	public String getAvailableCommandsAsString()
	{
		StringJoiner joiner = new StringJoiner("\n");
		getAvailableCommands().forEach(command -> joiner.add(command.getDescription()));
		return joiner.toString();
	}
	
	
	@Override
	public CliCommand makeCommand(String data)
	{
		data = data.toLowerCase();
		List<String> tokens = Arrays.asList(data.split("\\s"));
		
		CommandType type = null;
		for (Map.Entry<CommandType, AvailableCommand> entry : commands.entrySet())
		{
			if (entry.getValue().isMatch(tokens))
			{
				type = entry.getKey();
				break;
			}
		}
		
		CliCommand command;
		
		switch(type)
		{
		case CREATE_SESSION:
			command = new CreateSessionCommand(workflows, tokens.get(tokens.size() - 1));
			break;
		case CREATE_WORKFLOW:
			command = new CreateWorkflowCommand(workflows, tokens.get(tokens.size() - 1));
			break;
		case GET_SESSION:
			command = new GetSessionCommand(workflows, tokens.get(tokens.size() - 1));
			break;
		case RESUME_SESSION:
			command = new ResumeSessionCommand(workflows, tokens.get(tokens.size() - 1));
			break;
		case SET_TIMEOUT:
			command = new SetTimeoutCommand(workflows, tokens.get(tokens.size() - 1));
			break;
		default:
			throw new RuntimeException("Invalid command supplied");
		}
		
		return command;
	}
}
