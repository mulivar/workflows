package neota.workflow.elements.nodes;


public class NopNode extends Node
{
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
			// TODO
		}
	}
}
