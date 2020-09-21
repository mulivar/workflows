package neota.workflow.server.cli.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;


@Getter
public class AvailableCommand
{
	private List<String> tokens = new ArrayList<>();
	private List<String> arguments = new ArrayList<>();
	private int argumentCount = 0;
	
	
	public AvailableCommand(Collection<String> tokens, Collection<String> arguments)
	{
		this.tokens.addAll(tokens);
		this.arguments.addAll(arguments);
		this.argumentCount = arguments.size();
	}
	
	
	public boolean isMatch(List<String> tokens)
	{
		if (tokens.size() < this.tokens.size() || tokens.size() != this.tokens.size() + argumentCount)
		{
			return false;
		}
		else
		{
			// positional checking
			for (int i = 0; i < this.tokens.size(); i++)
			{
				if (!this.tokens.get(i).equalsIgnoreCase(tokens.get(i)))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	public String getDescription()
	{
		List<String> args = arguments.stream()
				.map(arg -> "$" + arg)
				.collect(Collectors.toList());
		
		return String.join(" ", tokens) + " " + String.join(" ", args);
	}
}
