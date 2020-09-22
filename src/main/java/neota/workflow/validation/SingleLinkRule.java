package neota.workflow.validation;

import java.util.Map;

import neota.workflow.elements.Workflow;
import neota.workflow.elements.nodes.Node;


public class SingleLinkRule extends WorkflowRule
{
	public SingleLinkRule(Workflow workflow)
	{
		super(workflow);
	}

	
	@Override
	public boolean validate()
	{
		Workflow w = getWorkflow();
		Map<String, Node> nodes = w.getNodes();
		
		/*
		 * This is resolved implicitly by the architectural limitation imposed on the Node class, but if it were
		 * altered in a way that the neighbours are hosted in a list, then this code would make sense.
		 */
		
		boolean foundBranching = false;
		for (Node node : nodes.values())
		{
			int count = 0;
			count += node.getOutgoingNodeId() != null ? 1 : 0;
			
			if (count > 1)
			{
				foundBranching = true;
				break;
			}
		}
		
		return !foundBranching;
	}


	@Override
	public String getDescription()
	{
		return "Only nodes with a single incoming and/or outgoing link are allowed in workflow with ID " + getWorkflow().getId();
	}
}
