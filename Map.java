import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.sql.Time;

public class Map {

	static Random r = new Random();

	/**
	 * Some constants indicating how to identify nodes, ways and oneway ways.
	 */
	static final String NODE = "node";
	static final String WAY = "way";
	static final String ONEWAY = "oneway";

	/**
	 * Reads the file {file}, adds all the nodes to the graph, connects the nodes as
	 * stated in ways in the file
	 * 
	 * @param file csv document containing a map
	 * @return the populated graph
	 */
	static HashMap<Integer, Node> buildGraph(String file) {

		HashMap<Integer, Node> graf = new HashMap<Integer, Node>();

		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		int DEBUGnumberOfLinesRead = 0;
		try {
			while (br.ready()) {
				DEBUGnumberOfLinesRead++;

				String[] result = br.readLine().split("[|]");
				if (NODE.equals(result[0])) {
					graf.put(Integer.valueOf(result[1]), new Node(result));

				} else if (WAY.equals(result[0])) {
					Integer[] cleanResult = new Integer[result.length - 9];
					Integer size = 0;
					// Some nodes in some ways are not contained in the node list so we delete them
					try {
						for (int i = 9; i < result.length; i++) {
							if (graf.containsKey(Integer.valueOf(result[i]))) {
								cleanResult[size] = Integer.valueOf(result[i]);
								size++;
							}
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}

					if (ONEWAY.equals(result[7])) {
						// If there are more than 2 valid nodes in this way we proceed to connect them.
						if (size >= 2) {
							for (int i = 0; i < size - 1; i++) {
								graf.get(cleanResult[i]).connections.add(cleanResult[i + 1]);
							}
						}
					} else {
						// If there are more than 2 valid nodes in this way we proceed to connect them.
						if (size >= 2) {
							graf.get(cleanResult[0]).connections.add(cleanResult[1]);
							for (int i = 1; i < size - 1; i++) {
								graf.get(cleanResult[i]).connections.add(cleanResult[i - 1]);
								graf.get(cleanResult[i]).connections.add(cleanResult[i + 1]);
							}
							graf.get(cleanResult[size - 1]).connections.add(cleanResult[size - 2]);
						}
					}

				} else {
					// If the line is neither a "node" or a "way" it is ignored.
				}
//						System.out.println(DEBUGnumberOfLinesRead);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("GRAPH CREATED SUCCESFULLY. SIZE: " + graf.size());
		System.out.println("NUMBER OF LINES READ: " + DEBUGnumberOfLinesRead);
		return graf;
	}

	/**
	 * Computes the distance of the shortest path found between two points, this
	 * calculation is done {iterations} times and with certain variations to the
	 * heuristic function.
	 * 
	 * @param percentages For every element we have a new case we calculate in every
	 *                    iteration. The values represent how much the heuristic
	 *                    function can be exceeded by h' <= h*(1+value)
	 * @param iterations
	 * @param graf
	 */
	static void compute(Double[] percentages, String mode, int iterations, HashMap<Integer, Node> graf) {

		int it = iterations;
		Integer[] nodes = graf.keySet().toArray(new Integer[0]);
		Integer start;
		Integer end;

		ArrayList<BackTracking> path;

		/*
		 * Data structure to store all the results
		 */
		ArrayList<ArrayList<Double>> data = new ArrayList<>();

		/*
		 * Initializing "data"
		 */
		for (int i = 0; i < percentages.length; i++) {
			data.add(new ArrayList<Double>());
		}

		/*
		 * Computing the results...
		 */
		while (iterations > 0) {
			System.out.println(iterations);
			start = nodes[r.nextInt(nodes.length)];
			end = nodes[r.nextInt(nodes.length)];

			while (graf.get(start).getConnections() == null || graf.get(start).getConnections().size() == 0
					|| graf.get(end).getConnections() == null || graf.get(end).getConnections().size() == 0) {
				start = nodes[r.nextInt(nodes.length)];
				end = nodes[r.nextInt(nodes.length)];
				System.out.println("Apa");
			}

			path = Solve.find(graf, start, end);
			if (path == null) {
				continue;
			} else {
				data.get(0).add(graf.get(end).g);
			}

			for (int i = 1; i < percentages.length; i++) {
				System.out.println(iterations + "." + i);
				path = Solve.findAlternative(graf, start, end, mode, percentages[i],null);
				if (path == null) {
					data.get(i).add(-1.);
				} else {
					data.get(i).add(graf.get(end).g);
				}
			}
			iterations--;
		}

		/*
		 * Printing the results to a .txt file.
		 */
		try {
			Random r = new Random();
			FileWriter writer = new FileWriter(it+" "+r.nextInt()+"Additive"+".txt");

			for (int i = 0; i < percentages.length - 1; i++) {
				writer.write(percentages[i] + ",");
			}
			writer.write(percentages[percentages.length - 1] + "\n");

			for (int j = 0; j < data.get(0).size(); j++) {
				for (int i = 0; i < percentages.length - 1; i++) {
					writer.write(data.get(i).get(j) + ",");
				}
				writer.write(data.get(percentages.length - 1).get(j) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Computes the distance of the shortest path found between two points, this
	 * calculation is done {iterations} times and with certain variations to the
	 * heuristic function.
	 * 
	 * @param percentages For every element we have a new case we calculate in every
	 *                    iteration. The values represent how much the heuristic
	 *                    function can be exceeded by h' <= h*(1+value)
	 * @param iterations
	 * @param graf
	 */
	static void computeGrafic(Integer start, Integer end, Double[] percentages, String mode, HashMap<Integer, Node> graf) {

		ArrayList<BackTracking> path;

		/*
		 * Data structure to store all the results
		 */
		ArrayList<ArrayList<Double>> data = new ArrayList<>();

		/*
		 * Initializing "data"
		 */
		for (int i = 0; i < percentages.length; i++) {
			data.add(new ArrayList<Double>());
		}

		/*
		 * Computing the results...
		 */

		path = Solve.find(graf, start, end);
		if (path == null) {
		} else {
			data.get(0).add(graf.get(end).g);
		}

		for (int i = 1; i < percentages.length; i++) {
			System.out.println(i);
			path = Solve.findAlternative(graf, start, end, mode, percentages[i],null);
			if (path == null) {
				data.get(i).add(-1.);
			} else {
				data.get(i).add(graf.get(end).g);
				try {
					FileWriter writer = new FileWriter("GironaLugo"+i+".txt");
					for(int j=0;j<path.size();j++) {
						writer.write(graf.get(path.get(j).id).toString());
					}
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/*
		 * Printing the results to a .txt file.
		 */
		try {
			Random r = new Random();
			FileWriter writer = new FileWriter(" "+r.nextInt()+"Additive"+".txt");

			for (int i = 0; i < percentages.length - 1; i++) {
				writer.write(percentages[i] + ",");
			}
			writer.write(percentages[percentages.length - 1] + "\n");

			for (int j = 0; j < data.get(0).size(); j++) {
				for (int i = 0; i < percentages.length - 1; i++) {
					writer.write(data.get(i).get(j) + ",");
				}
				writer.write(data.get(percentages.length - 1).get(j) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}