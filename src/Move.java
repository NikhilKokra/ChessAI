
public class Move {
	private Cell from, to;
	private Piece moved, captured;
	private Board b;
	
	public Move(Cell from, Cell to, Piece moved, Board b) {
		this.from = from;
		this.to = to;
		this.moved = moved;
		this.captured = to.getPiece();
		this.b = b;
	}
	
	public void execute() {
		//transfers from "from" to "to"
		b.put(to.getRow(), to.getCol(), moved);
		b.put(from.getRow(), from.getCol(), null);
		//edge cases: enpeseante, castling
		if (moved instanceof Pawn) {
			Pawn p = (Pawn)moved;
			p.setFirstMove(false);
			if (to.getRow() % 7 == 0) {
				b.put(to.getRow(), to.getCol(), new Queen(moved.getType(), to.getRow(), to.getCol(), Board.cellWidth));
			}
		}
		if (moved instanceof King) {
			King k = (King)moved;
			//if, on this move execution, the king has castled
			if (Math.abs(to.getCol() - from.getCol()) == 2) {
				//need to update rook location
				updateRookOnCastle();
			}
			k.setIsFirstMove(false);
		}
	}
	
	public void undo() {
		//transfers from "to" to "from"
		b.put(from.getRow(), from.getCol(), moved);
		b.put(to.getRow(), to.getCol(), captured);
		if (moved instanceof Pawn) {
			//if undo pawn moving forward 2 squares, need to reset pawn firstmove state
			Pawn p = (Pawn)moved;
			if ((from.getRow() == 1 && Piece.isBlack(moved.getId())) 
					|| (from.getRow() == 6 && Piece.isWhite(moved.getId()))) {
				p.setFirstMove(true);
			}
		}
		if (moved instanceof King) {
			King k = (King)moved;
			//if, on this undo, the king castled, need to update "Castled" state of king
			if (Math.abs(to.getCol() - from.getCol()) == 2) {
				//need to update rook location
				updateRookOnUnCastle();
			}
			k.setIsFirstMove(true);
		}
	}
	
	public boolean isValid() {
		if (moved != null) {
			return moved.canMove(to.getRow(), to.getCol(), b.getState());
		}
		return false;
	}
	
	public void updateRookOnCastle() {
		//update rook based on from/to
		if (to.getCol() == 6) {//castled short
			b.put(to.getRow(), 5, b.get(to.getRow(), 7).getPiece());
			b.put(to.getRow(), 7, null);
		} else if (to.getCol() == 2) {//castled long
			b.put(to.getRow(), 3, b.get(to.getRow(), 0).getPiece());
			b.put(to.getRow(), 0, null);
		}
	}
	
	public void updateRookOnUnCastle() {
		if (to.getCol() == 6) {//castled short
			b.put(to.getRow(), 7, b.get(to.getRow(), 5).getPiece());
			b.put(to.getRow(), 5, null);
		} else if (to.getCol() == 2) {//castled long
			b.put(to.getRow(), 0, b.get(to.getRow(), 3).getPiece());
			b.put(to.getRow(), 3, null);
		}
	}
	
	public String toString() {
		String cap = "none";
		if (captured != null) {
			cap = captured.toString();
		}
		return moved.toString() + "(" + from.getRow() + "," + from.getCol() + ")->" 
		+ cap + "(" + to.getRow() + "," + to.getCol() + ")";
	}
}
