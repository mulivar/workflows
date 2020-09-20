package neota.workflow.elements;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Session
{
	private String id;
	private int workflowId;
	private Workflow workflow;
	
	
	public String getLane()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
