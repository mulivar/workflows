package neota.workflow.validation;

import java.text.MessageFormat;
import java.util.Map;

import neota.workflow.elements.Session;


public class SessionResumeRule extends SessionRule
{
	private Map<String, Session> waitingSessions;

	
	public SessionResumeRule(Session session, Map<String, Session> waitingSessions)
	{
		super(session);
		this.waitingSessions = waitingSessions;
	}


	@Override
	public boolean validate()
	{
		boolean waiting = waitingSessions.containsKey(session.getId());
		boolean complete = session.isDone();
		
		return waiting && !complete;
	}


	@Override
	public String getDescription()
	{
		return MessageFormat.format("The requested session {0} is not waiting or is complete", session.getId());
	}

}
