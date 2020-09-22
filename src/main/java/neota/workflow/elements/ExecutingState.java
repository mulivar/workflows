package neota.workflow.elements;

import neota.workflow.elements.nodes.Node;


public class ExecutingState extends State
{
	public ExecutingState(Session session, Node node, SessionCallback callback)
	{
		super(node, callback);
		node = node.getOutgoingNodeId();
		state = StateEnum.EXECUTING;

		callback.onTaskRunning(session);
	}

	
	public void advance(Session session)
	{
		session.setState(new TaskCompleteState(session, node, callback));
	}
}
