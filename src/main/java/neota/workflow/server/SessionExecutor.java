package neota.workflow.server;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import neota.workflow.elements.Lane;
import neota.workflow.elements.Session;
import neota.workflow.elements.Workflow;
import neota.workflow.elements.nodes.Node;
import neota.workflow.elements.nodes.NodeType;


public class SessionExecutor
{
	@Data
	@AllArgsConstructor
	private static class NodeTask
	{
		Session session;
		Node node;
	}

	
	@Data
	@AllArgsConstructor
	private static class FutureInfo
	{
		Session session;
		Node node;
		Future<?> future;
	}
	
	
	private ExecutorService executor = Executors.newFixedThreadPool(20);
	private Map<String, Session> sessions = new HashMap<>();
	
	BlockingQueue<NodeTask> queue = new LinkedBlockingQueue<NodeTask>();
	List<FutureInfo> futures = Collections.synchronizedList(new LinkedList<>());
	
	public SessionExecutor()
	{
		Thread tasker = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					NodeTask task = queue.poll();
					Node node = task.getNode();
					Session session = task.getSession();
					
					if (node != null)
					{
						Future<?> future = executor.submit(() -> node.execute());
						futures.add(new FutureInfo(session, node, future));
					}
				}
			}
		});
		
		Thread checker = new Thread(new Runnable()
		{			
			@Override
			public void run()
			{
				while (true)
				{
					List<FutureInfo> completed = new ArrayList<>();

					// go over all the futures and check them
					for (FutureInfo f : futures)
					{
						// if the task is done move on to the next one
						Future<?> future = f.getFuture();
						if (future.isDone())
						{
							completed.add(f);
							
							Node node = f.getNode();
							
							String output = "";
							if (node.getType() == NodeType.TASK)
							{
								output = node.getName();
							}
							else if (node.getType() == NodeType.NOP)
							{
								output = "NOP";
							}
							
							output += " completed";
							System.out.println(output);
							
							if (node.getType() == NodeType.END)
							{
								// this is the last node
								System.out.println("Session completed");
							}
							else
							{
								Session session = f.getSession();
								Workflow workflow = session.getWorkflow();
								
								final Lane currentLane = workflow.getNodesLane(node.getId());
								final String nextNodeId = node.getOutgoing();
								final Lane nextNodesLane = workflow.getNodesLane(nextNodeId);
								
								if (nextNodesLane.getId().equals(currentLane.getId()))
								{
									// continue as normal
									queue.add(new NodeTask(session, workflow.getNode(nextNodeId)));
								}
								else
								{
									// need to wait for the signal from outside
								}
							}
						}
					}
					
					futures.removeAll(completed);
				}
			}
		});
		
		tasker.start();
		checker.start();
	}
	
	
	public void submit(Session session)
	{
		Workflow workflow = session.getWorkflow();
		
		final String startLaneId = workflow.getStartLane();
		final Lane startLane = workflow.getLane(startLaneId);
		
		final String startNodeId = startLane.getStartNode();
		final Node startNode = workflow.getNode(startNodeId);
		
		sessions.put(session.getId(), session);
		
		// add the first task to the execution queue
		Node currentNode = workflow.getNode(startNode.getOutgoing());
		queue.add(new NodeTask(session, currentNode));
	}
}
