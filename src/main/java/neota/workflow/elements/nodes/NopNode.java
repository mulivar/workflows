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
			Thread.sleep(10000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
