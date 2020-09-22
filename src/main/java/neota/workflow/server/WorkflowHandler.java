package neota.workflow.server;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import neota.workflow.data.WorkflowData;
import neota.workflow.elements.Session;
import neota.workflow.elements.Workflow;
import neota.workflow.exceptions.ValidationException;
import neota.workflow.validation.CycleRule;
import neota.workflow.validation.SingleLinkRule;
import neota.workflow.validation.StartEndNodesRule;
import neota.workflow.validation.ValidationRule;
import neota.workflow.validation.Validator;


/**
 * @author iackar
 */
public class WorkflowHandler
{
	@Getter
	private Map<String, Workflow> workflows = new HashMap<>();
	
	private Validator validator = new Validator();
	
	@Getter
	private SessionExecutor executor = new SessionExecutor();
	
	
	public void shutdown()
	{
		executor.shutdown();
	}
	
	
	public synchronized String loadFromJson(String path)
			throws JsonParseException, JsonMappingException, IOException, ValidationException
	{
		// load the entire file, and only then process it
	    ObjectMapper mapper = new ObjectMapper();
	    WorkflowData workflowData = mapper.readValue(Paths.get(path).toFile(), WorkflowData.class);
	    
	    // create a workflow out of the workflow data
	    Workflow workflow = Workflow.from(workflowData);
	    
	    // validate the workflow
	    List<ValidationRule> rules = new ArrayList<>();
	    rules.add(new StartEndNodesRule(workflow));
	    rules.add(new SingleLinkRule(workflow));
	    rules.add(new CycleRule(workflow));
	    validator.validate(rules);
	    
	    String workflowId = workflow.getId();
	    if (workflows.containsKey(workflowId))
	    {
	    	throw new RuntimeException(MessageFormat.format(
	    			"Attempting to load an existing workflow is not allowed, the workflow already exists under ID {0}",
	    			workflowId));
	    }
	    else
	    {
	    	workflows.put(workflowId, workflow);
	    	System.out.println("Successfully stored workflow: " + workflow.toString());
	    }
		
		return workflowId;
	}
	

	public synchronized String createSession(String workflowId)
	{
		return executor.submit(workflows.get(workflowId));
	}
	
	
	public synchronized Session getSession(String sessionId)
	{
		return executor.getSession(sessionId);
	}


	public synchronized void resumeSession(String sessionId) throws ValidationException
	{
		executor.resumeSession(sessionId);
	}


	public synchronized void setTimeout(int timeout)
	{
		executor.setTimeout(timeout);
	}
	
	
	public synchronized void registerTaskObserver(SessionObserver observer)
	{
		executor.registerTaskObserver(observer);
	}
	
	
	public synchronized void registerSessionObserver(SessionObserver observer)
	{
		executor.registerSessionObserver(observer);
	}
}
