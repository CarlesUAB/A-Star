import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Rubik2Node implements NodeInterface<Rubik2Id, Rubik2Node, Rubik2Backtracking> {

	
	/**
	 * Non admissible heuristic modes
	 */
	static final String SUMA = "SUMA";
	static final String MULT = "MULT";
	
	Random r = new Random();
	
	/**
	 * 0: WHITE, 1: GREEN, 2: RED, 3:YELLOW, 4: BLUE, 5: ORANGE Stores the face that
	 * every tile aims to. Every element represents a corner and every sub element a
	 * tile of the corner
	 */
	static final byte[][] FACENUMBER = { { 0, 1, 5 }, { 0, 2, 1 }, { 0, 4, 2 }, { 0, 5, 4 }, { 1, 3, 5 }, { 1, 2, 3 },
			{ 2, 4, 3 }, { 3, 4, 5 } };

	/**
	 * Stores in which position of the face is located this tile. 0 is for the
	 * corners 1 and 7, the rest of values are assigned clockwise
	 */
	static final byte[][] FACEPOSITION = { { 1, 3, 2 }, { 0, 0, 0 }, { 3, 2, 1 }, { 2, 1, 3 }, { 2, 1, 3 }, { 1, 3, 2 },
			{ 2, 1, 3 }, { 0, 0, 0 } };

	/**
	 * All the moves that can be done to the cube.
	 */
	static final String[] POSSIBLEMOVES = { "R", "R2", "R'", "F", "F2", "F'", "U", "U2", "U'" };
	
	// BACKTRACKING INFORMATION

	/**
	 * Stores the node id of the node to visit this node with the lower cost. This
	 * is used to keep track of the path to get to every node.
	 */
	Rubik2Id prevNode;

	// NODE INFORMATION

	/**
	 * Node id
	 */
	Rubik2Id id;

	/**
	 * Stores a list of every connected node id.
	 */
	ArrayList<Rubik2Id> connections = new ArrayList<>();

	/**
	 * Stores all the faces. The faces store every tile color.
	 */
	Rubik2Face[] faces = new Rubik2Face[6];

	// COSTS INFORMATION

	/**
	 * Cost of the path from the beginning to this Node.
	 */
	Double g;

	/**
	 * Estimated cost to get to the end.
	 */
	Double h;

	/**
	 * h + g
	 */
	Double f;

	/**
	 * Creates a Rubik node from an id.
	 * 
	 * @param id
	 */
	Rubik2Node(Rubik2Id id) {
		createFaces(faces);
		this.id = id;
		byte offset;
		byte corner;
		for (int i = 0; i < 8; i++) {
			offset = id.orientations[i];
			corner = id.positions[i];
			for (int j = 0; j < 3; j++) {
				faces[FACENUMBER[i][j]].e[FACEPOSITION[i][j]] = FACENUMBER[corner][(j + 2 * offset) % 3];
			}
		}
	}

	/**
	 * */
	/**
	 * Creates a Rubik node from an id. Also creates nodes connected to it, add them
	 * to connections if necessary and check and include to the graph if they need
	 * to be added
	 * 
	 * @param id
	 * @param remaining
	 * @param remainingSet
	 */
	Rubik2Node(Rubik2Id id, HashMap<Integer, Rubik2Id> remaining, HashSet<Rubik2Id> remainingSet) {

		this(id);
		createConnections(remaining, remainingSet);
	}

	/**
	 * Creates nodes connected to this node. (This creation is local) If they are
	 * not in the remainingSet they are added. (Keeping track of which nodes are
	 * added or noted to be added to the graph)
	 * 
	 * @param remaining    contains node id's noted to be added
	 * @param remainingSet contains values of {remaining} only for performance
	 */
	void createConnections(HashMap<Integer, Rubik2Id> remaining, HashSet<Rubik2Id> remainingSet) {

		for (String move : POSSIBLEMOVES) {
			Rubik2Node node = new Rubik2Node(this, move);
			connections.add(node.id);
			if (!remainingSet.contains(node.id)) {
				remaining.put(remaining.size(), node.id);
				remainingSet.add(node.id);
			}
		}
	}

	/**
	 * Creates a node from the previous node and one movement. This creation is
	 * temporary, only used to calculate its id. After that is never used and
	 * deleted.
	 * 
	 * @param prevNode
	 * @param movement
	 */
	Rubik2Node(Rubik2Node prevNode, String movement) {
		faces = copy(prevNode.faces);
		rotate(movement);
		id = calculateId();
	}

	/**
	 * @return Arraylist containing the Id of all the connected nodes to this node.
	 */
	public ArrayList<Rubik2Id> getConnections() {
		return connections;
	};

	/**
	 * Updates node information: g, f and prevNode When this node is visited for the
	 * first time or it is on the open set and a cheaper cost path is found to get
	 * to node it has to be updated.
	 * 
	 * @param prevNode new previous node
	 */
	public void update(Rubik2Node prevNode) {
		g = g(prevNode);
		f = f(prevNode, g);
		this.prevNode = prevNode == null ? null : prevNode.getId();
	}

	/**
	 * This node is being introduced to the open set so all the path costs
	 * information fields are populated
	 * 
	 * @param prevNode previous node
	 * @param endNode  the goal node
	 */
	public void set(Rubik2Node prevNode, Rubik2Node endNode) {
		h = h(endNode);
		update(prevNode);
	}

	/**
	 * This node is being introduced to the open set so all the path costs
	 * information fields are populated. In this case we are not using the
	 * admissible heuristic of the straight line distance
	 * 
	 * @param mode       it can either be fixed or variable. Determines which non
	 *                   admissible heuristic is being used.
	 * @param upperBound how much bigger the heuristic can be than the original
	 *                   heuristic.
	 * @param prevNode   previous node
	 * @param endNode    the goal node
	 */
	public void setAlternative(Rubik2Node prevNode, Rubik2Node endNode, String mode, Double upperBound,
			HashMap<Rubik2Id, Integer> heuristicMap) {
		h = h(endNode);
		update(prevNode);
	}

	/**
	 * Returns the value of the heuristic function (0 in this case)
	 * 
	 * @param endNode
	 * @return 0
	 */
	public Double h(Rubik2Node endNode) {
		return 0.;
	}

	/**
	 * Returns the value of the cost of the path from the start node to this one.
	 * This cost is the cost of the previous node + 1
	 * 
	 * @param prevNode
	 * @return cost from start to here
	 */
	public Double g(Rubik2Node prevNode) {
		return prevNode != null ? prevNode.g + 1 : 0.;
	}

	/**
	 * Returns g + h
	 * 
	 * @param prevNode
	 * @return value of f function
	 */
	public Double f(Rubik2Node prevNode) {
		return g(prevNode) + h;
	}

	/**
	 * Returns f stored value
	 * 
	 * @return value of f stored
	 */
	public Double getF() {
		return f;
	}

	/**
	 * Returns g + h, without having to calculate g
	 * 
	 * @param prevNode
	 * @param g        already calculated g value.
	 * @return value of f function
	 */
	public Double f(Rubik2Node prevNode, Double g) {
		return g + h;
	}

	/**
	 * @return returns the stored id of the node
	 */
	public Rubik2Id getId() {
		return id;
	}

	/**
	 * Calculates the id of this node and returns it
	 * 
	 * @return returns the id of the node
	 */
	public Rubik2Id calculateId() {
		return new Rubik2Id(this);
	}

	/**
	 * Creates the backtracking element for this node and returns it. This contains
	 * the id, the previous node id, and the movement that joins them
	 * 
	 * @return returns the backtracking element for this node
	 */
	public Rubik2Backtracking getBacktracking() {
		return new Rubik2Backtracking(id, prevNode);
	}

	/**
	 * Populates the array with empty faces.
	 * 
	 * @param faces face array to be populated
	 */
	void createFaces(Rubik2Face[] faces) {
		for (int i = 0; i < 6; i++) {
			faces[i] = new Rubik2Face();
		}
	}

	/**
	 * Creates a face array identical to f. Needed since java passes by reference
	 * 
	 * @param f face array to copy from
	 * @return face array identical to f
	 */
	public Rubik2Face[] copy(Rubik2Face[] f) {
		Rubik2Face[] buffer = new Rubik2Face[6];
		createFaces(buffer);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				buffer[i].e[j] = f[i].e[j];
			}
		}
		return buffer;
	}

	/**
	 * Applies the movement to this node, changing the position of some corners (and
	 * their tiles).
	 * 
	 * @param movement
	 */
	public void rotate(String movement) {
		switch (movement) {
		case "R":
			rotateClockwise(faces[2], faces[0], faces[4], faces[3], faces[1]);
			break;
		case "R2":
			rotate2(faces[2], faces[0], faces[4], faces[3], faces[1]);
			break;
		case "R'":
			rotateAnticlockwise(faces[2], faces[0], faces[4], faces[3], faces[1]);
			break;
		case "U":
			rotateClockwise(faces[0], faces[1], faces[5], faces[4], faces[2]);
			break;
		case "U2":
			rotate2(faces[0], faces[1], faces[5], faces[4], faces[2]);
			break;
		case "U'":
			rotateAnticlockwise(faces[0], faces[1], faces[5], faces[4], faces[2]);
			break;
		case "F":
			rotateClockwise(faces[1], faces[2], faces[3], faces[5], faces[0]);
			break;
		case "F2":
			rotate2(faces[1], faces[2], faces[3], faces[5], faces[0]);
			break;
		case "F'":
			rotateAnticlockwise(faces[1], faces[2], faces[3], faces[5], faces[0]);
			break;
		}
	}

	/**
	 * Applies a clockwise rotation to f0 face.
	 * 
	 * @param f0  face that rotates
	 * @param fa, fb, fc, fd rest of faces affected by the rotation.
	 */
	public void rotateClockwise(Rubik2Face f0, Rubik2Face fa, Rubik2Face fb, Rubik2Face fc, Rubik2Face fd) {
		byte buffer1;
		byte buffer2;
		buffer1 = f0.e[0];
		f0.e[0] = f0.e[3];
		f0.e[3] = f0.e[2];
		f0.e[2] = f0.e[1];
		f0.e[1] = buffer1;

		buffer1 = fa.e[0];
		buffer2 = fa.e[3];
		fa.e[0] = fd.e[1];
		fa.e[3] = fd.e[0];
		fd.e[0] = fc.e[2];
		fd.e[1] = fc.e[3];
		fc.e[2] = fb.e[1];
		fc.e[3] = fb.e[2];
		fb.e[1] = buffer2;
		fb.e[2] = buffer1;
	}

	/**
	 * Applies a (180 deg) rotation to f0 face.
	 * 
	 * @param f0  face that rotates
	 * @param fa, fb, fc, fd rest of faces affected by the rotation.
	 */
	public void rotate2(Rubik2Face f0, Rubik2Face fa, Rubik2Face fb, Rubik2Face fc, Rubik2Face fd) {
		byte buffer1;
		byte buffer2;
		buffer1 = f0.e[0];
		f0.e[0] = f0.e[2];
		f0.e[2] = buffer1;
		buffer1 = f0.e[1];
		f0.e[1] = f0.e[3];
		f0.e[3] = buffer1;

		buffer1 = fa.e[0];
		buffer2 = fa.e[3];
		fa.e[0] = fc.e[3];
		fa.e[3] = fc.e[2];
		fc.e[3] = buffer1;
		fc.e[2] = buffer2;

		buffer1 = fd.e[1];
		buffer2 = fd.e[0];
		fd.e[1] = fb.e[2];
		fd.e[0] = fb.e[1];
		fb.e[2] = buffer1;
		fb.e[1] = buffer2;
	}

	/**
	 * Applies an anticlockwise rotation to f0 face.
	 * 
	 * @param f0  face that rotates
	 * @param fa, fb, fc, fd rest of faces affected by the rotation.
	 */
	public void rotateAnticlockwise(Rubik2Face f0, Rubik2Face fa, Rubik2Face fb, Rubik2Face fc, Rubik2Face fd) {
		byte buffer1;
		byte buffer2;
		buffer1 = f0.e[1];
		f0.e[1] = f0.e[2];
		f0.e[2] = f0.e[3];
		f0.e[3] = f0.e[0];
		f0.e[0] = buffer1;

		buffer1 = fb.e[1];
		buffer2 = fb.e[2];
		fb.e[1] = fc.e[2];
		fb.e[2] = fc.e[3];
		fc.e[2] = fd.e[0];
		fc.e[3] = fd.e[1];
		fd.e[0] = fa.e[3];
		fd.e[1] = fa.e[0];
		fa.e[3] = buffer1;
		fa.e[0] = buffer2;
	}

	@Override
	public String toString() {
		String s = "NODE: \n";
		for (int i = 0; i < 6; i++) {
			s = s + faces[i] + "\n";
		}
		return s;
	}

}
