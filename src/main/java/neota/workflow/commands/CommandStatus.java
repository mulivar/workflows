package neota.workflow.commands;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CommandStatus
{
	private String message;
	private Status status;
}
