package neota.workflow.server;

import neota.workflow.elements.Session;
import neota.workflow.elements.nodes.Node;


public interface SessionObserver
{
	void onTaskComplete(Node node);
	void onSessionComplete(Session session);
}
