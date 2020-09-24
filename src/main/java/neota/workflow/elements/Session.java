package neota.workflow.elements;

import lombok.Getter;
import lombok.Setter;
import neota.workflow.elements.nodes.Node;
import neota.workflow.elements.states.NotStartedState;
import neota.workflow.elements.states.SessionCallback;
import neota.workflow.elements.states.State;


/**
 * Hosts info about a particular session, i.e. workflow instance, and presents a finite state machine for all
 * the session states.
 */
public class Session
{
	/** Session ID. */
	@Getter
	private String id;
	
	/** The workflow instantiated into this session. */
	@Getter
	private Workflow workflow;
	
	/** The current state of the session. */
	@Getter @Setter
	private State state;
	
	/** The session's currently executing node, if any. */
	@Getter @Setter
	private Node currentNode;
	
	
	public Session(String id, Workflow workflow, SessionCallback callback)
	{
		this.id = id;
		this.workflow = workflow;
		
		// initialise the current node to the starting node
		final Lane startLane = workflow.getStartLane();
		final Node startNode = startLane.getStartNode();
		
		this.currentNode = startNode;
		
		state = new NotStartedState(callback);
	}
	
	
	/**
	 * Move the session into the next state.
	 */
	public void advance()
	{
		state.advance(this);
	}
	
	
	public Lane getCurrentLane()
	{
		return workflow.getNodeLane(currentNode.getId());
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
}
