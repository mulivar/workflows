package neota.workflow.elements.mock;

import java.util.UUID;

import neota.workflow.elements.Session;
import neota.workflow.elements.Workflow;
import neota.workflow.server.SessionExecutor;


public class SessionExecutorMock extends SessionExecutor
{
	@Override
	public String submit(Workflow workflow)
	{
		final String sessionId = UUID.randomUUID().toString();
		Session session = new Session(sessionId, workflow, this);
		
		storeSession(session);
		
		return sessionId;
	}
}
