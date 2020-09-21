package neota.workflow.elements.nodes;


public class EndNode extends Node
{
	public EndNode(String id, String name)
	{
		super(id, Node.Type.END, name);
	}

	
	@Override
	public void runNodeTask()
	{
	}
}
