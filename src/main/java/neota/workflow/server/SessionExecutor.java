package neota.workflow.server;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import neota.workflow.elements.NodeCallback;
import neota.workflow.elements.Session;
import neota.workflow.elements.nodes.Node;


public class SessionExecutor
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
	
	
	public void submit(Session session)
	{
		sessions.put(session.getId(), session);
		continueSession(session.getId());
	}
	
	
	public void resumeSession(String sessionId)
	{
		if (!waitingSessions.containsKey(sessionId))
		{
			throw new RuntimeException(MessageFormat.format("The session with ID {0} is not among the waiting sessions",
					sessionId));
		}
		else
		{
			waitingSessions.remove(sessionId);
			continueSession(sessionId);
		}
	}
	
	
	private void onSessionTaskComplete(Session session)
	{
		System.out.println("onSessionTaskComplete, state = " + session.getState().name());
		
		if (session.isRunning())
		{
			onTaskComplete(session);
			continueSession(session.getId());
		}
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
					Session session = tasks.poll();
					if (session != null)
					{
						advanceSession(session);
					}
				}
			}
		});
	}
	
	
	private void advanceSession(Session session)
	{
		if (session.isRunning())
		{
			executeSession(session);
		}
		else if (session.isWaiting())
		{
			waitSession(session);
		}
	}
	
	
	private void executeSession(Session session)
	{
		Node node = session.getCurrentNode();
		node.setTimeout(timeout);
		
		executor.submit(() -> node.execute(new NodeCallback()
		{
			@Override
			public void onTaskComplete()
			{
				onSessionTaskComplete(session);
			}
		}));
	}
	
	
	private void waitSession(Session session)
	{
		waitingSessions.put(session.getId(), session);
	}
	
	
	private void continueSession(String sessionId)
	{
		Session session = sessions.get(sessionId);
		System.out.println("continueSession, current state before = " + session.getState().name());
		session.advance();
		System.out.println("continueSession, current state after = " + session.getState().name());

		if (session.isDone())
		{
			onSessionComplete(session);
		}
		else
		{
			// put this session up for execution :)
			tasks.add(session);
		}
	}
	
	
	private void onTaskComplete(Session session)
	{
		Node node = session.getCurrentNode();
		taskObservers.forEach(observer -> observer.onTaskComplete(node));
	}
	
	
	private void onSessionComplete(Session session)
	{
		// this is the last node
		sessionObservers.forEach(observer -> observer.onSessionComplete(session));
	}
}
