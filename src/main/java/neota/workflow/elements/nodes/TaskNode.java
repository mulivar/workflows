package neota.workflow.elements.nodes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TaskNode extends Node
{
	private Logger log = LoggerFactory.getLogger(TaskNode.class);
	
	public TaskNode(String id, String name)
	{
		super(id, Node.Type.TASK, name);
	}

	
	@Override
	public void runNodeTask()
	{	
		try
		{
			Thread.sleep(getTimeout() * 1000);
		}
		catch (InterruptedException e)
		{
			log.error("An interupt occurred while executing the task node, message = " + e.getMessage());
		}
	}
}
