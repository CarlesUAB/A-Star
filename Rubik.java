import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Rubik {

	/**
	 * Creates a graph containing all the possible states of the rubik's 2x2 cube.
	 * Every node represents a different state and every connection between nodes a
	 * movement.
	 * 
	 * @return
	 */
	static HashMap<Rubik2Id, Rubik2Node> buildGraf() {

		HashSet<Rubik2Id> remainingSet = new HashSet<>();
		HashMap<Integer, Rubik2Id> remaining = new HashMap<>();
		HashMap<Rubik2Id, Rubik2Node> graf = new HashMap<>();
		Rubik2Id id = new Rubik2Id(Rubik2Id.POSITIONSSOLVED, Rubik2Id.ORIENTATIONSSOLVED);

		remaining.put(0, id);
		remainingSet.add(id);
		int i = 0;
		while (i < remainingSet.size()) {
			id = remaining.get(i);
			graf.put(id, new Rubik2Node(id, remaining, remainingSet));
			i++;
		}
		return graf;
	}
	
	static void compute(Double[] percentages, String mode,int iterations, HashMap<Rubik2Id, Rubik2Node> graf, Rubik2Id end, HashMap<Rubik2Id,Integer> heuristicMap) {

		Random r = new Random();
		int it = iterations;
		Rubik2Id[] nodes = graf.keySet().toArray(new Rubik2Id[0]);
		Rubik2Id start;


		ArrayList<Rubik2Backtracking> path;

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

			while (graf.get(start).getConnections() == null || graf.get(start).getConnections().size() == 0
					|| graf.get(end).getConnections() == null || graf.get(end).getConnections().size() == 0) {
				start = nodes[r.nextInt(nodes.length)];
				System.out.println("Apa");
			}

			path = Solve.findAlternative(graf, start, end, Rubik2Node.SUMA, 0., heuristicMap);
			if (path == null) {
				continue;
			} else {
				data.get(0).add(graf.get(end).g);
			}

			for (int i = 1; i < percentages.length; i++) {
				System.out.println(iterations + "." + i);
				path = Solve.findAlternative(graf, start, end, mode, percentages[i], heuristicMap);
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
			FileWriter writer = new FileWriter(it+" "+r.nextInt()+"RubikSuma"+".txt");

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
