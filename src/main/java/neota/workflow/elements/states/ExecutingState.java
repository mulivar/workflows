package neota.workflow.elements.states;

import neota.workflow.elements.Session;
import neota.workflow.elements.nodes.Node;


public class ExecutingState extends State
{
	public ExecutingState(Session session, SessionCallback callback, boolean waited)
	{
		super(callback);
		state = StateEnum.EXECUTING;

		if (!waited)
		{
			Node next = session.getCurrentNode().getOutgoingNode();
			session.setCurrentNode(next);
		}
		
		callback.onTaskRunning(session);
	}

	
	public void advance(Session session)
	{
		session.setState(new TaskCompleteState(session, callback));
		
		// after task complete state we immediately resume into the next state
		session.advance();
	}
}
