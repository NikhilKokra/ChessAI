public class Knight extends Piece {
	public final int[] VALUE_GRADIENT = {
			-50,-40,-30,-30,-30,-30,-40,-50,
			-40,-20,  0,  0,  0,  0,-20,-40,
			-30,  0, 10, 15, 15, 10,  0,-30,
			-30,  5, 15, 20, 20, 15,  5,-30,
			-30,  0, 15, 20, 20, 15,  0,-30,
			-30,  5, 10, 15, 15, 10,  5,-30,
			-40,-20,  0,  5,  5,  0,-20,-40,
			-50,-40,-30,-30,-30,-30,-40,-50};

	public Knight (int t, int r, int c, int w) {
		super("files/knight", t, r, c, w);
	}
	
	public int getValue() {
		//reorient row to flip so that black matches gradient map above
		int row = getRow();
		if (isBlack(getId())) {
			row = 7-row;
		}
		int gradient = VALUE_GRADIENT[8*row + getCol()];
		return (1-(getType()*2))*(320 + gradient);
	}
	
	public int getId() {
		return Piece.KNIGHT_W + (6 * getType());
	}
	
	public Piece clone() {
		int width = getWidth();
		return new Knight(getType(), getRow(), getCol(), width);
	}
	
	public boolean canMove(int row, int col, int[][] board) {
		int cr = getRow();
		int cc = getCol();
		return canMove(cr, cc, row, col, board);
	}
	
	public static boolean canMove(int sr, int sc, int tr, int tc, int[][] board) {
		int from = board[sc][sr];
		int to = board[tc][tr];
		//System.out.println(isSameColor(from, to));
		return !isSameColor(from, to) && ((Math.abs(tr-sr)==1 && Math.abs(tc-sc)==2) 
									|| (Math.abs(tr-sr)==2 && Math.abs(tc-sc)==1));
	}
	
	public String toString() {
		return "knight";
	}
}
