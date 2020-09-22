package neota.workflow.elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
	
	private String startLaneId;
	private String endLaneId;
	
	
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
	
	
	public String getId()
	{
		// trivial handling of the UUID calculation
		return UUID.fromString(lanes.get(getStartLaneId()).getId()).toString();
	}
	
	
	public Node getStartNode()
	{
		List<Node> startNode = nodes.values().stream()
				.filter(node -> node.getType() == Node.Type.START)
				.collect(Collectors.toList());
		
		return startNode.get(0);
	}
	
	
	public Lane getNodeLane(String nodeId)
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
	
	
	private void addLane(Lane lane)
	{
		lanes.put(lane.getId(), lane);
		
		if (lane.isStartLane())
		{
			startLaneId = lane.getId();
		}
		else if (lane.isEndLane())
		{
			endLaneId = lane.getId();
		}
	}
	
	
	private void addNode(Node node)
	{
		nodes.put(node.getId(), node);
	}
}
