package neota.workflow.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import junit.framework.TestCase;
import neota.workflow.elements.Lane;
import neota.workflow.elements.Session;
import neota.workflow.elements.Workflow;
import neota.workflow.elements.mock.SessionExecutorMock;
import neota.workflow.elements.nodes.Node;
import neota.workflow.elements.states.State.StateEnum;
import neota.workflow.exceptions.ValidationException;


public class WorkflowHandlerTest extends TestCase
{
	WorkflowHandler handler;


	protected void setUp() throws Exception
	{
		super.setUp();
		
		SessionExecutor executor = new SessionExecutor();
		handler = new WorkflowHandler(executor);
	}


	protected void tearDown() throws Exception
	{
		handler.shutdown();
		super.tearDown();
	}
	
	
    @Test
    public void testLoadFromJsonIoException() throws JsonParseException, JsonMappingException, IOException,
    		ValidationException, java.io.FileNotFoundException
    {
        try
        {
			final String path = "src/test/resources/asdf.json";
			final String workflowId = handler.loadFromJson(path);
			fail("Expected an exception to occur");
        }
        catch (FileNotFoundException e)
        {
        	assertEquals("src/test/resources/asdf.json (No such file or directory)", e.getMessage());
        }
    }
	
	
    @Test
    public void testLoadFromJsonEndRule() throws JsonParseException, JsonMappingException, IOException
    {
        try
        {
			final String path = "src/test/resources/workflow_end.json";
			final String workflowId = handler.loadFromJson(path);
			fail("Expected an exception to occur");
        }
        catch (ValidationException e)
        {
        	assertTrue(e.getMessage().startsWith("A validation exception occurred; message = Failed to validate the following rule: "
        		+ "There is not start and/or end node present"));
        }
    }
	
	
    @Test
    public void testLoadFromJsonEnd2Rule() throws JsonParseException, JsonMappingException, IOException
    {
        try
        {
			final String path = "src/test/resources/workflow_end2.json";
			final String workflowId = handler.loadFromJson(path);
			fail("Expected an exception to occur");
        }
        catch (ValidationException e)
        {
        	assertTrue(e.getMessage().startsWith("A validation exception occurred; message = Failed to validate the following rule: "
            		+ "There is not start and/or end node present"));
        }
    }
	
	
    @Test
    public void testLoadFromJsonCycleRule() throws JsonParseException, JsonMappingException, IOException
    {
        try
        {
			final String path = "src/test/resources/workflow_cycle.json";
			final String workflowId = handler.loadFromJson(path);
			fail("Expected an exception to occur");
        }
        catch (ValidationException e)
        {
        	assertTrue(e.getMessage().contains("A cycle is detected in the workflow"));
        }
    }


    @Test
	public void testLoadFromJson() throws JsonParseException, JsonMappingException, IOException, ValidationException
	{
		final String path = "src/test/resources/workflow.json";
		final String workflowId = handler.loadFromJson(path);
		
		assertNotNull(workflowId);
		assertFalse(workflowId.isEmpty());
		
		Map<String, Workflow> workflows = handler.getWorkflows();
		assertEquals(1, workflows.size());
		
		Workflow w = workflows.get(workflowId);
		assertNotNull(w);
	}


    @Test
	public void testLoadLanes() throws JsonParseException, JsonMappingException, IOException, ValidationException
	{
		final String path = "src/test/resources/workflow.json";
		final String workflowId = handler.loadFromJson(path);
		
		Map<String, Workflow> workflows = handler.getWorkflows();
		
		Workflow w = workflows.get(workflowId);
		
		List<String> laneIds = Arrays.asList(
				"96c83fad-bd58-498b-a061-42ce5bc17b15",
				"2e0a3ce2-5dd3-4cec-97c8-37d358bc80d8",
				"52d065c6-749b-45f7-9d31-a37c78a4b092"
		);
		
		Map<String, Lane> lanes = w.getLanes();
		assertEquals(laneIds.size(), lanes.size());
		
		for (String laneId : laneIds)
		{
			assertTrue(lanes.containsKey(laneId));
		}
		
		assertEquals("96c83fad-bd58-498b-a061-42ce5bc17b15", w.getStartLane().getId());
		assertEquals("52d065c6-749b-45f7-9d31-a37c78a4b092", w.getEndLane().getId());
	}


    @Test
	public void testLoadNodes() throws JsonParseException, JsonMappingException, IOException, ValidationException
	{
		final String path = "src/test/resources/workflow.json";
		final String workflowId = handler.loadFromJson(path);
		
		Map<String, Workflow> workflows = handler.getWorkflows();
		
		Workflow w = workflows.get(workflowId);

		List<String> nodeIds = Arrays.asList(
				"9175d33b-957e-440f-85a6-92d290fc3d2d",
				"a70931cb-63bd-444e-9bb9-c19c099dd222",
				"85f08553-ca2c-4cb2-b761-a80994118027",
				"d5e725cd-1bc9-4e49-bceb-b7dc56d646b2",
				"3cbc8581-07a8-4207-adfd-3ac955f41b70",
				"68590ae3-ea1d-4a35-9806-97880a136a48",
				"f9b28088-4953-40b4-aed1-b73962d67bfb"
		);
		
		Map<String, Node> nodes = w.getNodes();
		assertEquals(nodeIds.size(), nodes.size());
		
		for (String nodeId : nodeIds)
		{
			assertTrue(nodes.containsKey(nodeId));
		}
		
		assertEquals("9175d33b-957e-440f-85a6-92d290fc3d2d", w.getStartLane().getStartNode().getId());
		assertEquals("85f08553-ca2c-4cb2-b761-a80994118027", w.getEndLane().getEndNode().getId());
	}
}
