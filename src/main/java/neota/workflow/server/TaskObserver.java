package neota.workflow.server;

import neota.workflow.elements.nodes.Node;


public interface TaskObserver
{
	void onTaskComplete(Node node);
}
