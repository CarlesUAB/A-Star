
public class TreeNode <Id> {
	
	/**
	 * Stores the id of the node
	 */
	Id id;
	
	/**
	 * Stores the cost of the node
	 */
	Double cost;
	
	/**
	 * Empty constructor
	 */
	TreeNode(){
	}
	
	/**
	 * Full constructor
	 * 
	 * @param id
	 * @param cost
	 */
	TreeNode(Id id, Double cost){
		this.id=id;
		this.cost=cost;
	}
	
	/**
	 * Populate fields copying from node
	 * 
	 * @param node node to copy from
	 */
	void copy(TreeNode<Id> node) {
		id=node.id;
		cost=node.cost;
	}
}
