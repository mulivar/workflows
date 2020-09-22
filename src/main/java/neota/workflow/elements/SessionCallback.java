package neota.workflow.elements;


public interface SessionCallback
{
	void onTaskRunning(Session session);
	void onTaskComplete(Session session);
	void onSessionComplete(Session session);
	void onSessionWaiting(Session session);
}
