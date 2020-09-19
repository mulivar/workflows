package neota.workflow.data;

import java.util.Map;

import lombok.Data;


/**
 * Describes the Source JSON element.
 * @author iackar
 */
@Data
public class SourceData
{
	private Map<String, NodeData> nodes;
	private Map<String, LaneData> lanes;
	private Map<String, LinkData> links;
}
