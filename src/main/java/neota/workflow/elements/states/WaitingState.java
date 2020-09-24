package neota.workflow.elements.states;

import neota.workflow.elements.Session;
import neota.workflow.elements.nodes.Node;


public class WaitingState extends State
{
	public WaitingState(Session session, SessionCallback callback)
	{
		super(callback);
		state = StateEnum.WAITING;

		Node next = session.getCurrentNode().getOutgoingNode();
		session.setCurrentNode(next);
		
		callback.onSessionWaiting(session);
	}

	
	public void advance(Session session)
	{
		session.setState(new ExecutingState(session, callback, true));
	}
}
