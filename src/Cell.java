import java.awt.Color;
import java.awt.Graphics;

public class Cell {
	public static final double TINT = .6;
	private Piece p;
	private int w;//if c=0, then white square
	private Color c;
	private boolean toggled;
	private int row, col;
	
	
	public Cell(Piece p, int w, int row, int col) {
		this.p = p;
		this.w = w;
		if ((row+col)%2 == 1) {
			this.c = new Color(255,178,102);
		} else {
			this.c = new Color(153,76,0);
		}
		this.toggled = false;
		this.row = row;
		this.col = col;
	}
	
	public void draw(Graphics g) {
		g.setColor(c);
		g.fillRect(col*w, row*w, w, w);
		//System.out.println("filled square");
		if (p != null) {
			p.draw(g);
			//System.out.println("drew piece");
		}
	}
	
	public void press() {
		if (!toggled) {
			toggled = true;
			c = new Color((int)(c.getRed()*TINT), (int)(c.getGreen()*TINT), (int)(c.getBlue()*TINT));
		}
	}
	
	public void unpress() {
		if (toggled) {
			toggled = false;
			c = new Color((int)(c.getRed()/TINT), (int)(c.getGreen()/TINT), (int)(c.getBlue()/TINT));
		}
	}
	
	public boolean isToggled() {
		return toggled;
	}
	
	public void put(Piece p) {
		if (p != null) {
			//System.out.println("set position of " + p.toString() + " to " + row + "," + col);
			p.setPos(row, col);
		}
		this.p = p;
	}
	
	public Piece getPiece() {
		if (p != null) {
			return p;
		}
		return null;
	}
	
	public boolean occupied() {
		return p != null;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public String toString() {
		if (p != null) {
			return p.toString();
		}
		return "empty";
	}
	
	public boolean equals(Object o) {
		if (o != null && o instanceof Cell) {
			Cell cell = (Cell)o;
			return cell.getCol() == col && cell.getRow() == row;
		}
		return false;
	}
}
