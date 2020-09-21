package neota.workflow.server.cli;

import java.text.MessageFormat;

import neota.workflow.elements.Session;
import neota.workflow.elements.nodes.Node;
import neota.workflow.server.SessionObserver;
import neota.workflow.server.TaskObserver;


public class CliReactor implements TaskObserver, SessionObserver
{
	@Override
	public void onTaskComplete(Node node)
	{
		String output = "";
		if (node.getType() == Node.Type.TASK)
		{
			output = node.getName();
		}
		else if (node.getType() == Node.Type.NOP)
		{
			output = "NOP";
		}
		
		if (!output.isEmpty())
		{
			output += " completed";
			System.out.println(output);
		}
	}
	
	
	@Override
	public void onSessionComplete(Session session)
	{
		System.out.println(MessageFormat.format("Session {0} completed", session.getId()));
	}
}
