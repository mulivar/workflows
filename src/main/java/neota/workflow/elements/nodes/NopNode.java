package neota.workflow.elements.nodes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NopNode extends Node
{
	private Logger log = LoggerFactory.getLogger(NopNode.class);
	
	public NopNode(String id, String name)
	{
		super(id, Node.Type.NOP, name);
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
			log.error("An interupt occurred while executing the NOP node, message = " + e.getMessage());
		}
	}
}
