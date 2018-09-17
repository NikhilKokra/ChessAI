public class Queen extends Piece {
	public final int[] VALUE_GRADIENT = {
			-20,-10,-10, -5, -5,-10,-10,-20,
			-10,  0,  0,  0,  0,  0,  0,-10,
			-10,  0,  5,  5,  5,  5,  0,-10,
			 -5,  0,  5,  5,  5,  5,  0, -5,
			  0,  0,  5,  5,  5,  5,  0, -5,
			-10,  5,  5,  5,  5,  5,  0,-10,
			-10,  0,  5,  0,  0,  0,  0,-10,
			-20,-10,-10, -5, -5,-10,-10,-20};
	
	public Queen(int t, int r, int c, int w) {
		super("files/queen", t, r, c, w);
	}
	
	public int getValue() {
		//reorient row to flip so that black matches gradient map above
		int row = getRow();
		if (isBlack(getId())) {
			row = 7-row;
		}
		int gradient = VALUE_GRADIENT[8*row + getCol()];
		return (1-(getType()*2))*(900 + gradient);
	}
	
	public int getId() {
		return Piece.QUEEN_W + (6 * getType());
	}
	
	public Piece clone() {
		int width = getWidth();
		return new Queen(getType(), getRow(), getCol(), width);
	}
	
	public boolean canMove(int row, int col, int[][] board) {
		int cr = getRow();
		int cc = getCol();
		return canMove(cr, cc, row, col, board);
	}
	
	public static boolean canMove(int sr, int sc, int tr, int tc, int[][] board) {
		return Bishop.canMove(sr, sc, tr, tc, board) || Rook.canMove(sr, sc, tr, tc, board);
	}
	
	public String toString() {
		return "queen";
	}
}
