package neota.workflow.elements;

import lombok.Getter;
import lombok.Setter;
import neota.workflow.elements.nodes.Node;


@Getter
@Setter
public abstract class State
{
	public static enum StateEnum
	{
		NOT_STARTED,
		EXECUTING,
		TASK_COMPLETE,
		WAITING,
		COMPLETED
	}

	protected StateEnum state;
	protected Node node;
	protected SessionCallback callback;
	
	
	protected State(Node node, SessionCallback callback)
	{
		this.node = node;
		this.callback = callback;
	}
	
	
	public StateEnum getState()
	{
		return state;
	}
	
	
	public Node getCurrentNode()
	{
		return node;
	}
	
	
	public abstract void advance(Session session);
}
