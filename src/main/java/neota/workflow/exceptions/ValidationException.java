package neota.workflow.exceptions;

import java.text.MessageFormat;

import lombok.Getter;


/**
 * The exception related to any kind of validation error.
 */
@Getter
public class ValidationException extends Exception
{
	private static final long serialVersionUID = -1403211494551755074L;
	
	/** The format to use for creating new printouts. */
	private static final String FORMAT = "A validation exception occurred; message = {0}";
	
	/** The line where the error occurred. */
	private int line;
	
	/** The file or object in which the error occurred. */
	private String object;

	
	public ValidationException(String message)
	{
		super(MessageFormat.format(FORMAT, message));
	}
	
	
	public ValidationException(String message, String object)
	{
		super(MessageFormat.format(FORMAT + ", object = {1}", message, object));
		
		this.object = object;
	}
	
	
	public ValidationException(String message, String object, int line)
	{
		super(MessageFormat.format(FORMAT + ", object = {1}, line = {2}", message, object, line));
		
		this.object = object;
		this.line = line;
	}
}
