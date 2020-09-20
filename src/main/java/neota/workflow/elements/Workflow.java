package neota.workflow.elements;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import neota.workflow.data.SourceData;
import neota.workflow.data.WorkflowData;
import neota.workflow.elements.nodes.Node;


@Data
@NoArgsConstructor
public class Workflow
{
	private Map<String, Lane> lanes = new HashMap<>();
	private Map<String, Node> nodes = new HashMap<>();
	
	private String startLane;
	private String endLane;
	
	
	private void addLane(Lane lane)
	{
		lanes.put(lane.getId(), lane);
		
		if (lane.isStartLane())
		{
			startLane = lane.getId();
		}
		else if (lane.isEndLane())
		{
			endLane = lane.getId();
		}
	}
	
	
	private void addNode(Node node)
	{
		nodes.put(node.getId(), node);
	}
	
	
	public static Workflow from(WorkflowData data)
	{
		SourceData source = data.getSource();

		Workflow workflow = new Workflow();
		
		// start by reading the nodes
		source.getNodes().forEach((id, node) -> {
			workflow.addNode(Node.from(node));
		});
		
		// reconnect the nodes by using the provided link mappings
		source.getLinks().forEach((id, link) -> {
			Node.linkNodes(workflow.getNodes(), link.getFrom(), link.getTo());
		});
		
		// read the lanes then, and feed them the linked node data
		source.getLanes().forEach((id, lane) -> {
			workflow.addLane(Lane.from(lane, workflow.getNodes()));
		});
		
		return workflow;
	}
	
	
	public Lane getNodesLane(String nodeId)
	{
		Lane lane = null;
		
		for (Lane l : lanes.values())
		{
			if (l.containsNode(nodeId))
			{
				lane = l;
				break;
			}
		}
		
		return lane;
	}
	
	
	public Lane getLane(String id)
	{
		return lanes.get(id);
	}


	public Node getNode(String id)
	{
		return nodes.get(id);
	}
}
