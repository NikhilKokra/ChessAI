import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel {
	private static final int HUMAN_PLAYER = 0, AI_PLAYER = 1;
	public static final int cellWidth = 60;
	private Cell[][] cells;
	private Piece preview;
	public Cell selected;
	public LinkedList<Move> moves;
	private int[][] state;
	private int[] players;
	private AI bot;
	private int turn;
	private boolean mouseEnabled;
	private JLabel status, aistatus;
	private JComboBox<String>[] playerMenus;
	
	public Board (JLabel status, JComboBox<String> p1, JComboBox<String> p2, JLabel aistatus) {
		playerMenus = new JComboBox[2];
		playerMenus[0] = p1;
		playerMenus[1] = p2;
		this.status = status;
		this.aistatus = aistatus;
		this.mouseEnabled = true;
		moves = new LinkedList<Move>();
		this.preview = null;
		turn = 0;
		setPlayers();
        setFocusable(true);
        addMouseListener(new mouseEventHandler());
        addMouseMotionListener(new mouseMotionEventHandler());
        reset();
        playerMenus[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (moves.size() == 0) {
					setPlayers();
					String p1 = "Human";
			        String p2 = "Human";
			        if (players[0] == AI_PLAYER) {
			        	p1 = "Bot";
			        }
			        if (players[1] == AI_PLAYER) {
			        	p2 = "Bot";
			        }
			        aistatus.setText(p1 + " vs " + p2);
			        aistatus.paintImmediately(aistatus.getVisibleRect());
					if (players[0] == AI_PLAYER) {
			        	Move m = bot.getMove(turn);
			        	status.setText("Black's turn!");
				        status.paintImmediately(status.getVisibleRect());
						moves.push(m);
						m.execute();
						turn = 1;
			        }
			        repaint();
				}
			}
        });
        
        playerMenus[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (moves.size() == 0) {
					setPlayers();
					String p1 = "Human";
			        String p2 = "Human";
			        if (players[0] == AI_PLAYER) {
			        	p1 = "Bot";
			        }
			        if (players[1] == AI_PLAYER) {
			        	p2 = "Bot";
			        }
			        aistatus.setText(p1 + " vs " + p2);
				}
			}
        });
	}
	
	//update according to state of playermenus
	public void setPlayers() {
		players = new int[2];
		JComboBox<String> p1 = playerMenus[0];
		JComboBox<String> p2 = playerMenus[1];
		if (p1.getSelectedIndex() != 0 && p2.getSelectedIndex() != 0) {
			//change white to human
			p1.setSelectedIndex(0);
		}
		for (int i = 0; i <= 1; i++) {
			switch(playerMenus[i].getSelectedIndex()) {
				case 0: players[i] = HUMAN_PLAYER;
						break;
				case 1: players[i] = AI_PLAYER;
						bot = new RandomBot(this);
						break;
				case 2: players[i] = AI_PLAYER;
						bot = new SmartBot(this);
						break;
				case 3: players[i] = AI_PLAYER;
						bot = new SmarterBot(this);
						break;
				case 4: players[i] = AI_PLAYER;
						bot = new SmartestBot(this);
						break;
			}
		}
	}
	
	public void undo() {
		if (moves.size() == 0) {
			return;
		}
		Move m = popMove();
		m.undo();
		switchTurn(false);
		repaint();
		if (players[turn] == AI_PLAYER) {
			undo();
		}
		
		repaint();
	}
	
	public void clean() {
		cells = new Cell[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				cells[i][j] = new Cell(null, cellWidth, j, i);
			}
		}
		moves.clear();
		state = new int[8][8];
		repaint();
	}
	
	public void reset() {
		clean();
        for (int c = 0; c < 8; c++) {
        	for (int r = 0; r < 8; r++) {
        		//pawns
        		if (r == 1 || r == 6) {
        			put(r, c, new Pawn(1 - r/6, r, c, cellWidth));
        		}
        		//back rank
        		if (r == 0 || r == 7) {
        			if (c % 7 == 0) {
        				put(r, c, new Rook(1 - r/7, r, c, cellWidth));
        			} else if ((c-1) % 5 == 0) {
        				put(r, c, new Knight(1 - r/7, r, c, cellWidth));
        			} else if ((c-2) % 3 == 0) {
        				put(r, c, new Bishop(1 - r/7, r, c, cellWidth));
        			} else {
        				if (c == 3) {
        					put(r, c, new Queen(1 - r/7, r, c, cellWidth));
        				} else {
        					put(r, c, new King(1 - r/7, r, c, cellWidth));
        				}
        			}
        			
        		}
        	}
        }

        turn = 0;
        moves.clear();
        setPlayers();
        if (turn == 1) {
			status.setText("Black's turn!");
		} else {
			status.setText("White's turn!");
		}
        String p1 = "Human";
        String p2 = "Human";
        if (players[0] == AI_PLAYER) {
        	p1 = "Bot";
        }
        if (players[1] == AI_PLAYER) {
        	p2 = "Bot";
        }
        aistatus.setText(p1 + " vs " + p2);
        if (players[0] == AI_PLAYER) {
        	aistatus.setText("Thinking...");
			aistatus.paintImmediately(aistatus.getVisibleRect());
			status.paintImmediately(status.getVisibleRect());
			mouseEnabled = false;
        	Move m = bot.getMove(turn);
        	mouseEnabled = true;
			aistatus.setText("Your turn!");
			status.setText("Black's turn!");
			status.paintImmediately(status.getVisibleRect());
			moves.push(m);
			m.execute();
			turn = 1;
        }
        repaint();
	}
	
	public boolean isTurn(Piece p) {
		return (Piece.isWhite(p.getId()) && turn == 0) 
				|| (Piece.isBlack(p.getId()) && turn == 1);
	}
	
	public void addMove(Move m) {
		moves.addLast(m);
	}
	
	public Move popMove() {
		return moves.pop();
	}
	
	public boolean scanForKing() {
		//reset the board and display victory message "Black has been annihalated! Good game!"
		//return true if detected a missing king
		boolean whiteKing = false;
		boolean blackKing = false;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (state[r][c] == Piece.KING_W) {
					whiteKing = true;
				}
				if (state[r][c] == Piece.KING_B) {
					blackKing = true;
				}
			}
		}
		if (!whiteKing || !blackKing) {
			String message = "";
			if (!whiteKing) {
				message = "Black wins! White was annihalated!";
			} else if (!blackKing) {
				message = "White wins! Black was annihalated!";
			}
			JLabel msg = new JLabel(message);
			msg.setFont(new Font("Arial", Font.PLAIN, 30));
			JPanel pane = new JPanel();
			pane.add(msg);
			JOptionPane.showMessageDialog(null, pane, "Victory!", JOptionPane.PLAIN_MESSAGE);
			reset();
			return true;
		}
		return false;
	}
	
	public void switchTurn(boolean onMove) {
		turn = (turn + 1) % 2;
		if (scanForKing()) {//both kings still alive
			onMove = false;
		}
		if (turn == 1) {
			status.setText("Black's turn!");
		} else {
			status.setText("White's turn!");
		}
		if (players[turn] == AI_PLAYER && onMove) {
			aistatus.setText("Thinking...");
			aistatus.paintImmediately(aistatus.getVisibleRect());
			status.paintImmediately(status.getVisibleRect());
			mouseEnabled = false;
        	Move m = bot.getMove(turn);
        	mouseEnabled = true;
			aistatus.setText("Your turn!");
			moves.push(m);
			m.execute();
			switchTurn(true);
		}
	}
	
	public void setTurn(int t) {
		turn = t;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Set<Move> possibleMoves() {
		Set<Move> moves = new HashSet<Move>();
		for (int col = 0; col < 8; col++) {
			for (int row = 0; row < 8; row++) {
				Piece p = get(row, col).getPiece();
				if (p != null && p.getType() == turn) {
					moves.addAll(p.getPossibleMoves(this));
					
				}
			}
		}
		return moves;
	}
	
	public Cell get(int row, int col) {
		return cells[col][row];
	}
	
	public void put(int row, int col, Piece p) {
		cells[col][row].put(p);
		//String str = "empty";
		if (p != null) {
			state[col][row] = p.getId();
			//str = p.toString();
		} else {
			state[col][row] = Piece.EMPTY;
		}
		//System.out.println(moves);
		//System.out.println("Put piece " + str + " in row,col = (" + row + "," + col + ")");
	}
	
	public int[][] getState() {
		return state;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				cells[r][c].draw(g);
			}
		}
		if (preview != null) {
			preview.draw(g);
		}
		Graphics2D g2 = (Graphics2D) g;
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(10));
		g2.setColor(Color.DARK_GRAY);
		g2.drawRect(0, 0, cellWidth*8, cellWidth*8);
		g2.setStroke(oldStroke);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(cellWidth*8, cellWidth*8);
	}
	
	public String toString() {
		String re = "";
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				re  = re + state[c][r] + " ";
			}
			re += "\n";
		}
		return re;
	}
	
	class mouseEventHandler extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
    		int x = e.getX()/cellWidth;
    		int y = e.getY()/cellWidth;
    		boolean madeMove = false;
    		if (x >= 0 && y >= 0 && x < 8 && y < 8 && mouseEnabled) {
    			Cell currentCell = cells[x][y];
    			if (!currentCell.isToggled()) {
    				if (selected != null && !currentCell.equals(selected)) {
        				selected.unpress();
        				currentCell.press();
    					Move move = new Move(selected, currentCell, selected.getPiece(), Board.this);
        				if (move.isValid() && isTurn(selected.getPiece())) {
        					//if cell i clicked is valid, then transfer
        					moves.push(move);
        					move.execute();
        					selected = null;
        					currentCell.unpress();
        					madeMove = true;
        				} else {
        					selected = currentCell;
        				}
        			} else {
        				//if clicked on a new empty square
        				selected = currentCell;
        				selected.press();
        			}
    			} else {
    				currentCell.unpress();
    				selected = null;
    			}
    		}
    		Board.this.paintImmediately(Board.this.getBounds());
    		if (madeMove) {
    			switchTurn(true);
    		}
    		repaint();
    	}
    	
    	public void mouseReleased(MouseEvent e) {
    		int x = e.getX()/cellWidth;
    		int y = e.getY()/cellWidth;
    		boolean madeMove = false;
    		if (x >= 0 && y >= 0 && x < 8 && y < 8 && selected != null && preview != null && mouseEnabled) {
    			Cell currentCell = cells[x][y];
    			if (!selected.equals(currentCell)) {
    				//if I released on a different cell than where I started
					Move move = new Move(selected, currentCell, preview, Board.this);
    				
    				if (move.isValid() && isTurn(preview)) {
    					//if cell I released on is valid, transfer piece
    					moves.push(move);
    					move.execute();
    					madeMove = true;
        			} else {
        				//if not valid to move to released cell, then put piece back in original
        				selected.put(preview);
        			}
    				selected.unpress();
    				selected = null;
    			} else {
    				//finished a drag on the same cell in which i started
    				selected.put(preview);
    			}
    			preview = null;
    		}
    		Board.this.paintImmediately(Board.this.getBounds());
    		if (madeMove) {
    			switchTurn(true);
    		}
    		repaint();
    	}
	}
	
	class mouseMotionEventHandler extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
        	int x = e.getX();
    		int y = e.getY();
    		if (x < cellWidth*8 && y < cellWidth*8 && x > 0 && y > 0 && mouseEnabled) {
	    		if (selected != null && selected.occupied()) {
	    			preview = selected.getPiece();
	    			selected.put(null);
	    			preview.setCoords(x - cellWidth/2, y - cellWidth/2);
	    		} else if (preview != null ){
	    			preview.setCoords(x - cellWidth/2, y - cellWidth/2);
	    		}
    		} else {
    			if (selected != null && preview != null) {
	    			selected.put(preview);
					preview = null;
    			}
    		}
    		repaint();
        }
	}
}