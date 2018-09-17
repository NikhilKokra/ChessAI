public class Rook extends Piece {
	public final int[] VALUE_GRADIENT = {
			0,  0,  0,  0,  0,  0,  0,  0,
			  5, 10, 10, 10, 10, 10, 10,  5,
			 -5,  0,  0,  0,  0,  0,  0, -5,
			 -5,  0,  0,  0,  0,  0,  0, -5,
			 -5,  0,  0,  0,  0,  0,  0, -5,
			 -5,  0,  0,  0,  0,  0,  0, -5,
			 -5,  0,  0,  0,  0,  0,  0, -5,
			  0,  0,  0,  5,  5,  0,  0,  0};
	
	public Rook(int t, int r, int c, int w) {
		super("files/rook", t, r, c, w);
	}
	
	public int getValue() {
		//reorient row to flip so that black matches gradient map above
		int row = getRow();
		if (isBlack(getId())) {
			row = 7-row;
		}
		int gradient = VALUE_GRADIENT[8*row + getCol()];
		return (1-(getType()*2))*(500 + gradient);
	}
	
	public int getId() {
		return Piece.ROOK_W + (6 * getType());
	}
	
	public Piece clone() {
		int width = getWidth();
		return new Rook(getType(), getRow(), getCol(), width);
	}
	
	public boolean canMove(int row, int col, int[][] board) {
		int cr = getRow();
		int cc = getCol();
		return canMove(cr, cc, row, col, board);
	}
	
	public static boolean canMove(int sr, int sc, int tr, int tc, int[][] board) {
		int from = board[sc][sr];
		int to = board[tc][tr];
		if (!isSameColor(from, to)) {
			int rowIncr = 0;
			int colIncr = 0;
			if (sc == tc) {
				rowIncr = -(sr -  tr) / Math.abs(sr - tr);
			} else if (sr == tr) {
				colIncr = (tc -  sc) / Math.abs(tc - sc);
			} else {
				return false;
			}
			int riter = sr + rowIncr;
			int citer = sc + colIncr;
			while (citer != tc || riter != tr) {
				if (board[citer][riter] != Piece.EMPTY) {
					return false;
				}
				riter += rowIncr;
				citer += colIncr;
			}
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "rook";
	}
}
