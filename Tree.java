import java.util.ArrayList;
import java.util.HashMap;

public class Tree<Id> {

	/**
	 * Min heap tree. Every node has two child nodes (left and right) and both of
	 * them are required to have a higher cost. As this property has to be followed
	 * the higher node in the tree (index 0) keeps the minimum cost
	 */
	ArrayList<TreeNode<Id>> list = new ArrayList<TreeNode<Id>>();

	/**
	 * Keeps track of the position of every element in the tree. So if it is needed
	 * to access a node by Id there's no need to iterate through all the list
	 */
	HashMap<Id, Integer> indexes = new HashMap<>();

	/**
	 * @param i index of the node
	 * @return index of the parent node of the node with index i.
	 */
	int parent(int i) {
		return (i - 1) / 2;
	}

	/**
	 * @param i index of the node
	 * @return index of the left child node of the node with index i.
	 */
	int leftChild(int i) {
		return 2 * i + 1;
	}

	/**
	 * @param i index of the node
	 * @return index of the right child node of the node with index i.
	 */
	int rightChild(int i) {
		return 2 * i + 2;
	}

	/**
	 * Returns the node with index 0, which is the node with minimum cost
	 * 
	 * @return node with index 0.
	 */
	TreeNode<Id> getMin() {
		return list.get(0);
	}

	/**
	 * Adds the node to the tree. The node it's added in the last position and then
	 * the tree is rearranged.
	 * 
	 * @param node the new node to be added
	 * @return void
	 */
	void add(TreeNode<Id> node) {
		list.add(node);
		indexes.put(node.id, list.size() - 1);
		checkUpwards(list.size() - 1);
	}

	/**
	 * Deletes the node in position 0 and rearranges the tree.
	 * 
	 * @return void
	 */
	void deleteMinimum() {
		swap(0, list.size() - 1);
		indexes.remove(list.get(list.size() - 1).id);
		list.remove(list.size() - 1);
		checkDownwards(0);
	}

	/**
	 * Updates the node associated with the id
	 * 
	 * @param id   the id associated with the node to be updated
	 * @param cost new cost for the node to be updated
	 * @return void
	 */
	void update(Id id, Double cost) {
		updateWithIndex(indexes.get(id), cost);
	}

	/**
	 * Updates the node specified with the new cost If cost is smaller than the
	 * nodes above it will update above If not then it will stay the same and then
	 * check below.
	 * 
	 * @param index the index of the element to be updated
	 * @param cost  new cost for the node with position index
	 * @return void
	 */
	void updateWithIndex(int index, Double cost) {
		list.get(index).cost = cost;
		checkUpwards(index);
		checkDownwards(index);
	}

	/**
	 * If the parent node has higher cost, both are swapped and we check again
	 * 
	 * @param index the index of the node to check
	 * @return void
	 */
	void checkUpwards(int index) {
		int parent = parent(index);
		if (parent >= 0) {
			if (list.get(index).cost < list.get(parent).cost) {
				swap(index, parent);
				checkUpwards(parent);
			}
		}
	}

	/**
	 * If any child has a smaller cost, they are swapped and we check again
	 * 
	 * @param index the index of the node to check
	 * @return void
	 */
	void checkDownwards(int index) {
		int right = rightChild(index);
		int left = leftChild(index);
		int min = index;

		if (right < list.size()) {
			if (list.get(right).cost < list.get(index).cost) {
				min = right;
			}
		}
		if (left < list.size()) {
			if (list.get(left).cost < list.get(min).cost) {
				min = left;
			}
		}
		if (min != index) {
			swap(index, min);
			checkDownwards(min);
		}
	}

	/**
	 * Swaps two nodes of the tree. The indexes list is updated and the nodes are
	 * swapped in the tree
	 * 
	 * @param index1 index of first node to swap
	 * @param index2 index of second node to swap
	 * @return void
	 */
	void swap(int index1, int index2) {
		indexes.put(list.get(index2).id, index1);
		indexes.put(list.get(index1).id, index2);
		TreeNode<Id> buffer = new TreeNode<Id>();
		buffer.copy(list.get(index2));
		list.get(index2).copy(list.get(index1));
		list.get(index1).copy(buffer);

	}
}
