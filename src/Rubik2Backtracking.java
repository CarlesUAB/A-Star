
public class Rubik2Backtracking implements BacktrackingInterface<Rubik2Id> {

	/**
	 * Stores the id
	 */
	Rubik2Id id;

	/**
	 * Stores the node id of the node to visit this node with the lower cost. This
	 * is used to keep track of the path to get to every node.
	 */
	Rubik2Id prevNodeId;

	/**
	 * Stores the movement to go from previous node to this one
	 */
	String movement;

	/**
	 * Creates a backtracking element storing the id, previous node id and the
	 * movement between them. This last one is not stored so has to be computed
	 * 
	 * @param id
	 * @param prevNodeId
	 */
	Rubik2Backtracking(Rubik2Id id, Rubik2Id prevNodeId) {
		this.prevNodeId = prevNodeId;
		this.id = id;
		if (!(prevNodeId == null || id == null)) {
			if (compare(2)) {
				int offset = rotation(0);
				switch (offset) {
				case 1:
					movement = "F";
					break;
				case 2:
					movement = "F2";
					break;
				case 3:
					movement = "F'";
					break;
				}
			} else if (compare(0)) {
				int offset = rotation(1);
				switch (offset) {
				case 1:
					movement = "R";
					break;
				case 2:
					movement = "R2";
					break;
				case 3:
					movement = "R'";
					break;
				}
			} else if (compare(5)) {
				int offset = rotation(2);
				switch (offset) {
				case 1:
					movement = "U";
					break;
				case 2:
					movement = "U2";
					break;
				case 3:
					movement = "U'";
					break;
				}
			}
		} else {
			movement = "START";
		}

	}

	/**
	 * Checks if the corner {i} is the same in the previous and actual nodes.
	 * 
	 * @param i
	 * @return
	 */
	boolean compare(int i) {
		return (id.positions[i] == prevNodeId.positions[i]);
	}

	/**
	 * Calculates the number of face rotations for front(i==0), right(i==1),
	 * upper(i==2) face
	 * 
	 * @param i
	 * @return the number of clockwise rotations.
	 */
	int rotation(int i) {
		switch (i) {
		case 0:
			int[] rotated = { 0, 1, 5, 4 };
			for (int j = 0; j < 4; j++) {
				if (prevNodeId.positions[rotated[0]] == id.positions[rotated[0 + j]]) {
					return j;
				}
			}
			break;
		case 1:
			int[] rotated2 = { 1, 2, 6, 5 };
			for (int j = 0; j < 4; j++) {
				if (prevNodeId.positions[rotated2[0]] == id.positions[rotated2[0 + j]]) {
					return j;
				}
			}
			break;
		case 2:
			int[] rotated3 = { 0, 3, 2, 1 };
			for (int j = 0; j < 4; j++) {
				if (prevNodeId.positions[rotated3[0]] == id.positions[rotated3[0 + j]]) {
					return j;
				}
			}
			break;
		}
		return -1;
	}

	/**
	 * @return the previous id
	 */
	public Rubik2Id getId() {
		return prevNodeId;
	}

	@Override
	public String toString() {
		return movement + " ";
	}

}
