import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class TicTacToe extends JFrame {

  public minimax_template minmax = new minimax_template();
  private int dim=3;

  private char position[]={  // Board position (' ', O, or X)
    ' ', ' ', ' ',
    ' ', ' ', ' ',
    ' ', ' ', ' '};

  private int wins=0, losses=0, draws=0;

  public static void main(String args[]) {
    new TicTacToe();
  }

  // Initialize
  public TicTacToe() {
    super("Tic Tac Toe");
    add(new Board(), BorderLayout.CENTER);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(400, 400);
    setVisible(true);
  }

  // Board is what actually plays and displays the game
  private class Board extends JPanel implements MouseListener {

    public Board() {
      addMouseListener(this);
    }

    // Redraw the board
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      int w=getWidth();
      int h=getHeight();
      Graphics2D g2d = (Graphics2D) g;

      // Draw the grid
      g2d.setPaint(Color.WHITE);
      g2d.fill(new Rectangle2D.Double(0, 0, w, h));
      g2d.setPaint(Color.BLACK);
      g2d.setStroke(new BasicStroke(4));
      g2d.draw(new Line2D.Double(0, h/3, w, h/3));
      g2d.draw(new Line2D.Double(0, h*2/3, w, h*2/3));
      g2d.draw(new Line2D.Double(w/3, 0, w/3, h));
      g2d.draw(new Line2D.Double(w*2/3, 0, w*2/3, h));

      // Draw the Os and Xs
      for (int i=0; i<9; ++i) {
        double xpos=(i%3+0.5)*w/3.0;
        double ypos=(i/3+0.5)*h/3.0;
        double xr=w/8.0;
        double yr=h/8.0;
        if (position[i]== 'O') {
          g2d.setPaint(Color.BLUE);
          g2d.draw(new Ellipse2D.Double(xpos-xr, ypos-yr, xr*2, yr*2));
        }
        else if (position[i]=='X') {
          g2d.setPaint(Color.RED);
          g2d.draw(new Line2D.Double(xpos-xr, ypos-yr, xpos+xr, ypos+yr));
          g2d.draw(new Line2D.Double(xpos-xr, ypos+yr, xpos+xr, ypos-yr));
        }
      }
    }

    // Human plays, when he clicks mouse in an available position, then an O is drawed in this position
    public void mouseClicked(MouseEvent e) {
      int xpos=e.getX()*3/getWidth();
      int ypos=e.getY()*3/getHeight();
      int pos=xpos+3*ypos;
      if (pos>=0 && pos<9 && position[pos]==' ') {
        position[pos]='O';
        repaint();
        playX();  // computer plays - minimax
        repaint();
      }
    }

    // Ignore other mouse events
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    // Computer plays X using minimax
    void playX() {
      
      // First check if game is over to start a new game
      if (win('O'))
        newGame('O');
      else if (isMovesLeft())
        newGame(' ');

      // Else play X, and check again if game is over to start a new game
      else {
        int index = minmax.findBestMove(position);
        if(position[index] == ' '){
            position[index]='X';
        }
        if (win('X'))
          newGame('X');
        else if (isMovesLeft())
          newGame(' ');
      }
    }

    // Returns true if are 3 Xs or Os 
    public boolean win_line(char turn, int start, int step){
        for(int i = 0; i <3; i++){
            if(position[start + step*i] != turn){
                return false;
            }
        }
        return true;
    }

    // check if a player wins - if he have 3 Xs or Os in a column, a row or a diagonal
    public boolean win(char turn){ 

        for(int i =0; i <dim; i++){
            if(win_line(turn, i*dim, 1) || win_line(turn, i, dim)){
                return true;
            }
        }

        if(win_line(turn, dim-1, dim-1) || win_line(turn, 0, dim+1)){
            return true;
        }

        return false;
    }    

    // Check if all 9 positions have Os or Xs 
    boolean isMovesLeft() {
      for (int i=0; i<9; ++i)
        if (position[i]==' ')
          return false;
      return true;
    }

    // Start a new game
    void newGame(char winner) {
      repaint();

      // Print the results of the game.  Ask user if he wants to play again.
      String result;
      if (winner=='O') {
        ++wins;
        result = "O Wins - HUMAN!";
      }
      else if (winner=='X') {
        ++losses;
        result = "X Wins - MINIMAX!";
      }
      else {
        result = "Tie";
        ++draws;
      }

      if (JOptionPane.showConfirmDialog(null, "You have "+wins+ " wins, "+losses+" losses, "+draws+" draws\n" +"Play again?", result, JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) {
        System.exit(0);
      }

      // Clear the board to start a new game
      for (int j=0; j<9; ++j)
        position[j]=' ';

      // Computer starts first every other game
      if ((wins+losses+draws)%2 == 1){
       
        int index = minmax.findBestMove(position);
        if(position[index] == ' '){
            position[index]='X';
        }
      }
    }
    
  } //inner class Board

} //class TicTacToe