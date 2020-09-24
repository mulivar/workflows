package neota.workflow.server;

import neota.workflow.elements.Session;
import neota.workflow.elements.nodes.Node;


/**
 * Offers hooks that will inform the observers about session related events.
 */
public interface SessionObserver
{
	/**
	 * Executed when a task is complete.
	 * @param node the task that just completed
	 */
	void onTaskComplete(Node node);
	
	/**
	 * Executed when a session is complete.
	 * @param session the session that just completed
	 */
	void onSessionComplete(Session session);
}
