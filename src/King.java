public class King extends Piece {
	public final int[] VALUE_GRADIENT = {
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-20,-30,-30,-40,-40,-30,-30,-20,
			-10,-20,-20,-20,-20,-20,-20,-10,
			 20, 20,  0,  0,  0,  0, 20, 20,
			 20, 30, 10,  0,  0, 10, 30, 20};
	
	private boolean isFirstMove;
	
	public King(int t, int r, int c, int w) {
		super("files/king", t, r, c, w);
		isFirstMove = true;
	}
	
	public boolean isFirstMove() {
		return isFirstMove;
	}
	
	public void setIsFirstMove(boolean b) {
		isFirstMove = b;
	}
	
	public int getValue() {
		//reorient row to flip so that black matches gradient map above
		int row = getRow();
		if (isBlack(getId())) {
			row = 7-row;
		}
		int gradient = VALUE_GRADIENT[8*row + getCol()];
		return (1-(getType()*2))*(20000 + gradient);
	}
	
	public int getId() {
		return Piece.KING_W + (6 * getType());
	}
	
	public Piece clone() {
		int width = getWidth();
		return new King(getType(), getRow(), getCol(), width);
	}
	
	public boolean canMove(int row, int col, int[][] board) {
		int cr = getRow();
		int cc = getCol();
		return canMove(isFirstMove, cr, cc, row, col, board);
	}
	
	public static boolean canMove(boolean firstMove, int sr, int sc, int tr, int tc, int[][] board) {
		int from = board[sc][sr];
		int to = board[tc][tr];
		//if king moves one spot in any direction
		if (!isSameColor(from, to) && Math.abs(sr - tr) <= 1 && Math.abs(sc - tc) <= 1) {
			return true;
		}
		return canCastle(firstMove, sr, sc, tr, tc, board);
	}
	
	public static boolean canCastle(boolean firstMove, int sr, int sc, int tr, int tc, int[][] board) {
		if (firstMove && sr == tr && Math.abs(tc - sc) == 2) {
			//king hasn't castled yet, so need to consider case when moves two spots
			int id = board[sc][sr];
			int colDir = (sc - tc) / Math.abs(tc - sc); // - if short castle
			int row = 0;
			int startCol = (int)(3.5 + (-colDir*3.5)); // 7 if short castle, 0 otherwise
			if (isWhite(id)) {
				row = 7;
			}
			startCol += colDir;
			
			while (startCol != 4) {
				//no piece can be in the way
				if (board[startCol][row] != Piece.EMPTY) {
					return false;
				}
				startCol += colDir;
			}
			if (tc > sc) {
				//castle short
				
				if (isWhite(id) && board[7][7] == Piece.ROOK_W) {
					return true;
				}
				if (isBlack(id) && board[7][0] == Piece.ROOK_B) {
					return true;
				}
			} else {
				//castle long
				if (isWhite(id) && board[0][7] == Piece.ROOK_W) {
					return true;
				}
				if (isBlack(id) && board[0][0] == Piece.ROOK_B) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String toString() {
		return "king";
	}
}
