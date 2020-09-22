package neota.workflow.elements;

import neota.workflow.elements.nodes.Node;


public class NotStartedState extends State
{
	public NotStartedState(Node startNode, SessionCallback callback)
	{
		super(startNode, callback);
		state = StateEnum.NOT_STARTED;
	}

	
	public void advance(Session session)
	{
		session.setState(new ExecutingState(session, node, callback));
	}
}
