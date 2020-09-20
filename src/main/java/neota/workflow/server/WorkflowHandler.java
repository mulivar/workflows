package neota.workflow.server;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import neota.workflow.data.WorkflowData;
import neota.workflow.elements.Session;
import neota.workflow.elements.Workflow;


/**
 * @author leto
 */
public class WorkflowHandler
{
	private Map<Integer, Workflow> workflows = new HashMap<>();
	private Map<String, Session> sessions = new HashMap<>();
	private SessionExecutor executor = new SessionExecutor();
	
	
	public synchronized int loadFromJson(String path) throws JsonParseException, JsonMappingException, IOException
	{
		// load the entire file, and only then process it
	    ObjectMapper mapper = new ObjectMapper();
	    WorkflowData workflowData = mapper.readValue(Paths.get(path).toFile(), WorkflowData.class);
	    
	    // create a workflow out of the workflow data
	    Workflow workflow = Workflow.from(workflowData);
	    
	    int hash = workflow.hashCode();
	    if (workflows.containsKey(hash))
	    {
	    	throw new RuntimeException(MessageFormat.format(
	    			"Attempting to load an existing workflow is not allowed, the workflow already exists under ID {0}",
	    			hash));
	    }
	    else
	    {
	    	workflows.put(hash, workflow);
	    	System.out.println("Successfully stored workflow: " + workflow.toString());
	    }
		
		return hash;
	}
	

	public synchronized String createSession(int workflowId)
	{
		final String sessionId = UUID.randomUUID().toString();
		Session session = new Session(sessionId, workflowId, workflows.get(workflowId));
		
		executor.submit(session);
		
		return sessionId;
	}
	
	
	public synchronized Session getSession(String sessionId)
	{
		return null;
	}


	public synchronized Session resumeSession(String sessionId)
	{
		return null;
	}


	public synchronized void setTimeout(int timeout)
	{
	}
}
