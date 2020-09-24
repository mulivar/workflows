package neota.workflow.elements;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import junit.framework.TestCase;
import neota.workflow.elements.mock.SessionExecutorMock;
import neota.workflow.elements.states.CompletedState;
import neota.workflow.elements.states.ExecutingState;
import neota.workflow.elements.states.NotStartedState;
import neota.workflow.elements.states.WaitingState;
import neota.workflow.elements.states.State.StateEnum;
import neota.workflow.exceptions.ValidationException;
import neota.workflow.server.SessionExecutor;
import neota.workflow.server.WorkflowHandler;


public class SessionTest extends TestCase
{
	WorkflowHandler handler;


	protected void setUp() throws Exception
	{
		super.setUp();
		
		SessionExecutor executor = new SessionExecutorMock();
		handler = new WorkflowHandler(executor);
	}


	protected void tearDown() throws Exception
	{
		handler.shutdown();
		super.tearDown();
	}

	
    @Test
	public void testAdvance() throws JsonParseException, JsonMappingException, IOException, ValidationException
	{
		final String path = "src/test/resources/workflow.json";
		
		SessionExecutor mock = new SessionExecutorMock();
		WorkflowHandler wh = new WorkflowHandler(mock);
		final String workflowId = wh.loadFromJson(path);
		
		final String sessionId = wh.createSession(workflowId);
		Session session = wh.getSession(sessionId);
		
		assertNotNull(sessionId);
		assertNotNull(session);
		
		assertTrue(session.getState() instanceof NotStartedState);
		
		session.advance();
		assertTrue(session.getState() instanceof ExecutingState);
		
		session.advance();
		assertTrue(session.getState() instanceof ExecutingState);
		
		session.advance();
		assertTrue(session.getState() instanceof WaitingState);
		
		session.advance();
		assertTrue(session.getState() instanceof ExecutingState);
		
		session.advance();
		assertTrue(session.getState() instanceof WaitingState);
		
		session.advance();
		assertTrue(session.getState() instanceof ExecutingState);
		
		session.advance();
		assertTrue(session.getState() instanceof ExecutingState);
		
		session.advance();
		assertTrue(session.getState() instanceof ExecutingState);
		
		session.advance();
		assertTrue(session.getState() instanceof CompletedState);
		
		try
		{
			session.advance();
			fail("Excpected an exception to occur");
		}
		catch (RuntimeException e)
		{
			assertEquals("Unable to progress this session, the session's execution has completed", e.getMessage());
		}
	}
}
