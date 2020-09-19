package neota.workflow.elements;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import neota.workflow.data.LaneData;


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
	private Node startNode;
	private Node endNode;

	
	public static Lane from(LaneData data)
	{
		Lane lane = new Lane();
		
		lane.id = data.getId();
		lane.name = data.getName();
		
		return lane;
	}
}
