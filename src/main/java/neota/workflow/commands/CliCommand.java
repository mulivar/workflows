package neota.workflow.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import neota.workflow.server.WorkflowHandler;


@Data
@AllArgsConstructor
public abstract class CliCommand implements Command
{
	protected WorkflowHandler workflows;
	protected String argument;
}
