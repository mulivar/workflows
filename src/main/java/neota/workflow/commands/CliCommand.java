package neota.workflow.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import neota.workflow.server.WorkflowHandler;


/**
 * The base of all other CLI commands.
 * @author iackar
 */
@Data
@AllArgsConstructor
public abstract class CliCommand implements Command
{
	/** The workflow handler that keeps info about all the loaded workflows. */
	protected WorkflowHandler workflows;
	
	/** Any arguments supplied to the actual command (i.e. does not include the command itself). */
	protected String argument;
}
