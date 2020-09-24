package neota.workflow.elements.states;

import lombok.Getter;
import lombok.Setter;
import neota.workflow.elements.Session;


/**
 * The base class of the finite state machine's state. All inheriting classes know how to switch from one state
 * to another, thus participating in the session runtime.
 */
@Getter
@Setter
public abstract class State
{
	/** Enumerated session states, for easier handling. */
	public static enum StateEnum
	{
		NOT_STARTED,
		EXECUTING,
		TASK_COMPLETE,
		WAITING,
		COMPLETED
	}

	/** The current session state. */
	protected StateEnum state;
	
	/** The callback to invoke upon state change. */
	protected SessionCallback callback;
	
	
	protected State(SessionCallback callback)
	{
		this.callback = callback;
	}
	
	
	public StateEnum getState()
	{
		return state;
	}
	
	
	/**
	 * Move to the next state of this session.
	 * @param session the session to advance into the next state
	 */
	public abstract void advance(Session session);
}
