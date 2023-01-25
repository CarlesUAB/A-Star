import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {

	public static void main(String args[]) {

		/* MAP OF SPAIN */
		/*
		 * HashMap<Integer, Node> graf =
		 * Map.buildGraph("C:\\Users\\Carles\\Documents\\Mates\\TFG\\spain_ascii.csv");
		 * 
		 * Integer start = 771979683; Integer end = 698734221; ArrayList<BackTracking>
		 * path = Solve.find(graf, start, end);
		 * System.out.println("FOUND PATH WITH LENGTH: " + graf.get(end).g +
		 * " meters."); // for (int i = 0; i < path.size(); i++) { //
		 * System.out.print(path.get(i)); // }
		 */

		/* 2x2x2 RUBIK CUBE */
		HashMap<Rubik2Id, Rubik2Node> graf = Rubik.buildGraf();

		byte[] pos = { 0, 2, 1, 3, 4, 5, 6, 7 };
		byte[] ori = { 0, 0, 0, 0, 0, 0, 0, 0 };

		Rubik2Id start = new Rubik2Id(pos, ori);
		Rubik2Id end = new Rubik2Id(Rubik2Id.POSITIONSSOLVED, Rubik2Id.ORIENTATIONSSOLVED);

		// Solving with h=0;
		Solve.find(graf, start, end);

		// We read the heuristic document.
		Rubik2Id[] nodes = graf.keySet().toArray(new Rubik2Id[0]);
		HashMap<Rubik2Id, Integer> heuristicMap = new HashMap<>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream("heuristic.txt"), StandardCharsets.US_ASCII));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			for (int i = 0; i < nodes.length; i++) {
				heuristicMap.put(nodes[i], Integer.valueOf(br.readLine()));
			}
			br.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Solving with h=h*
		Solve.findAlternative(graf, start, end, Rubik2Node.MULT, 0., heuristicMap);
	}
}

//	public static void main(String args[]) {
//
//		long t0 = System.nanoTime();
//		HashMap<Rubik2Id, Rubik2Node> graf = Rubik.buildGraf();
//		long t1 = System.nanoTime();
//		System.out.println("GRAPH FINISHED - SIZE: " + graf.size() + " IN " + (t1 - t0) + " NANOSECONDS "
//				+ (t1 - t0) / 1000000000);
//
//		byte[] pos = { 0, 2, 1, 3, 4, 5, 6, 7 };
//		byte[] ori = { 0, 0, 0, 0, 0, 0, 0, 0 };
//
//		Rubik2Id start = new Rubik2Id(pos, ori);
//		Rubik2Id end = new Rubik2Id(Rubik2Id.POSITIONSSOLVED, Rubik2Id.ORIENTATIONSSOLVED);
//
//		t0 = System.nanoTime();
//		ArrayList<Rubik2Backtracking> path = Solve.find(graf, start, end);
//		t1 = System.nanoTime();
//		System.out.println("PATH OF " + path.size() + " MOVEMENTS FOUND IN " + (t1 - t0) + " NANOSECONDS "
//				+ (t1 - t0) / 1000000000);
//
//		for (int i = 0; i < path.size(); i++) {
//			System.out.print(path.get(i));
//		}
//	}
//}
