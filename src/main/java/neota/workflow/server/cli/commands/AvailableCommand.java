package neota.workflow.server.cli.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lombok.Getter;


@Getter
public class AvailableCommand
{
	private List<String> tokens = new ArrayList<>();
	private int argumentCount = 0;
	
	
	public AvailableCommand(int argumentCount, Collection<String> tokens)
	{
		this.tokens.addAll(tokens);
		this.argumentCount = argumentCount;
	}
	
	
	public AvailableCommand(int argumentCount, String... tokens)
	{
		this.argumentCount = argumentCount;
		this.tokens.addAll(Arrays.asList(tokens));
	}
	
	
	public boolean isMatch(int argumentCount, Collection<String> tokens)
	{
		if (this.argumentCount == argumentCount && this.tokens.equals(tokens))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
