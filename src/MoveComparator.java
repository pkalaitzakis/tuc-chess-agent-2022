package myPackage;

import java.util.Comparator;

	public class MoveComparator implements Comparator<Move> {
		public int compare(Move move1, Move move2) {
            if (move1.getValue() > move2.getValue()) {
                return -1;
            }
            if (move1.getValue() < move2.getValue()) {
                return 1;
            }
            return 0;
        }
    } 