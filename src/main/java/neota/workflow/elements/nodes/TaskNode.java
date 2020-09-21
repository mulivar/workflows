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
			Thread.sleep(10000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
