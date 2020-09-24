package neota.workflow.elements.states;

import neota.workflow.elements.Session;


public class NotStartedState extends State
{
	public NotStartedState(SessionCallback callback)
	{
		super(callback);
		state = StateEnum.NOT_STARTED;
	}

	
	public void advance(Session session)
	{
		session.setState(new ExecutingState(session, callback, false));
	}
}
