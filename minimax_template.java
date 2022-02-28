public class minimax_template {

    char player = 'X', opponent = 'O';

    // Check if all 9 positions have Os or Xs 
    public boolean isMovesLeft(char board[])
    {
        for (int i = 0; i < 9; i++)
                if (board[i] == ' ')
                    return true;
        return false;
    }

    // Endpoints of the 8 rows in the board - across, down, diagonally
    private int rows[][]={{0,2},{3,5},{6,8},{0,6},{1,7},{2,8},{0,8},{2,6}};  

    // If the 2 positions are occupied by player and the third is blank, then returns the index of the blank position
    int findRow(char[] position,char player) {

      for (int i=0; i<8; ++i) {
        int result=findMiddle(position, player, rows[i][0], rows[i][1]);
        
        if (result>=0)
          return result;
      }

      return -1;
    }

    // If 2 of 3 positions in the row are occupied by player and the third is blank, then returns the index of the blank position
    int findMiddle(char[] position, char player, int a, int b) {
      
      int middle=(a+b)/2; 

      if (position[a]==player && position[b]==player && position[middle]==' ')
        return middle;
      if (position[a]==player && position[middle]==player && position[b]==' ')
        return b;
      if (position[b]==player && position[middle]==player && position[a]==' ')
        return a;

      return -1;
    }

    // evaluates the board and returns the best position
    public int evaluate(char b[])
    {
        int r = findRow(b,opponent);  //try to block O from winning
        if (r>=0) {
            return -100;
        }

        int i =0;
        for (int row = 0; row < 3; row++) //rows
        {
            if ((b[i] == b[i+1] && b[i] == b[i+2]) && b[i] != ' ')
            {
                if (b[i] == player)
                    return +10;
                else if (b[i] == opponent)
                    return -10;
            }
            i=i+3;
        }
        
        i =0;
        for (int col = 0; col < 3; col++) //columns
        {
            if ((b[i] == b[i+3] && b[i] == b[i+6]) && b[i] != ' ')
            {
                if (b[i] == player)
                    return +10;
    
                else if (b[i] == opponent)
                    return -10;
            }
            i++;
        }
    
        if ((b[0] == b[4] && b[4] == b[8]) && b[i] != ' ') //diagonals
        {
            if (b[0] == player)
                return +10;
            else if (b[0] == opponent)
                return -10;
        }
    
        if ((b[2] == b[4] && b[4] == b[6]) && b[i] != ' ')
        {
            if (b[2] == player)
                return +10;
            else if (b[2] == opponent)
                return -10;
        }
    
        // Else if none of them have won then return 0
        return 0;
    }

   
    public int minimax(char board[], int depth, boolean isMax)
    {
        int score = evaluate(board);
    
        if (score == 10)
            return score;
    
        if (score == -10)
            return score;
    
        if (isMovesLeft(board) == false)
            return 0;
    
        // If this maximizer's move
        if (isMax)
        {
            int best = -1000;
    
            for (int i = 0; i < 9; i++)
            {
                if (board[i] == ' ')
                {
                    board[i] = player;

                    // Call minimax recursively and choose the maximum value
                    best = Math.max(best, minimax(board, depth + 1, !isMax));

                    board[i] = ' ';
                }
            }

            return best;

        }else // If this minimizer's move
        {
            int best = 1000;
    
            for (int i = 0; i < 9; i++)
            {
                if (board[i] == ' ')
                {
                    board[i] = opponent;

                    // Call minimax recursively and choose the minimum value
                    best = Math.min(best, minimax(board,depth + 1, !isMax));

                    board[i] = ' ';
                }
            }
            return best;
        }
    }

    public int findBestMove(char board[])
    {
        int bestVal = -1000;
        int index = -1;
    
        for (int i = 0; i < 9; i++)
        {
            if (board[i] == ' ')
            {
                board[i] = player;

                // call minimax for this move.
                int moveVal = minimax(board, 0, false);

                board[i] = ' ';

                // If the value of the current move is more than the best value, then update best
                if (moveVal > bestVal)
                {
                    index = i;
                    bestVal = moveVal;
                }
            }
        }
    
        return index;
    }

}
