package neota.workflow.elements.states;

import neota.workflow.elements.Session;

/**
 * Provides a contract for various session execution related events that are needed for the state machine to function
 * properly (i.e. to execute any action on the changed states).
 */
public interface SessionCallback
{
	/**
	 * Executed when a particular task starts executing.
	 * @param session the session that owns the started task
	 */
	void onTaskRunning(Session session);

	/**
	 * Executed when a particular task is completed.
	 * @param session the session that owns the completed task
	 */
	void onTaskComplete(Session session);
	
	/**
	 * Executed when the entire session completes.
	 * @param session the session that completed its runtime
	 */
	void onSessionComplete(Session session);

	/**
	 * Executed when a session is put into the waiting state
	 * @param session the session that is waiting for external input
	 */
	void onSessionWaiting(Session session);
}
