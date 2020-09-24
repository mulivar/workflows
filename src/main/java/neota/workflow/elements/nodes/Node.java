package neota.workflow.elements.nodes;

import java.text.MessageFormat;
import java.util.Map;

import lombok.Data;
import neota.workflow.json.NodeData;


@Data
public abstract class Node
{
	/** The default timeout in seconds */
	// TODO change this to 60
	public static int DEFAULT_TIMEOUT	= 10;
	
	public static enum Type
	{
		START,
		END,
		TASK,
		NOP
	}
	
	protected String id;
	protected Node.Type type;
	protected String name;
	
	protected int timeout = DEFAULT_TIMEOUT;

	// for branching this would have to be a list in order to support a complex graph
	protected Node outgoingNode;
	
	protected boolean visited = false;
	protected boolean beingVisited = false;
	
	
	public Node(String id, Node.Type type, String name)
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
		
		nodes.get(from).setOutgoingNode(nodes.get(to));
	}
	
	
	public void execute(NodeCallback callback)
	{
		runNodeTask();
		callback.onTaskComplete();
	}
	
	
	/**
	 * The node's actual task to perform.
	 */
	public abstract void runNodeTask();
}
