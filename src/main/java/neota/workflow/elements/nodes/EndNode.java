package neota.workflow.elements.nodes;


public class EndNode extends Node
{
	public EndNode(String id, String name)
	{
		super(id, NodeType.END, name);
	}

	
	@Override
	public void execute()
	{	
	}
}
