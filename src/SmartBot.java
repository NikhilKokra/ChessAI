import java.util.Set;

public class SmartBot implements AI {
	private Board b;
	private int turn; //-1 is white 1 is black
	
	public SmartBot(Board b) {
		this.b = b;
	}
	
	public Move getMove(int turn) {
		Set<Move> moves = b.possibleMoves();
		int bestScore = Integer.MAX_VALUE * (turn*2 - 1); //want this to be negative if black, else positive
		Move bestMove = null;
		
		for (Move m: moves) {
			m.execute();
			int score = evaluateBoard();
			m.undo();
			if (turn == 1 && score < bestScore) {
				System.out.println(score);
				bestScore = score;
				bestMove = m;
			}
			if (turn == 0 && score > bestScore) {
				System.out.println(score);
				bestScore = score;
				bestMove = m;
			}
		}
		
		return bestMove;
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
