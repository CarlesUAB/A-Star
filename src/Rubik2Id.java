public class Rubik2Id {

	/**
	 * Stores the positions and orientations configuration for the solved rubik 2x2
	 * cube
	 */
	static final byte[] POSITIONSSOLVED = { 0, 1, 2, 3, 4, 5, 6, 7 };
	static final byte[] ORIENTATIONSSOLVED = { 0, 0, 0, 0, 0, 0, 0, 0 };

	/**
	 * Stores the corner number in every position. positions[0] is the number of the
	 * corner in position 0.
	 */
	byte[] positions = new byte[8];

	/**
	 * Stores the corner orientation. Reading the tiles in the order specified in
	 * Rubik2Node, orientation is the position that contains the minimum face
	 */
	byte[] orientations = new byte[8];

	/**
	 * Creates an id cloning the positions and orientations given
	 * 
	 * @param positions
	 * @param orientations
	 */
	Rubik2Id(byte[] positions, byte[] orientations) {
		this.positions = positions.clone();
		this.orientations = orientations.clone();
	}

	/**
	 * Creates an id from a node, calculating the position and orientation of every
	 * corner
	 * 
	 * @param n node to compute id from
	 */
	Rubik2Id(Rubik2Node n) {

		byte[] buffer = new byte[3];
		byte min;
		byte minIndex;
		for (int i = 0; i < 8; i++) {
			min = 10;
			minIndex = -1;
			for (byte j = 0; j < 3; j++) {
				buffer[j] = n.faces[Rubik2Node.FACENUMBER[i][j]].e[Rubik2Node.FACEPOSITION[i][j]];
				if (buffer[j] < min) {
					min = buffer[j];
					minIndex = j;
				}
			}
			orientations[i] = minIndex;
			positions[i] = assignPosition(buffer);
		}
	}

	/**
	 * Identifies which corner has the tiles of buffer.
	 * 
	 * @param buffer list of three tiles
	 * @return a number between 0 and 7 indicating the corner number.
	 */
	byte assignPosition(byte[] buffer) {
		int prod = (buffer[0] + 1) * (buffer[1] + 1) * (buffer[2] + 1);
		byte result = 100;
		switch (prod) {
		case 12:
			result = 0;
			break;
		case 6:
			result = 1;
			break;
		case 15:
			result = 2;
			break;
		case 30:
			result = 3;
			break;
		case 48:
			result = 4;
			break;
		case 24:
			result = 5;
			break;
		case 60:
			result = 6;
			break;
		case 120:
			result = 7;
			break;
		}
		return result;
	}

//	boolean verifyCorner(byte[] buffer, byte j) {
//		for (int i = 0; i < 3; i++) {
//			if (!contains(buffer, Rubik2Node.FACENUMBER[j][i])) {
//				return false;
//			}
//		}
//		return true;
//	}

//	boolean contains(byte[] buffer, byte b) {
//		for (int i = 0; i < 3; i++) {
//			if (buffer[i] == b) {
//				return true;
//			}
//		}
//		return false;
//	}

//	void checkId() {
//		for (int i = 0; i < 8; i++) {
//			System.out.println(i + ": POSITION: " + positions[i] + " ORIENTATION: " + orientations[i]);
//		}
//
//		byte buffer = 0;
//		for (int i = 0; i < 8; i++) {
//			buffer += positions[i];
//		}
//		byte sum = 28;
//		if (buffer != sum) {
//			System.out.println("WRONG POSITIONS ASIGNED");
//			System.exit(-1);
//		}
//		for (int i = 0; i < 8; i++) {
//			if (orientations[i] < 0 || orientations[i] > 2) {
//				System.out.println("WRONG ORIENTATIONS ASIGNED");
//				System.exit(-1);
//				this.equals(ORIENTATIONSSOLVED);
//			}
//		}
//	}

	@Override
	public boolean equals(Object o) {
		Rubik2Id id2 = (Rubik2Id) o;
		for (int i = 0; i < 8; i++) {
			if (!((orientations[i] == id2.orientations[i]) && (positions[i] == id2.positions[i]))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int buffer = 0;
		for (int i = 0; i < 7; i++) {
			buffer += positions[i] * ((int) Math.pow(7, i));
		}
		int buffer2 = 0;
		for (int i = 0; i < 7; i++) {
			buffer2 += orientations[i] * ((int) Math.pow(3, i));
		}
		return buffer2 == 0 ? buffer : buffer * buffer2;
	}

	@Override
	public String toString() {
		String s = "ID. POSITIONS: \n";
		for (int i = 0; i < 8; i++) {
			s = s + positions[i] + ", ";
		}
		s = s + "\nORIENTATIONS: \n";
		for (int i = 0; i < 8; i++) {
			s = s + orientations[i] + ", ";
		}
		s = s + "\n";
		return s;
	}

}
