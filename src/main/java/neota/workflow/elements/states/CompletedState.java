package neota.workflow.elements.states;

import neota.workflow.elements.Session;


public class CompletedState extends State
{
	public CompletedState(Session session, SessionCallback callback)
	{
		super(callback);
		state = StateEnum.COMPLETED;
		
		callback.onSessionComplete(session);
	}

	
	public void advance(Session session)
	{
		throw new RuntimeException("Unable to progress this session, the session's execution has completed");
	}
}
