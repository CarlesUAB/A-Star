import java.util.ArrayList;
import java.util.HashMap;

public interface NodeInterface<Id, Node, Backtracking> {

	/**
	 * @return Arraylist containing the Id of all the connected nodes to this node.
	 */
	public ArrayList<Id> getConnections();

	/**
	 * Returns the value of the heuristic function.
	 * 
	 * @param endNode goal node
	 * @return heuristic result
	 */
	public Double h(Node endNode);

	/**
	 * Returns the value of the cost of the path from the start node to this one.
	 * This cost is the cost of the previous node + cost between previous and this
	 * node.
	 * 
	 * @param prevNode
	 * @return cost from start to here
	 */
	public Double g(Node prevNode);

	/**
	 * Returns g + h
	 * 
	 * @param prevNode
	 * @return value of f function
	 */
	public Double f(Node prevNode);

	/**
	 * Returns stored value of f
	 * 
	 * @return value of f function stored
	 */
	public Double getF();

	/**
	 * Updates node information: g, f and prevNode When this node is visited for the
	 * first time or it is on the open set and a cheaper cost path is found to get
	 * to node it has to be updated.
	 * 
	 * @param prevNode new previous node
	 */
	public void update(Node prevNode);

	/**
	 * This node is being introduced to the open set so all the path costs
	 * information fields are populated
	 * 
	 * @param prevNode previous node
	 * @param endNode  the goal node
	 */
	public void set(Node prevNode, Node endNode);

	/**
	 * This node is being introduced to the open set so all the path costs
	 * information fields are populated. In this case we are not using an admissible
	 * heuristic.
	 * 
	 * @param mode       it can either be fixed or variable. Determines which non
	 *                   admissible heuristic is being used.
	 * @param upperBound how much bigger the heuristic can be than the original
	 *                   heuristic.
	 * @param prevNode   previous node
	 * @param endNode    the goal node
	 */
	public void setAlternative(Node prevNode, Node endNode, String mode, Double upperBound,
			HashMap<Id, Integer> heuristicMap);

	/**
	 * @return returns the stored id of the node
	 */
	public Id getId();

	/**
	 * Creates the backtracking element for this node and returns it. This contains
	 * the id and the previous node id and possibly more information
	 * 
	 * @return returns the backtracking element for this node
	 */
	public Backtracking getBacktracking();

}
