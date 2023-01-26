import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Node implements NodeInterface<Integer, Node, BackTracking> {

	/**
	 * Stores the average earth radius in meters Used for calculating distances.
	 */
	static final Integer EARTH_RADIUS = 6371000;
	static final String SUMA = "SUMA";
	static final String MULT = "MULT";
	static Random r = new Random();

	// BACKTRACKING INFORMATION

	/**
	 * Stores the node id of the node to visit this node with the lower cost. This
	 * is used to keep track of the path to get to every node.
	 */
	Integer prevNode;

	// NODE INFORMATION

	/**
	 * Node id
	 */
	Integer id;

	/**
	 * Node latitude in radians
	 */
	Double lat;

	/**
	 * Node longitude in radians
	 */
	Double lon;

	/**
	 * Stores a list of every connected node id.
	 */
	ArrayList<Integer> connections = new ArrayList<>();

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
	 * Creates a node from a list of data
	 * 
	 * @param list of data containing:
	 *             (id,name,place,highway,route,ref,oneway,maxspeed,lat,lon)
	 */
	Node(String[] list) {
		id = Integer.parseInt(list[1]);
		lat = degreesToRad(Double.parseDouble(list[9]));
		lon = degreesToRad(Double.parseDouble(list[10]));
	}

	/**
	 * @return Arraylist containing the Id of all the connected nodes to this node.
	 */
	public ArrayList<Integer> getConnections() {
		return connections;
	}

	/**
	 * Updates node information: g, f and prevNode When this node is visited for the
	 * first time or it is on the open set and a cheaper cost path is found to get
	 * to node it has to be updated.
	 * 
	 * @param prevNode new previous node
	 */
	public void update(Node prevNode) {
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
	public void set(Node prevNode, Node endNode) {
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
	public void setAlternative(Node prevNode, Node endNode, String mode, Double upperBound, HashMap<Integer, Integer> heuristicMap) {
		if (mode == SUMA) {
			h = h(endNode);
			h = h + r.nextDouble()*upperBound;
			update(prevNode);
		} else {
			h = h(endNode);
			h = h*(1+ r.nextDouble()*upperBound);
			update(prevNode);
		}

	}

	/**
	 * Returns the value of the heuristic function. The distance between this node
	 * and the end node.
	 * 
	 * @param endNode goal node
	 * @return distance between this and endNode
	 */
	public Double h(Node endNode) {
		return distance(this, endNode);
	}

	/**
	 * Returns the value of the cost of the path from the start node to this one.
	 * This cost is the cost of the previous node + distance between previous and
	 * this node.
	 * 
	 * @param prevNode
	 * @return cost from start to here
	 */
	public Double g(Node prevNode) {
		return prevNode == null ? 0. : prevNode.g + distance(prevNode, this);
	}

	/**
	 * Returns g + h
	 * 
	 * @param prevNode
	 * @return value of f function
	 */
	public Double f(Node prevNode) {
		return h + g(prevNode);
	}
	
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
	public Double f(Node prevNode, Double g) {
		return g + h;
	}

	/**
	 * @return returns the stored id of the node
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Creates the backtracking element for this node and returns it. This contains
	 * the id and the previous node id
	 * 
	 * @return returns the backtracking element for this node
	 */
	public BackTracking getBacktracking() {
		return new BackTracking(id, prevNode);
	}

	/**
	 * Computes the distance between node1 and node2. The calculated distance is the
	 * minimum distance between two points in a sphere assuming a constant radius
	 * {EARTH_RADIUS}
	 * 
	 * @param node1
	 * @param node2
	 * @return the distance between the two points.
	 */
	public static Double distanceSphere(Node node1, Node node2) {

		Double arc;
		Double d = (Math.cos((Math.PI / 2) - node1.lat) * Math.cos((Math.PI / 2) - node2.lat)
				+ Math.sin((Math.PI / 2) - node1.lat) * Math.sin((Math.PI / 2) - node2.lat)
						* Math.cos(node2.lon - node1.lon));
		if (d < 1) {
			arc = node1.lon == node2.lon ? Math.abs(node2.lat - node1.lat) : Math.acos(d.doubleValue());
		} else {
			arc = 0.;
		}

		return Math.abs(arc * EARTH_RADIUS);

	}
	
	/**
	 * Computes the distance between node1 and node2. The calculated distance is the
	 * straight line distance between the two nodes as 3 dimensional points.
	 * 
	 * @param node1
	 * @param node2
	 * @return the distance between the two points.
	 */
	public static Double distance(Node node1, Node node2) {

		Double x1,x2,y1,y2,z1,z2;
		x1=EARTH_RADIUS*Math.cos(node1.lat)*Math.cos(node1.lon);
		x2=EARTH_RADIUS*Math.cos(node2.lat)*Math.cos(node2.lon);
		y1=EARTH_RADIUS*Math.cos(node1.lat)*Math.sin(node1.lon);
		y2=EARTH_RADIUS*Math.cos(node2.lat)*Math.sin(node2.lon);
		z1=EARTH_RADIUS*Math.sin(node1.lat);
		z2=EARTH_RADIUS*Math.sin(node2.lat);
		
		return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) +(z2-z1)*(z2-z1));

	}

	/**
	 * Converts from degrees to radians
	 * 
	 * @param deg angle in degrees
	 * @return the angle converted to radians.
	 */
	public Double degreesToRad(Double deg) {
		return deg * Math.PI / 180.;
	}

	/**
	 * Converts from degrees to radians
	 * 
	 * @param deg angle in degrees
	 * @return the angle converted to radians.
	 */
	public Double radToDegrees(Double rad) {
		return rad * 180. / Math.PI;
	}

	@Override
	public String toString() {
		return id + "|" + radToDegrees(lat) + "|" + radToDegrees(lon) + "|" + connections.toString() + "\n";
	}
}
