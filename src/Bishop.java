public class Bishop extends Piece {
	public final int[] VALUE_GRADIENT = {
			-20,-10,-10,-10,-10,-10,-10,-20,
			-10,  0,  0,  0,  0,  0,  0,-10,
			-10,  0,  5, 10, 10,  5,  0,-10,
			-10,  5,  5, 10, 10,  5,  5,-10,
			-10,  0, 10, 10, 10, 10,  0,-10,
			-10, 10, 10, 10, 10, 10, 10,-10,
			-10,  5,  0,  0,  0,  0,  5,-10,
			-20,-10,-10,-10,-10,-10,-10,-20};
	
	public Bishop (int t, int r, int c, int w) {
		super("files/bishop", t, r, c, w);
	}
	
	public int getValue() {
		//reorient row to flip so that black matches gradient map above
		int row = getRow();
		if (isBlack(getId())) {
			row = 7-row;
		}
		int gradient = VALUE_GRADIENT[8*row + getCol()];
		return (1-(getType()*2))*(330 + gradient);
	}
	
	public int getId() {
		return Piece.BISHOP_W + (6 * getType());
	}
	
	public Piece clone() {
		int width = getWidth();
		return new Bishop(getType(), getRow(), getCol(), width);
	}
	
	public boolean canMove(int row, int col, int[][] board) {
		int cr = getRow();
		int cc = getCol();
		return canMove(cr, cc, row, col, board);
	}
	
	public static boolean canMove(int sr, int sc, int tr, int tc, int[][] board) {
		int from = board[sc][sr];
		int to = board[tc][tr];
		if (Math.abs(sr - tr) == Math.abs(sc - tc) && !isSameColor(from, to)) {
			int r = (tr-sr)/Math.abs(sr-tr); // +-1
			int c = (tc-sc)/Math.abs(sc-tc); // +-1
			int riter = sr + r;
			int citer = sc + c;
			while (riter != tr && citer != tc) {
				if (board[citer][riter] != Piece.EMPTY) {
					return false;
				}
				riter += r;
				citer += c;
			}
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "bishop";
	}
}
