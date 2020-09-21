package neota.workflow.elements.nodes;


public class TaskNode extends Node
{
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
			// TODO
		}
	}
}
