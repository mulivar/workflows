package neota.workflow.elements.nodes;

import java.text.MessageFormat;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import neota.workflow.data.NodeData;


@Data
public abstract class Node
{
	private String id;
	private NodeType type;
	private String name;
	
	private String incoming;
	private String outgoing;
	
	
	public Node(String id, NodeType type, String name)
	{
		this.id = id;
		this.type = type;
		this.name = name;
	}
	
	
	public static Node from(NodeData data)
	{
		Node node = null;
		
		switch(data.getType().toLowerCase())
		{
		case "startnode":
			node = new StartNode(data.getId(), data.getName());
			break;
		case "endnode":
			node = new EndNode(data.getId(), data.getName());
			break;
		case "tasknode":
			node = new TaskNode(data.getId(), data.getName());
			break;
		case "nop":
			node = new NopNode(data.getId(), data.getName());
			break;
		default:
			throw new RuntimeException(MessageFormat.format("Unrecognised node of type {0} with ID {1}, unable to process",
					data.getType(), data.getId()));
		}
		
		return node;
	}
	
	
	public static void linkNodes(Map<String, Node> nodes, String from, String to)
	{
		// first verify the nodes
		if (!nodes.containsKey(from))
		{
			throw new RuntimeException(MessageFormat.format("Unable to find the node with ID {0} among the available nodes",
					from));
		}
		
		if (!nodes.containsKey(to))
		{
			throw new RuntimeException(MessageFormat.format("Unable to find the node with ID {0} among the available nodes",
					to));
		}
		
		nodes.get(from).setOutgoing(to);
		nodes.get(to).setIncoming(from);
	}
	
	
	public abstract void execute();
}
