package neota.workflow.elements.nodes;


public class StartNode extends Node
{
	public StartNode(String id, String name)
	{
		super(id, Node.Type.START, name);
	}

	
	@Override
	public void runNodeTask()
	{	
	}
}
