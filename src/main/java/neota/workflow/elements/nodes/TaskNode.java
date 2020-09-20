package neota.workflow.elements.nodes;


public class TaskNode extends Node
{
	public TaskNode(String id, String name)
	{
		super(id, NodeType.TASK, name);
	}

	
	@Override
	public void execute()
	{	
	}
}
