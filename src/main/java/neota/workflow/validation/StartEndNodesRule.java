package neota.workflow.validation;

import java.util.Map;

import neota.workflow.elements.Workflow;
import neota.workflow.elements.nodes.Node;


public class StartEndNodesRule extends WorkflowRule
{
	public StartEndNodesRule(Workflow workflow)
	{
		super(workflow);
	}


	@Override
	public boolean validate()
	{
		Workflow w = getWorkflow();
		Map<String, Node> nodes = w.getNodes();
		
		boolean startFound = false;
		boolean endFound = false;
		
		startFound = nodes.values().stream()
				.filter(node -> node.getType() == Node.Type.START)
				.count() == 1;
				
		endFound = nodes.values().stream()
				.filter(node -> node.getType() == Node.Type.END)
				.count() == 1;
		
		return startFound && endFound;
	}


	@Override
	public String getDescription()
	{
		return "There is not start and/or end node present with ID " + getWorkflow().getId();
	}
}
