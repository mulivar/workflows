package neota.workflow.elements;

import neota.workflow.elements.nodes.Node;


public class WaitingState extends State
{
	public WaitingState(Session session, Node node, SessionCallback callback)
	{
		super(node, callback);
		node = node.getOutgoingNodeId();
		state = StateEnum.WAITING;
		
		callback.onSessionWaiting(session);
	}

	
	public void advance(Session session)
	{
		session.setState(new ExecutingState(session, node, callback));
	}
}
