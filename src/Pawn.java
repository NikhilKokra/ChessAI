import java.util.Set;
import java.util.TreeSet;

public class Pawn extends Piece {
	public final int[] VALUE_GRADIENT = {
			0,  0,  0,  0,  0,  0,  0,  0,
			50, 50, 50, 50, 50, 50, 50, 50,
			10, 10, 20, 30, 30, 20, 10, 10,
			 5,  5, 10, 25, 25, 10,  5,  5,
			 0,  0,  0, 20, 20,  0,  0,  0,
			 5, -5,-10,  0,  0,-10, -5,  5,
			 5, 10, 10,-20,-20, 10, 10,  5,
			 0,  0,  0,  0,  0,  0,  0,  0};
	private boolean firstMove;
	
	public Pawn(int t, int r, int c, int w) {
		super("files/pawn", t, r, c, w);
		firstMove = true;
	}
	
	public int getValue() {
		//reorient row to flip so that black matches gradient map above
		int row = getRow();
		if (isBlack(getId())) {
			row = 7-row;
		}
		int gradient = VALUE_GRADIENT[8*row + getCol()];
		return (1-(getType()*2))*(100 + gradient);
	}
	
	public void setFirstMove(boolean b) {
		firstMove = b;
	}
	
	public int getId() {
		return Piece.PAWN_W + (6 * getType());
	}
	
	public Piece clone() {
		int width = getWidth();
		return new Pawn(getType(), getRow(), getCol(), width);
	}
	
	public boolean canMove(int row, int col, int[][] board) {
		int cr = getRow();
		int cc = getCol();
		return canMove(firstMove, cr, cc, row, col, board);
	}
	
	public static boolean canMove(boolean firstMove, int sr, int sc, int tr, int tc, int[][] board) {
		int from = board[sc][sr];
		int to = board[tc][tr];
		if (!isSameColor(from, to) && (int)(tc-sc)/2 == 0) {
			int dir = 0;
			if (isWhite(from)) {
				dir = -1;
			} else if (isBlack(from)) {
				dir = 1;
			}
			if (firstMove && tc == sc && board[tc][sr+dir] == Piece.EMPTY && sr-tr == dir*-2) {
				return board[tc][tr] == Piece.EMPTY;
			} else {
				if (sr-tr == dir*-1) {
					return ((Math.abs(tc - sc) == 1) && (to != Piece.EMPTY)) || 
							(Math.abs(tc - sc) == 0 && to == Piece.EMPTY);
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	public String toString() {
		return "pawn";
	}
}
