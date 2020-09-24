package neota.workflow.cli;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neota.workflow.elements.Session;
import neota.workflow.elements.nodes.Node;
import neota.workflow.server.SessionObserver;


public class CliReactor implements SessionObserver
{
	private Logger log = LoggerFactory.getLogger(CliReactor.class);
	
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
			log.info(output);
		}
	}
	
	
	@Override
	public void onSessionComplete(Session session)
	{
		final String message = MessageFormat.format("Session {0} completed", session.getId());
		System.out.println(message);
		log.info(message);
	}
}
