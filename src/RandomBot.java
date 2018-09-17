import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomBot implements AI {
	private Board b;
	
	public RandomBot(Board b) {
		this.b = b;
	}
	
	public Move getMove(int turn) {
		if (turn == 0) {
			return whiteTurn();
		} else {
			return blackTurn();
		}
	}
	
	public Move whiteTurn() {
		Set<Move> moves = b.possibleMoves();
		if (moves.size() != 0) {
			Random rnd = new Random();
			int i = rnd.nextInt(moves.size());
			return (Move)moves.toArray()[i];
		}
		return null;
	}
	
	public Move blackTurn() {
		Set<Move> moves = b.possibleMoves();
		if (moves.size() != 0) {
			Random rnd = new Random();
			int i = rnd.nextInt(moves.size());
			return (Move)moves.toArray()[i];
		}
		return null;
	}
}
