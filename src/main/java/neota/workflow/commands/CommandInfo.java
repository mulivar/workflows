package neota.workflow.commands;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * The command status conveying info about the just executed command.
 * @author iackar
 */
@Data
@AllArgsConstructor
public class CommandInfo
{
	/**
	 * Possible exit codes for the command status.
	 */
	public static enum Status
	{
		SUCCESS,
		WARNING,
		ERROR
	}

	
	/** The command result message. */
	private String message;
	
	/** The command result status. */
	private Status status;
}
