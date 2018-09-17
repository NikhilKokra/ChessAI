import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

public abstract class Piece {
	public static final int EMPTY = 0;
	public static final int PAWN_W = 1;
	public static final int ROOK_W = 2;
	public static final int KNIGHT_W = 3;
	public static final int BISHOP_W = 4;
	public static final int QUEEN_W = 5;
	public static final int KING_W = 6;
	public static final int PAWN_B = 7;
	public static final int ROOK_B = 8;
	public static final int KNIGHT_B = 9;
	public static final int BISHOP_B = 10;
	public static final int QUEEN_B = 11;
	public static final int KING_B = 12;
	private BufferedImage img;
	private int x, y, row, col, width, type, id;
	
	//t is the type: white(0) or black(1)
	public Piece (String path, int t, int r, int c, int w) {
		this.id = t;
		this.type = t;
		x = c*w;
		y = r*w;
		row = r;
		col = c;
		width = w;
		if (t == 1) {
			path += "_b.png";
		} else {
			path += "_w.png";
		}
		img = getPicture(path);
	}
	
	public BufferedImage getPicture(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (image == null) {
			System.out.println("File for chess image wasn't found");
		}
		return image;
	}
	
	public void setPos(int row, int col) {
		this.x = col*width;
		this.y = row*width;
		this.row = row;
		this.col = col;
	}
	
	public void setCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g) {
		int pad = 10;
		g.drawImage(img, x+pad, y+pad, x+width-pad, y+width-pad, 0, 0, img.getWidth(), img.getHeight(), null);
	}
	
	public int getType() {
		return type;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean isType(int type) {
		return (type == 0 && isWhite(id)) || (type == 1 && isBlack(id));
	}
	
	public static boolean isSameColor(int id1, int id2) {
		return (isWhite(id1) && isWhite(id2)) || (isBlack(id1) && isBlack(id2));
	}
	
	public static boolean isWhite(int id) {
		return id != 0 && id < 7;
	}
	
	public static boolean isBlack(int id) {
		return id != 0 && id >= 7;
	}
	
	public Set<Move> getPossibleMoves(Board b) {
		int[][] state = b.getState();
		int cr = getRow();
		int cc = getCol();
		Set<Move> moves = new HashSet<Move>();
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (canMove(r, c, state)) {
					moves.add(new Move(b.get(cr, cc), b.get(r, c), b.get(cr, cc).getPiece(), b));
				}
			}
		}
		return moves;
	}
	
	public abstract int getValue();
	
	public abstract int getId();
	
	public abstract boolean canMove(int row, int col, int[][] board);
	
	public abstract Piece clone();
	
	public abstract String toString();
}
