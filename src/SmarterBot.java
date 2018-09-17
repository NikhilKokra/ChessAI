import java.util.Set;

public class SmarterBot implements AI {
	private final int DEPTH = 3;
	private Board b;
	private Move bestMove;
	
	public SmarterBot(Board b) {
		this.b = b;
		bestMove = null;
	}
	
	public Move getMove(int turn) {
		minimax(turn, DEPTH);
		b.setTurn(turn);
		return bestMove;
	}
	
	public int minimax(int turn, int depth) {
		b.setTurn(turn);
		Set<Move> moves = b.possibleMoves();
		if (depth == 0 || moves.size() == 0) {
			return evaluateBoard();
		}
		
		if (turn == 1) { //is minimizing player
			int bestScore = Integer.MAX_VALUE;
			for (Move m: moves) {
				m.execute();
				int score = minimax((turn+1)%2, depth-1);
				m.undo();
				if (score < bestScore) {
					bestScore = score;
					if (depth == DEPTH) {
						bestMove = m;
					}
				}
			}
			return bestScore;
		} else { //is maximizing player
			int bestScore = Integer.MIN_VALUE;
			for (Move m: moves) {
				m.execute();
				int score = minimax((turn+1)%2, depth - 1);
				m.undo();
				
				if (score > bestScore) {
					bestScore = score;
					if (depth == DEPTH) {
						bestMove = m;
					}
				}
			}
			return bestScore;
		}
	}
	
	//negative is better for black
	public int evaluateBoard() {
		int score = 0;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Piece p = b.get(r, c).getPiece();
				if (p != null) {
					score += p.getValue();
				}
			}
		}
		return score;
	}
}
