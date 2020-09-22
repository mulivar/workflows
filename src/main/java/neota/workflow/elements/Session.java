package neota.workflow.elements;

import lombok.Getter;
import lombok.Setter;
import neota.workflow.elements.nodes.Node;


public class Session
{
	
	@Getter
	private String id;
	
	@Getter
	private Workflow workflow;
	
	@Getter @Setter
	//private State state = State.NOT_STARTED;
	private State state;
	
	@Getter
	private Node currentNode;
	
	private SessionCallback callback;
	
	
	public Session(String id, Workflow workflow, SessionCallback callback)
	{
		this.id = id;
		this.workflow = workflow;
		this.callback = callback;
		
		// initialise the current node to the starting node
		final Lane startLane = workflow.getLane(workflow.getStartLaneId());
		final Node startNode = workflow.getNode(startLane.getStartNodeId());
		
		this.currentNode = startNode;
		
		state = new NotStartedState(startNode, callback);
	}
	
	
	public boolean isWaiting()
	{
		return state.getState() == State.StateEnum.WAITING;
	}
	
	
	public boolean isRunning()
	{
		return state.getState() == State.StateEnum.EXECUTING;
	}
	
	
	public boolean isDone()
	{
		return state.getState() == State.StateEnum.COMPLETED;
	}


	public boolean isTaskComplete()
	{
		return state.getState() == State.StateEnum.TASK_COMPLETE;
	}
	
	
	public Lane getCurrentLane()
	{
		return workflow.getNodeLane(currentNode.getId());
	}
	
	
	public void advance()
	{
		state.advance(this);
	}
}
