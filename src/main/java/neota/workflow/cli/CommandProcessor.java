package neota.workflow.cli;

import neota.workflow.cli.commands.Command;

/**
 * The command processor responsible for creating commands.
 */
public interface CommandProcessor
{
	/**
	 * Instantiates a valid command object based on the string data provided; the form
	 * of the string is open to interpretation by the inherited classes.
	 * @param data the data needed for the command instantiation
	 * @return the actual command to execute
	 */
	Command makeCommand(String data);
}
