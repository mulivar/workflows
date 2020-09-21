package neota.workflow.server;

import neota.workflow.elements.Session;


public interface SessionObserver
{
	void onSessionComplete(Session session);
}
