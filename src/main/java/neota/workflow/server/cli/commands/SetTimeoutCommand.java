package neota.workflow.server.cli.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import neota.workflow.commands.Command;
import neota.workflow.server.WorkflowHandler;


/**
 * 
 * @author leto
 *
 */
@AllArgsConstructor
@Getter
public class SetTimeoutCommand implements Command
{
	WorkflowHandler workflows;
	String argument;
	

	@Override
	public void execute()
	{
	}
}
