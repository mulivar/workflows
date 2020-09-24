package neota.workflow.server;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neota.workflow.elements.Session;
import neota.workflow.elements.Workflow;
import neota.workflow.elements.nodes.Node;
import neota.workflow.elements.nodes.NodeCallback;
import neota.workflow.elements.states.SessionCallback;
import neota.workflow.exceptions.ValidationException;
import neota.workflow.validation.SessionResumeRule;
import neota.workflow.validation.ValidationRule;
import neota.workflow.validation.Validator;


/**
 * Handles all session execution logic.
 */
public class SessionExecutor implements SessionCallback
{
	private Logger log = LoggerFactory.getLogger(SessionExecutor.class);
	
	/** The thread count used for execution of particular tasks. */
	public static final int THREAD_COUNT	= 20;
	
	/** The queue size for the bounded queue used for manoeuvring tasks. */
	public static final int QUEUE_LENGTH	= 1000;
	
	/** The executor used for executing particular tasks. */
	private ExecutorService taskExecutor = Executors.newFixedThreadPool(THREAD_COUNT);
	
	/** The executor used for polling the task queue. */
	private ExecutorService queuePoller = Executors.newSingleThreadExecutor();
	
	/** The queue that holds the tasks that are waiting for execution. */
	private BlockingQueue<Session> tasks = new ArrayBlockingQueue<>(QUEUE_LENGTH);
	
	/** Holds all currently running and completed sessions in the system. */
	private Map<String, Session> sessions = new ConcurrentHashMap<>();
	
	/** Holds only sessions that are waiting for user input. */
	private Map<String, Session> waitingSessions = new ConcurrentHashMap<>();

	/** Observers run upon task completion. */
	private List<SessionObserver> taskObservers = new ArrayList<>();
	
	/** Observers run upon session completion. */
	private List<SessionObserver> sessionObservers = new ArrayList<>();
	
	/** The validator used for various session validations. */
	private Validator validator = new Validator();
	
	/** The timeout used for nodes that support timeouts. */
	private int timeout = Node.DEFAULT_TIMEOUT;
	
	
	/**
	 * Starts the session executor, which is immediately ready for accepting new sessions.
	 */
	public SessionExecutor()
	{
		runTaskThread();
	}
	
	
	/**
	 * Stop all running threads.
	 */
	public void shutdown()
	{
		taskExecutor.shutdown();
		queuePoller.shutdown();
	}


	public Session getSession(String sessionId)
	{
		return sessions.get(sessionId);
	}
	
	
	public void registerTaskObserver(SessionObserver observer)
	{
		taskObservers.add(observer);
	}
	
	
	public void registerSessionObserver(SessionObserver observer)
	{
		sessionObservers.add(observer);
	}


	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}
	
	
	/**
	 * Creates a session from a particular workflow, and immediately starts executing it if possible.
	 * @param workflow the workflow to instantiate into a session
	 * @return session ID
	 */
	public String submit(Workflow workflow)
	{
		final String sessionId = UUID.randomUUID().toString();
		Session session = new Session(sessionId, workflow, this);
		
		storeSession(session);
		session.advance();
		
		return sessionId;
	}
	
	
	/**
	 * Continues running an already started session if possible.
	 * @param sessionId the session ID to continue
	 * @throws ValidationException if any of the rules for continuing a session is broken (session must be in the
	 * waiting state)
	 */
	public void resumeSession(String sessionId) throws ValidationException
	{
		List<ValidationRule> rules = new ArrayList<>();
		rules.add(new SessionResumeRule(sessions.get(sessionId), waitingSessions));
		
		validator.validate(rules);
		
		log.debug(MessageFormat.format("The session with ID {0} is continued from the waiting state", sessionId));
		
		waitingSessions.remove(sessionId);
		continueSession(sessionId);
	}

	
	protected void storeSession(Session session)
	{
		sessions.put(session.getId(), session);
	}
	
	
	private void continueSession(String sessionId)
	{
		Session session = sessions.get(sessionId);
		session.advance();
	}


	private void runTaskThread()
	{
		queuePoller.submit(() -> {
			while (true)
			{
				try
				{
					Session session = tasks.take();
					log.trace("Executing a task for the session with ID " + session.getId());
					
					executeTask(session);
				}
				catch (InterruptedException e)
				{
					log.error("An error occurred while polling tasks, message = " + e.getMessage());
				}
			}
		});
	}
	
	
	private void executeTask(Session session)
	{
		Node node = session.getCurrentNode();
		node.setTimeout(timeout);
		
		taskExecutor.submit(() -> node.execute(new NodeCallback()
		{
			@Override
			public void onTaskComplete()
			{
				session.advance();
			}
		}));
	}
	

	@Override
	public void onTaskRunning(Session session)
	{
		// put this session up for execution :)
		tasks.add(session);
	}
	

	@Override
	public void onTaskComplete(Session session)
	{
		Node node = session.getCurrentNode();
		taskObservers.forEach(observer -> observer.onTaskComplete(node));
	}
	

	@Override
	public void onSessionComplete(Session session)
	{
		// this is the last node
		sessionObservers.forEach(observer -> observer.onSessionComplete(session));
	}


	@Override
	public void onSessionWaiting(Session session)
	{
		waitingSessions.put(session.getId(), session);
	}
}
