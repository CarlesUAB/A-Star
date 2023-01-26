
public class BackTracking implements BacktrackingInterface<Integer> {

	/**
	 * Stores the id
	 */
	Integer id;
	
	/**
	 * Stores the node id of the node to visit this node with the lower cost. This
	 * is used to keep track of the path to get to every node.
	 */
	Integer prevId;

	/**
	 * Creates a backtracking element storing the id and the previous node id
	 * 
	 * @param id
	 * @param prevNode
	 */
	BackTracking(Integer id, Integer prevNode) {
		this.id = id;
		prevId = prevNode;
	}

	/**
	 * @return the previous id
	 */
	public Integer getId() {
		return prevId;
	}

	@Override
	public String toString() {
		return id + "\n";
	}
}
