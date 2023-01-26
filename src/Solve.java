
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Solve {

	/**
	 * Finds the minimum cost (optimal) path between Start and End using A* search.
	 * Standard search (Admissible heuristic with no modifications).
	 * 
	 * @param <Node>         Class containing some information of the node itself
	 *                       and the connected nodes ids
	 * @param <Id>           Id. Every node has one unique id. Used to identify
	 *                       nodes along the process
	 * @param <Backtracking> Element used for keeping track of the node that visited
	 *                       every other node with lower cost. Contains at least the
	 *                       node id and the previous node id.
	 * @param graf           Set of nodes forming a graph
	 * @param start          Id of the first node in the search
	 * @param end            Id of the goal node in the search
	 * @return
	 */
	static <Node extends NodeInterface<Id, Node, Backtracking>, Id, Backtracking extends BacktrackingInterface<Id>> ArrayList<Backtracking> find(
			HashMap<Id, Node> graf, Id start, Id end) {

		HashMap<Id, Double> open = new HashMap<>();
		Tree<Id> openTree = new Tree<Id>();

		HashSet<Id> closed = new HashSet<>();

		graf.get(start).set(null, graf.get(end));
		open.put(start, graf.get(start).f(null));
		openTree.add(new TreeNode<Id>(start, graf.get(start).f(null)));

		while (true) {
			if(open.size()<=0) {
				System.out.println("Path not found. START:" + graf.get(start) + " END: " + graf.get(end));
				return null;
			}
			Id minId = openTree.getMin().id;

			open.remove(minId);
			openTree.deleteMinimum();
			closed.add(minId);

			// Check if solved
			if (minId.equals(end)) {
				System.out.println("Optimal path found!");

				ArrayList<Backtracking> result = new ArrayList<>();
				Id id = end;
				while (!id.equals(start)) {
					Backtracking b = graf.get(id).getBacktracking();
					result.add(0, b);
					id = b.getId();
				}
				result.add(0, graf.get(start).getBacktracking());
				return result;

			}

			ArrayList<Id> connections = graf.get(minId).getConnections();
			
			for (Id id : connections) {
				if (closed.contains(id)) {
					Double cost = graf.get(id).f(graf.get(minId));
					if (cost < graf.get(id).getF()) {
						open.put(id, cost);
						openTree.add(new TreeNode<Id>(id, cost));
						graf.get(id).update(graf.get(minId));
					}
				} else if (open.containsKey(id)) {
					Double cost = graf.get(id).f(graf.get(minId));
					if (cost < open.get(id)) {
						open.put(id, cost);
						openTree.update(id, cost);
						graf.get(id).update(graf.get(minId));
					}
				} else {
					graf.get(id).set(graf.get(minId), graf.get(end));
					Double cost = graf.get(id).f(graf.get(minId));
					open.put(id, cost);
					openTree.add(new TreeNode<Id>(id, cost));
				}
			}
		}
	}

	/**
	 * Finds the minimum cost (optimal) path between Start and End using A* search.
	 * Standard search (Admissible heuristic with no modifications).
	 * 
	 * @param <Node>         Class containing some information of the node itself
	 *                       and the connected nodes ids
	 * @param <Id>           Id. Every node has one unique id. Used to identify
	 *                       nodes along the process
	 * @param <Backtracking> Element used for keeping track of the node that visited
	 *                       every other node with lower cost. Contains at least the
	 *                       node id and the previous node id.
	 * @param graf           Set of nodes forming a graph
	 * @param start          Id of the first node in the search
	 * @param end            Id of the goal node in the search
	 * @param c              Small double greater than 0
	 * @return
	 */

	static <Node extends NodeInterface<Id, Node, Backtracking>, Id, Backtracking extends BacktrackingInterface<Id>> ArrayList<Backtracking> findAlternative(
			HashMap<Id, Node> graf, Id start, Id end, String mode, Double upperBound, HashMap<Id, Integer> heuristicMap) {

		HashMap<Id, Double> open = new HashMap<>();
		Tree<Id> openTree = new Tree<Id>();
		HashSet<Id> closed = new HashSet<>();

		graf.get(start).set(null, graf.get(end));
		open.put(start, graf.get(start).f(null));
		openTree.add(new TreeNode<Id>(start, graf.get(start).f(null)));

		while (true) {

			if(open.size()<=0) {
				System.out.println("Path not found. START:" + graf.get(start) + " END: " + graf.get(end));
				return null;
			}
			Id minId = openTree.getMin().id;

			open.remove(minId);
			openTree.deleteMinimum();
			closed.add(minId);

			// Check if solved
			if (minId.equals(end)) {
				ArrayList<Backtracking> result = new ArrayList<>();
				Id id = end;
				while (!id.equals(start)) {
					Backtracking b = graf.get(id).getBacktracking();
					result.add(0, b);
					id = b.getId();
				}
				result.add(0, graf.get(start).getBacktracking());
				return result;

			}

			ArrayList<Id> connections = graf.get(minId).getConnections();
			for (Id id : connections) {
				if (closed.contains(id)) {
					Double cost = graf.get(id).f(graf.get(minId));
					if (cost < graf.get(id).getF()) {
						open.put(id, cost);
						openTree.add(new TreeNode<Id>(id, cost));
						graf.get(id).update(graf.get(minId));
					}
				} else if (open.containsKey(id)) {
					Double cost = graf.get(id).f(graf.get(minId));
					if (cost < open.get(id)) {
						open.put(id, cost);
						openTree.update(id, cost);
						graf.get(id).update(graf.get(minId));
					}
				} else {
					graf.get(id).setAlternative(graf.get(minId), graf.get(end), mode, upperBound , heuristicMap);
					Double cost = graf.get(id).f(graf.get(minId));
					open.put(id, cost);
					openTree.add(new TreeNode<Id>(id, cost));
				}
			}
		}
	}

	/**
	 * Finds all the optimum paths from start to all the other nodes.
	 * 
	 * @param <Node>         Class containing some information of the node itself
	 *                       and the connected nodes ids
	 * @param <Id>           Id. Every node has one unique id. Used to identify
	 *                       nodes along the process
	 * @param <Backtracking> Element used for keeping track of the node that visited
	 *                       every other node with lower cost. Contains at least the
	 *                       node id and the previous node id.
	 * @param graf           Set of nodes forming a graph
	 * @param start          Id of the first node in the search
	 * @return
	 */
	static <Node extends NodeInterface<Id, Node, Backtracking>, Id, Backtracking extends BacktrackingInterface<Id>> void findAllSolutions(
			HashMap<Id, Node> graf, Id start) {

		HashMap<Id, Double> open = new HashMap<>();
		Tree<Id> openTree = new Tree<Id>();
		HashSet<Id> closed = new HashSet<>();

		graf.get(start).set(null, graf.get(start));
		open.put(start, graf.get(start).f(null));
		openTree.add(new TreeNode<Id>(start, graf.get(start).f(null)));

		while (true) {

			if(open.size()<=0) {
				System.out.println("Guide graph built");
				break;
			}
			Id minId = openTree.getMin().id;

			open.remove(minId);
			openTree.deleteMinimum();
			closed.add(minId);

			ArrayList<Id> connections = graf.get(minId).getConnections();
			for (Id id : connections) {
				if (closed.contains(id)) {
				} else if (open.containsKey(id)) {
					Double cost = graf.get(id).f(graf.get(minId));
					if (cost < open.get(id)) {
						open.put(id, cost);
						openTree.update(id, cost);
						graf.get(id).update(graf.get(minId));
					}
				} else {
					graf.get(id).set(graf.get(minId), graf.get(start));
					Double cost = graf.get(id).f(graf.get(minId));
					open.put(id, cost);
					openTree.add(new TreeNode<Id>(id, cost));
				}
			}
		}
	}
	
	/**
	 * The paths are already been found. This method returns the path from start to end by only
	 * checking where the nodes aim to.
	 * 
	 * @param <Node>         Class containing some information of the node itself
	 *                       and the connected nodes ids
	 * @param <Id>           Id. Every node has one unique id. Used to identify
	 *                       nodes along the process
	 * @param <Backtracking> Element used for keeping track of the node that visited
	 *                       every other node with lower cost. Contains at least the
	 *                       node id and the previous node id.
	 * @param graf           Set of nodes forming a graph
	 * @param start          Id of the first node in the search
	 * @param end            Id of the goal node in the search
	 * @return
	 */
	
	static <Node extends NodeInterface<Id, Node, Backtracking>, Id, Backtracking extends BacktrackingInterface<Id>> ArrayList<Backtracking> rememberSolution(
			HashMap<Id, Node> graf, Id start, Id end) {

		ArrayList<Backtracking> result = new ArrayList<>();
		Id id = start;
		while (!id.equals(end)) {
			Backtracking b = graf.get(id).getBacktracking();
			result.add(b);
			id = b.getId();
		}
		result.add(0, graf.get(end).getBacktracking());
		return result;
	}
}
