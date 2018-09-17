/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Chess");
        frame.setLocation(1100, 200);

        // Main playing area
        final JLabel status = new JLabel("White's turn!");
        status.setPreferredSize(new Dimension(150, 50));
        status.setFont(new Font("Arial", Font.PLAIN, 20));
        
        String[] options1 = new String[] {"Human", "Sped bot", "Dumb bot", "Average bot", "Smart bot"}; 
        JComboBox<String> player1 = new JComboBox<String>(options1);
        player1.setPreferredSize(new Dimension(200, 40));
        player1.setFont(new Font("Arial", Font.PLAIN, 30));
        player1.setSelectedIndex(0);

        String[] options2 = new String[] {"Human", "Sped bot", "Dumb bot", "Average bot", "Smart bot"};
        JComboBox<String> player2 = new JComboBox<String>(options2);
        player2.setPreferredSize(new Dimension(200, 40));
        player2.setFont(new Font("Arial", Font.PLAIN, 30));
        player2.setSelectedIndex(0);
        
        //ai status
        JLabel aistatus = new JLabel("Human vs Human");
        aistatus.setFont(new Font("Arial", Font.PLAIN, 20));
        aistatus.setPreferredSize(new Dimension(50, 25));
        
        final JPanel status_panel = new JPanel();
        
        status_panel.setLayout(new BoxLayout(status_panel, BoxLayout.PAGE_AXIS));
        status_panel.add(aistatus);
        
        final Board board = new Board(status, player1, player2, aistatus);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel button_panel = new JPanel();
        final JPanel info_panel = new JPanel();
        
        
        JPanel master_panel = new JPanel();
        master_panel.setLayout(new BoxLayout(master_panel, BoxLayout.PAGE_AXIS));
        master_panel.add(status_panel);
        master_panel.add(button_panel);
        master_panel.add(info_panel);
        
        frame.add(master_panel, BorderLayout.SOUTH);

        final JButton reset = new JButton("New game");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        final JButton clean = new JButton("Clean");
        clean.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.clean();
            }
        });
        
        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
        });
        reset.setPreferredSize(new Dimension(200,100));
        clean.setPreferredSize(new Dimension(200,100));
        undo.setPreferredSize(new Dimension(200,100));
        reset.setFont(new Font("Arial", Font.PLAIN, 50));
        clean.setFont(new Font("Arial", Font.PLAIN, 50));
        undo.setFont(new Font("Arial", Font.PLAIN, 50));
        status.setFont(new Font("Arial", Font.ITALIC, 40));
        button_panel.setLayout(new BoxLayout(button_panel, BoxLayout.LINE_AXIS));
        button_panel.add(reset);
        button_panel.add(Box.createRigidArea(new Dimension(25,0)));
        button_panel.add(clean);
        button_panel.add(Box.createRigidArea(new Dimension(25,0)));
        button_panel.add(undo);
        button_panel.add(Box.createRigidArea(new Dimension(50,0)));
        button_panel.add(status);
        
        final JLabel versus = new JLabel("versus");
        versus.setFont(new Font("Arial", Font.PLAIN, 30));
        final JLabel whiteLeft = new JLabel("(White)");
        whiteLeft.setFont(new Font("Arial", Font.PLAIN, 30));
        final JLabel blackRight = new JLabel("(Black)");
        blackRight.setFont(new Font("Arial", Font.PLAIN, 30));
        
        info_panel.add(whiteLeft);
        info_panel.add(Box.createRigidArea(new Dimension(10,0)));
        info_panel.add(player1);
        info_panel.add(Box.createRigidArea(new Dimension(25,0)));
        info_panel.add(versus);
        info_panel.add(Box.createRigidArea(new Dimension(25,0)));
        info_panel.add(player2);
        info_panel.add(Box.createRigidArea(new Dimension(10,0)));
        info_panel.add(blackRight);
        
        final JButton help = new JButton("Help");
        help.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JOptionPane.showMessageDialog(null, helpScreen(), "Instructions", JOptionPane.PLAIN_MESSAGE);
        	}
        });
        help.setFont(new Font("Arial", Font.PLAIN, 25));
        info_panel.add(Box.createRigidArea(new Dimension(50,0)));
        info_panel.add(help);
        
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(new Dimension(500, 700));
    }
    
    public static JPanel helpScreen() {
    	JPanel master_panel = new JPanel();
    	
    	String howToPlay = "The purpose of the game is to checkmate the opponent's king. This happens when the king is put into check and cannot get out of check. There are only three ways a king can get out of check: move out of the way (though he cannot castle!), block the check with another piece, or capture the piece threatening the king. If a king cannot escape checkmate then the game is over. Customarily the king is not captured or removed from the board, the game is simply declared over.\n" + 
    			"\n" + 
    			"The player with the white pieces always moves first. Therefore, players generally decide who will get to be white by chance or luck such as flipping a coin or having one player guess the color of the hidden pawn in the other player's hand. White then makes a move, followed by black, then white again, then black and so on until the end of the game. Being able to move first is a tiny advantage which gives the white player an opportunity to attack right away.\n" + 
    			"\n" + 
    			"Each of the 6 different kinds of pieces moves differently. Pieces cannot move through other pieces (though the knight can jump over other pieces), and can never move onto a square with one of their own pieces. However, they can be moved to take the place of an opponent's piece which is then captured. Pieces are generally moved into positions where they can capture other pieces (by landing on their square and then replacing them), defend their own pieces in case of capture, or control important squares in the game.\n" + 
    			"\n" + 
    			"The king is the most important piece, but is one of the weakest. The king can only move one square in any direction - up, down, to the sides, and diagonally. When the king is attacked by another piece this is called 'check'.\n" + 
    			"The queen is the most powerful piece. She can move in any one straight direction - forward, backward, sideways, or diagonally - as far as possible as long as she does not move through any of her own pieces. And, like with all pieces, if the queen captures an opponent's piece her move is over. Notice how the white queen captures the black queen and then the black king is forced to move.\n" + 
    			"\n" + 
    			"The rook may move as far as it wants, but only forward, backward, and to the sides. The rooks are particularly powerful pieces when they are protecting each other and working together!\n" + 
    			"\n" + 
    			"The bishop may move as far as it wants, but only diagonally. Each bishop starts on one color (light or dark) and must always stay on that color. Bishops work well together because they cover up each other's weaknesses.\n" + 
    			"\n" + 
    			"Knights move in a very different way from the other pieces, going two squares in one direction, and then one more move at a 90 degree angle, just like the shape of an L. Knights are also the only pieces that can move over other pieces.\n" + 
    			"\n" + 
    			"Pawns are unusual because they move and capture in different ways: they move forward, but capture diagonally. Pawns can only move forward one square at a time, except for their very first move where they can move forward two squares. Pawns can only capture one square diagonally in front of them. They can never move or capture backwards. If there is another piece directly in front of a pawn he cannot move past or capture that piece.\n";

    	String instructions = "This is chess. Scroll down to see instruction for how to play this game, if you live under a rock and don't already know. \n" +
    			"The pieces can be moved by clicking and dragging them to the desired position. You can also toggle a piece by clicking on it, and then click on another square to move the piece to that location. It will be evident when a piece is toggled because the square it is on will be shaded darker\n" +
    			"There are no keyboard control in this game. Feel free to check out the different levels of AI that are implemented. The smart bot is rated at about 1300 elo, so it will be tough to beat it unless you are an experieced chess player! Note that you cannot have an AI play another AI, i.e.," +
    			"at least one player must be human. Also note that the harder the difficulty of the AI, the longer it will take to calculate a move.\n" +
    			"Also note that this game doesn't support enpeasante, and the king can move into checkmate. Have fun!";
    	
    	JTextArea msg = new JTextArea(instructions + "\n" + howToPlay);
    	msg.setEditable(false);
		msg.setLineWrap(true);
		msg.setWrapStyleWord(true);
		msg.setFont(new Font("Arial", Font.PLAIN, 20));

		JScrollPane scrollPane = new JScrollPane(msg);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(700,300));
    	master_panel.add(scrollPane);
    	
    	return master_panel;
    }
    
    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}