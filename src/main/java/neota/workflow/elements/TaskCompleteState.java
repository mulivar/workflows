package neota.workflow.elements;

import neota.workflow.elements.nodes.Node;


public class TaskCompleteState extends State
{
	public TaskCompleteState(Session session, Node node, SessionCallback callback)
	{
		super(node, callback);
		state = StateEnum.TASK_COMPLETE;
		
		callback.onTaskComplete(session);
	}

	
	public void advance(Session session)
	{
		if (node.getType() == Node.Type.END)
		{
			session.setState(new CompletedState(session, node, callback));
		}
		else
		{
			Workflow workflow = session.getWorkflow();
			Node nextNode = workflow.getNode(node.getOutgoingNodeId());
			
			final Lane currentLane = workflow.getNodeLane(node.getId());
			final Lane nextNodeLane = workflow.getNodeLane(nextNode.getId());
			
			// if the lanes differ enter the WAITING state
			if (!nextNodeLane.getId().equals(currentLane.getId()))
			{
				session.setState(new WaitingState(session, node, callback));
			}
			else
			{
				session.setState(new ExecutingState(session, node, callback));
			}
		}
	}
}
