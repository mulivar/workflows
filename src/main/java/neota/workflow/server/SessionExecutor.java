package neota.workflow.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import neota.workflow.elements.NodeCallback;
import neota.workflow.elements.Session;
import neota.workflow.elements.SessionCallback;
import neota.workflow.elements.Workflow;
import neota.workflow.elements.nodes.Node;
import neota.workflow.exceptions.ValidationException;
import neota.workflow.validation.SessionResumeRule;
import neota.workflow.validation.ValidationRule;
import neota.workflow.validation.Validator;


public class SessionExecutor implements SessionCallback
{
	public static final int THREAD_COUNT	= 20;
	public static final int QUEUE_LENGTH	= 1000;
	
	private ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
	private ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
	
	private Map<String, Session> sessions = new ConcurrentHashMap<>();
	private Map<String, Session> waitingSessions = new ConcurrentHashMap<>();
	
	private BlockingQueue<Session> tasks = new ArrayBlockingQueue<>(QUEUE_LENGTH);

	private List<SessionObserver> taskObservers = new ArrayList<>();
	private List<SessionObserver> sessionObservers = new ArrayList<>();
	
	private Validator validator = new Validator();
	
	private int timeout = Node.DEFAULT_TIMEOUT;
	
	
	public SessionExecutor()
	{
		runTaskThread();
	}
	
	
	public void shutdown()
	{
		executor.shutdown();
		taskExecutor.shutdown();
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
	
	
	public String submit(Workflow workflow)
	{
		final String sessionId = UUID.randomUUID().toString();
		Session session = new Session(sessionId, workflow, this);
		
		sessions.put(sessionId, session);
		continueSession(sessionId);
		
		return sessionId;
	}
	
	
	public void resumeSession(String sessionId) throws ValidationException
	{
		List<ValidationRule> rules = new ArrayList<>();
		rules.add(new SessionResumeRule(sessions.get(sessionId), waitingSessions));
		
		validator.validate(rules);
		
		waitingSessions.remove(sessionId);
		continueSession(sessionId);
	}


	private void runTaskThread()
	{
		taskExecutor.submit(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						Session session = tasks.take();
						if (session != null)
						{
							executeTask(session);
						}
					}
					catch (InterruptedException e)
					{
						System.err.println("An error occurred while polling tasks, message = " + e.getMessage());
					}
				}
			}
		});
	}
	
	
	private void executeTask(Session session)
	{
		Node node = session.getCurrentNode();
		node.setTimeout(timeout);
		
		executor.submit(() -> node.execute(new NodeCallback()
		{
			@Override
			public void onTaskComplete()
			{
				System.out.println("onSessionTaskComplete, state = " + session.getState().name());
				continueSession(session.getId());
			}
		}));
	}
	
	
	private void continueSession(String sessionId)
	{
		Session session = sessions.get(sessionId);
		System.out.println("continueSession, current state before = " + session.getState().name());
		session.advance();
		System.out.println("continueSession, current state after = " + session.getState().name());
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
		
		continueSession(session.getId());
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
