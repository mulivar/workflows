package neota.workflow.elements.nodes;


public class NopNode extends Node
{
	public NopNode(String id, String name)
	{
		super(id, NodeType.NOP, name);
	}

	
	@Override
	public void execute()
	{	
	}
}
