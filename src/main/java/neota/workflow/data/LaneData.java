package neota.workflow.data;

import java.util.Set;

import lombok.Data;


/**
 * Describes the Lane JSON element.
 * @author iackar
 */
@Data
public class LaneData
{
	private String id;
	private String name;
	private Set<String> nodes;
}
