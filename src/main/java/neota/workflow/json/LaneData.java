package neota.workflow.json;

import java.util.Set;

import lombok.Data;


/**
 * Describes the Lane JSON element.
 */
@Data
public class LaneData
{
	private String id;
	private String name;
	private Set<String> nodes;
}
