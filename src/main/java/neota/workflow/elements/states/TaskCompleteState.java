package neota.workflow.elements.states;

import neota.workflow.elements.Lane;
import neota.workflow.elements.Session;
import neota.workflow.elements.Workflow;
import neota.workflow.elements.nodes.Node;


public class TaskCompleteState extends State
{
	public TaskCompleteState(Session session, SessionCallback callback)
	{
		super(callback);
		state = StateEnum.TASK_COMPLETE;
		
		callback.onTaskComplete(session);
	}

	
	public void advance(Session session)
	{
		Node node = session.getCurrentNode();
		
		if (node.getType() == Node.Type.END)
		{
			session.setState(new CompletedState(session, callback));
		}
		else
		{
			Workflow workflow = session.getWorkflow();
			Node nextNode = node.getOutgoingNode();
			
			final Lane currentLane = workflow.getNodeLane(node.getId());
			final Lane nextNodeLane = workflow.getNodeLane(nextNode.getId());
			
			// if the lanes differ enter the WAITING state
			if (!nextNodeLane.getId().equals(currentLane.getId()))
			{
				session.setState(new WaitingState(session, callback));
			}
			else
			{
				session.setState(new ExecutingState(session, callback, false));
			}
		}
	}
}
