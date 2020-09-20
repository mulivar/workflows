package neota.workflow.elements;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import neota.workflow.data.LaneData;
import neota.workflow.elements.nodes.Node;
import neota.workflow.elements.nodes.NodeType;


/**
 * Describes and handles one single lane.
 * @author iackar
 */
@Data
public class Lane
{
	private String id;
	private String name;
	
	private Map<String, Node> nodes = new HashMap<>();
	private String startNode;
	private String endNode;
	
	
	public Lane(String id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	
	public void addNode(Node node)
	{
		nodes.put(node.getId(), node);
		
		if (node.getType() == NodeType.START)
		{
			startNode = node.getId();
		}
		else if (node.getType() == NodeType.END)
		{
			endNode = node.getId();
		}
	}
	
	
	public boolean isStartLane()
	{
		return startNode != null;
	}
	
	
	public boolean isEndLane()
	{
		return endNode != null;
	}

	
	public static Lane from(LaneData data, Map<String, Node> allNodes)
	{
		Lane lane = new Lane(data.getId(), data.getName());
		
		data.getNodes().forEach(nodeId -> {
			Node node = allNodes.get(nodeId);
			if (node == null) {
				throw new RuntimeException(MessageFormat.format(
						"Unable to find the node with ID {0} in the pool of created nodes", nodeId));
			}
			
			lane.addNode(node);
		});
		
		return lane;
	}


	public boolean containsNode(String nodeId)
	{
		return nodes.containsKey(nodeId);
	}
}
