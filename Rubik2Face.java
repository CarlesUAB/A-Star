
public class Rubik2Face {

	/**
	 * Stores the color of every tile of the face. 0: WHITE, 1: GREEN, 2: RED,
	 * 3:YELLOW, 4: BLUE, 5: ORANGE
	 */
	byte[] e = new byte[4];

	@Override
	public String toString() {
		return "FACE: " + e[0] + " " + e[1] + " " + e[2] + " " + e[3] + " ";
	}
}
