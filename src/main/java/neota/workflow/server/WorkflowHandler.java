package neota.workflow.server;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import neota.workflow.data.WorkflowData;
import neota.workflow.elements.Session;
import neota.workflow.elements.Workflow;


/**
 * @author iackar
 */
public class WorkflowHandler
{
	@Getter
	private Map<String, Workflow> workflows = new HashMap<>();
	
	private WorkflowChecker checker = new WorkflowChecker();
	
	@Getter
	private SessionExecutor executor = new SessionExecutor();
	
	
	public void shutdown()
	{
		executor.shutdown();
	}
	
	
	public synchronized String loadFromJson(String path) throws JsonParseException, JsonMappingException, IOException
	{
		// load the entire file, and only then process it
	    ObjectMapper mapper = new ObjectMapper();
	    WorkflowData workflowData = mapper.readValue(Paths.get(path).toFile(), WorkflowData.class);
	    
	    // create a workflow out of the workflow data
	    Workflow workflow = Workflow.from(workflowData);
	    
	    // validate the workflow
	    // TODO
	    checker.validate(workflow);
	    
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
		final String sessionId = executor.submit(workflows.get(workflowId));
		
		return sessionId;
	}
	
	
	public synchronized Session getSession(String sessionId)
	{
		return executor.getSession(sessionId);
	}


	public synchronized void resumeSession(String sessionId)
	{
		executor.resumeSession(sessionId);
	}


	public synchronized void setTimeout(int timeout)
	{
		executor.setTimeout(timeout);
	}
	
	
	public void registerTaskObserver(SessionObserver observer)
	{
		executor.registerTaskObserver(observer);
	}
	
	
	public void registerSessionObserver(SessionObserver observer)
	{
		executor.registerSessionObserver(observer);
	}
}
