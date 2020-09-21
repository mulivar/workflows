package neota.workflow.elements;

import lombok.Getter;
import neota.workflow.elements.nodes.Node;


public class Session
{
	public static enum State
	{
		NOT_STARTED,
		EXECUTING,
		WAITING,
		COMPLETED
	}
	
	@Getter
	private String id;
	
	@Getter
	private Workflow workflow;
	
	@Getter
	private State state = State.NOT_STARTED;
	
	@Getter
	private Node currentNode;
	
	
	public Session(String id, Workflow workflow)
	{
		this.id = id;
		this.workflow = workflow;
		
		// initialise the current node to the starting node
		final Lane startLane = workflow.getLane(workflow.getStartLane());
		final Node startNode = workflow.getNode(startLane.getStartNode());
		
		this.currentNode = startNode;
	}
	
	
	public boolean isWaiting()
	{
		return state == State.WAITING;
	}
	
	
	public boolean isRunning()
	{
		return state == State.EXECUTING;
	}
	
	
	public boolean isDone()
	{
		return state == State.COMPLETED;
	}
	
	
	public Lane getCurrentLane()
	{
		return workflow.getNodeLane(currentNode.getId());
	}
	
	
	public void advance()
	{
		switch (state)
		{
		case NOT_STARTED:
			handleFromNotStarted();
			break;
		case EXECUTING:
			handleFromExecuting();
			break;
		case WAITING:
			handleFromWaiting();
			break;
		case COMPLETED:
			throw new RuntimeException("Unable to progress this session, the session's execution has completed");
		}
	}
	
	
	private void handleFromNotStarted()
	{
		state = State.EXECUTING;
		currentNode = workflow.getNode(currentNode.getOutgoing());
	}
	
	
	private void handleFromExecuting()
	{
		if (currentNode.getType() != Node.Type.END)
		{
			Node nextNode = workflow.getNode(currentNode.getOutgoing());
			
			final Lane currentLane = workflow.getNodeLane(currentNode.getId());
			final Lane nextNodeLane = workflow.getNodeLane(nextNode.getId());
			
			currentNode = nextNode;
			
			// if the lanes differ enter the WAITING state
			if (!nextNodeLane.getId().equals(currentLane.getId()))
			{
				state = State.WAITING;
			}
		}
		else
		{
			state = State.COMPLETED;
		}
	}
	
	
	private void handleFromWaiting()
	{
		state = State.EXECUTING;
	}
}
