package neota.workflow.elements;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import neota.workflow.elements.nodes.Node;
import neota.workflow.json.LaneData;


/**
 * Describes and handles one single lane.
 */
@Data
public class Lane
{
	/** Lane ID. */
	private String id;
	
	/** Lane name. */
	private String name;
	
	/** All the nodes that belong to this lane. */
	private Map<String, Node> nodes = new HashMap<>();
	
	/** The start node, if it belong to this lane. */
	private Node startNode;
	
	/** The end node, if it belongs to this lane. */
	private Node endNode;
	
	
	public Lane(String id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	
	/**
	 * Adds a node to the lane.
	 * @param node the node to add
	 */
	public void addNode(Node node)
	{
		nodes.put(node.getId(), node);
		
		if (node.getType() == Node.Type.START)
		{
			startNode = node;
		}
		else if (node.getType() == Node.Type.END)
		{
			endNode = node;
		}
	}
	
	
	/**
	 * Verifies if this lane contains the start node.
	 * @return true if contains the start node, otherwise false
	 */
	public boolean isStartLane()
	{
		return startNode != null;
	}
	

	/**
	 * Verifies if this lane contains the end node.
	 * @return true if contains the end node, otherwise false
	 */
	public boolean isEndLane()
	{
		return endNode != null;
	}

	
	/**
	 * Makes a Lane based on the provided loaded JSON data.
	 * @param data the JSON loaded into a DTO
	 * @param allNodes all nodes that belong to the related workflow, the lane will extract the nodes that belong to it
	 * @return the created lane
	 */
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
