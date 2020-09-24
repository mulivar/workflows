package neota.workflow.validation;

import neota.workflow.elements.Workflow;
import neota.workflow.elements.nodes.Node;


/**
 * Validates that the loaded workflow has no cycles.
 */
public class CycleRule extends WorkflowRule
{
	public CycleRule(Workflow workflow)
	{
		super(workflow);
	}


	@Override
	public boolean validate()
	{
		Workflow w = getWorkflow();
		
		Node startNode = w.getStartLane().getStartNode();
		return !detectCycle(startNode);
	}


	@Override
	public String getDescription()
	{
		return "A cycle is detected in the workflow with ID " + getWorkflow().getId();
	}
	
	
	private boolean detectCycle(Node node)
	{
	    node.setBeingVisited(true);
	 
	    Node neighbour = node.getOutgoingNode();
	    if (neighbour != null)
	    {
	        if (neighbour.isBeingVisited())
	        {
	            return true;
	        }
	        else if (!neighbour.isVisited() && detectCycle(neighbour))
	        {
	            return true;
	        }
	    }
	 
	    node.setBeingVisited(false);
	    node.setVisited(true);
	    
	    return false;
	}
}
