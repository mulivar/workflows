package neota.workflow.server;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import neota.workflow.elements.Session;
import neota.workflow.elements.Workflow;
import neota.workflow.exceptions.ValidationException;
import neota.workflow.json.WorkflowData;
import neota.workflow.validation.CycleRule;
import neota.workflow.validation.SingleLinkRule;
import neota.workflow.validation.StartEndNodesRule;
import neota.workflow.validation.ValidationRule;
import neota.workflow.validation.Validator;


/**
 * Handles all the workflows within the system.
 */
public class WorkflowHandler
{
	private Logger log = LoggerFactory.getLogger(WorkflowHandler.class);
	
	/** A map of all loaded workflows, mapped by the workflow ID. */
	@Getter
	private Map<String, Workflow> workflows = new HashMap<>();
	
	/** The validator to use for various validations. */
	private Validator validator = new Validator();
	
	/** Handles the runtime of a particular session, i.e. instantiated workflow. */
	@Getter
	private SessionExecutor executor;
	
	
	public WorkflowHandler(SessionExecutor executor)
	{
		this.executor = executor;
	}
	
	
	public void shutdown()
	{
		executor.shutdown();
	}
	
	
	/**
	 * Loads a new workflow from an external JSON file.
	 * @param path the path to the JSON file to load
	 * @return the ID of the newly instantiated workflow
	 * @throws JsonParseException in case there's an error related to JSON parsing
	 * @throws JsonMappingException in case there's an error related to JSON mapping to internal POJOs
	 * @throws IOException in case there's an error related to reading the JSON file
	 * @throws ValidationException in case there's an error related to the workflow breaking the mandatory rules
	 */
	public synchronized String loadFromJson(String path)
			throws JsonParseException, JsonMappingException, IOException, ValidationException
	{
		log.debug("About to load a JSON file from path " + path);
		
		// load the entire file, and only then process it
	    ObjectMapper mapper = new ObjectMapper();
	    WorkflowData workflowData = mapper.readValue(Paths.get(path).toFile(), WorkflowData.class);
	    
	    // create a workflow out of the workflow data
	    Workflow workflow = Workflow.from(workflowData, path);
	    
	    // validate the workflow
	    List<ValidationRule> rules = new ArrayList<>();
	    rules.add(new StartEndNodesRule(workflow));
	    rules.add(new SingleLinkRule(workflow));
	    rules.add(new CycleRule(workflow));
	    validator.validate(rules);
	    
	    String workflowId = workflow.getId();
	    log.debug("Successfully instantiated a workflow with ID " + workflowId);
	    log.trace("Workflow contents:\n" + workflow.toString());
	    
	    if (workflows.containsKey(workflowId))
	    {
	    	throw new RuntimeException(MessageFormat.format(
	    			"Attempting to load an existing workflow is not allowed, the workflow already exists under ID {0}",
	    			workflowId));
	    }
	    else
	    {
	    	workflows.put(workflowId, workflow);
	    	log.debug("Successfully stored workflow: " + workflow.toString());
	    }
		
		return workflowId;
	}
	

	public synchronized String createSession(String workflowId)
	{
		log.debug("Creating a session for workflow ID " + workflowId);
		return executor.submit(workflows.get(workflowId));
	}
	
	
	public synchronized Session getSession(String sessionId)
	{
		log.debug("Retrieving a session with session ID " + sessionId);
		return executor.getSession(sessionId);
	}


	public synchronized void resumeSession(String sessionId) throws ValidationException
	{
		log.debug("Attempting to continue a session with session ID " + sessionId);
		executor.resumeSession(sessionId);
	}


	public synchronized void setTimeout(int timeout)
	{
		log.debug("Setting the timeout to " + timeout);
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
