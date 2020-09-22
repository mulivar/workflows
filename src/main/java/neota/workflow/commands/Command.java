package neota.workflow.commands;

import neota.workflow.exceptions.ValidationException;

/**
 * The command that conveys both the data and the action from the user to the command server.
 * @author iackar
 */
public interface Command
{
	/**
	 * Executes the command. The method is blocking so care should be taken in order to ensure
	 * that the execution is prompt and fast.
	 * @return the command status
	 */
	CommandInfo execute();
}
