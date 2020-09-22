package neota.workflow.elements;

import neota.workflow.elements.nodes.Node;


public class CompletedState extends State
{
	public CompletedState(Session session, Node node, SessionCallback callback)
	{
		super(node, callback);
		state = StateEnum.COMPLETED;
		
		callback.onSessionComplete(session);
	}

	
	public void advance(Session session)
	{
		throw new RuntimeException("Unable to progress this session, the session's execution has completed");
	}
}
