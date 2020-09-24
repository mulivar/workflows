package neota.workflow.elements;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import lombok.Data;
import lombok.NoArgsConstructor;
import neota.workflow.elements.nodes.Node;
import neota.workflow.json.SourceData;
import neota.workflow.json.WorkflowData;


@Data
@NoArgsConstructor
public class Workflow
{
	private String id;
	
	private Map<String, Lane> lanes = new HashMap<>();
	private Map<String, Node> nodes = new HashMap<>();
	
	private Lane startLane;
	private Lane endLane;
	
	
	public static Workflow from(WorkflowData data, String path)
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
		
		// trivial solution, should be better
		//workflow.id = UUID.fromString(workflow.getStartLane().getId()).toString();
		String value = "";
		for (String nodeId : workflow.getNodes().keySet())
		{
			value += nodeId;
		}
		
	    workflow.id = DigestUtils.md5Hex(value);
		
		return workflow;
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
	
	
	private void addLane(Lane lane)
	{
		lanes.put(lane.getId(), lane);
		
		if (lane.isStartLane())
		{
			startLane = lane;
		}
		else if (lane.isEndLane())
		{
			endLane = lane;
		}
	}
	
	
	private void addNode(Node node)
	{
		nodes.put(node.getId(), node);
	}
}
