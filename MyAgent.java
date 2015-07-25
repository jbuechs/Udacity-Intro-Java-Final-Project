import java.util.Random;

public class MyAgent extends Agent
{
    Random r;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     */
    public void move()
    {
        char myColor = 'R';
        char theirColor = 'R';
        if (iAmRed)
        {
            theirColor = 'Y';
        }
        else
        {
            myColor = 'Y';
        }
        if  (canWin(myColor) != -1) 
        //if I can win by playing, do it!
        {
            moveOnColumn(canWin(myColor));
        }
        else if (canWin(theirColor) != -1) 
        //if they can win next move, block them!
        {
            moveOnColumn(canWin(theirColor));
        }
        else if (twoAway(myColor, theirColor) != -1) 
        //if I am two moves away from winning and the move won't set them up for a win, play there
        {
            moveOnColumn(twoAway(myColor, theirColor));
        }
        else if (twoAway(theirColor, myColor) != -1)
        //if they are two moves away from winning and my block won't set them up for a win, play there
        {
            moveOnColumn(twoAway(theirColor, myColor));
        }     
        else
        //play a random move but make sure I'm not setting them up for a win
        {
            boolean played = false;
            while (!played)
            {
                int possibleMove = randomMove();
                if (!settingThemUp(possibleMove, myColor, theirColor))
                {
                    moveOnColumn(randomMove());
                    played = true;
                }
            }
        }
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                          // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Finds lowest blank in a column of a character matrix representation of the board
     * 
     * @param board the character matrix representation of the board
     * @param colNum The column to check
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    private int getLowestBlank(char[][] board, int colNum)
    {
        int lowestBlank = -1;
        for (int i = 0; i < board.length; i++)
        {
            if (board[i][colNum] == 'B')
            {
                lowestBlank = i;
            }
        }
        return lowestBlank;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }

     /**
     * Returns the column that would allow the agent to win.
     * @param myColor the color of the agent
     * @return the column that would allow the agent to win, returns -1 if no win possible
     */
    private int canWin(char myColor)
    {
        int winColumn = -1;
        char[][] gameMatrix = myGame.getBoardMatrix(); 
        int i = 0;
        boolean won = false;
        while (!won && i < gameMatrix[0].length)
        {
            int lowestBlank = getLowestBlank(gameMatrix, i);
            if (lowestBlank != -1)
            {
                gameMatrix[lowestBlank][i] = myColor;   //plays hypothetical move on the column
                if (gameWon(gameMatrix) == myColor)     //checks whether it's a winning move
                {
                    won = true;
                    winColumn = i;
                }
                gameMatrix[lowestBlank][i] = 'B';   //resets the slot to blank
            }
            i++;
        }
        return winColumn;
    }

     /**
     * Returns the column that would allow the agent to be one move away from winning. Like the win method, but plays two hypothetical moves to
     * determine whether any combination of two moves would result in the agent winning. Also checks if the move sets the opposite player up to win
     * @param myColor the color of the agent to be tested
     * @param theirColor the color of the opposing agent
     * @return the column that would allow the agent to be one move away from winning, returns -1 if no win possible
     */
    private int twoAway(char myColor, char theirColor)
    {
        int moveColumn = -1;
        char[][] gameMatrix = myGame.getBoardMatrix(); 
        int i = 0;
        boolean twoMovesAway = false;
        while (!twoMovesAway && i < gameMatrix[0].length)   
        {
            int firstLowestBlank = getLowestBlank(gameMatrix, i);   //gets lowest blank slot of first hypothetical move
            if (firstLowestBlank != -1)
            {
                gameMatrix[firstLowestBlank][i] = myColor; //plays first hypothetical move
                int j = 0;
                while (!twoMovesAway && j < gameMatrix[0].length)
                {
                    int secondLowestBlank = getLowestBlank(gameMatrix, j); //gets lowest blank slot of second hypothetical
                    if (secondLowestBlank != -1)
                    {
                       gameMatrix[secondLowestBlank][j] = myColor;   //plays second hypothetical move
                       if (gameWon(gameMatrix) == myColor && !settingThemUp(i, myColor, theirColor))     
                       //checks whether it's a winning move and makes sure it is not setting up the opponent for a winning move
                       {
                                twoMovesAway = true;
                                moveColumn = i;
                       }   
                       gameMatrix[secondLowestBlank][j] = 'B';   //resets the second slot to blank
                    }
                    j++;
                }
                gameMatrix[firstLowestBlank][i] = 'B'; //resets the first hypothetical move to blank
            }
            i++;    
        }
        return moveColumn;    
    }
   
    /**
     * Returns the name of this agent.
     * @return the agent's name
     */
    public String getName()
    {
        return "Jennie Never Gives Up";
    }
    
    /**
     * Check if the game has been won.
     * 
     * @param board the character matrix representation of the board
     * @return 'R' if red won, 'Y' if yellow won, 'N' if the game has not been won.
     */
    private char gameWon(char[][] board)
    {
        for (int i = 0; i < board[0].length; i++)
        {
            for (int j = 0; j < board.length; j++)
            {
                if(board[j][i] != 'B')
                {
                    if (j + 3 < board.length)
                    {
                        if(board[j][i] == board[j + 1][i] && board[j][i] == board[j + 2][i] && board[j][i] == board[j + 3][i])
                        {
                            return board[j][i];
                        }
                    }
                    if (i + 3 < board[0].length)
                    {
                        if (board[j][i] == board[j][i + 1] && board[j][i] == board[j][i + 2] && board[j][i] == board[j][i + 3])
                        {
                           return board[j][i];
                        }
                    }
                    if (i + 3 < board[0].length && j + 3 < board.length)
                    {
                        if(board[j][i] == board[j + 1][i + 1] && board[j][i] == board[j + 2][i + 2] && board[j][i] == board[j + 3][i + 3])
                        {
                            return board[j][i];
                        }
                    }
                    if (i > 2 && j + 3 < board.length)
                    {
                        if (board[j][i] == board[j + 1][i - 1] && board[j][i] == board[j + 2][i - 2] && board[j][i] == board[j + 3][i - 3])
                        {
                            return board[j][i];
                        }
                    }
                }
            }
        }
        return 'N';
    }
    
    /**
     * Checks to see if a move is setting the other player up for a win.
     * @param column The index of the column of the planned move
     * @param myColor The character of my color 'R' or 'Y'
     * @param theirColor The character of their color 'R' or 'Y'
     * @return Returns true if the planned move will set the other player up for a win, otherwise false
     */
    private boolean settingThemUp(int column, char myColor, char theirColor)
    {
        char[][] gameMatrix = myGame.getBoardMatrix(); 
        boolean settingUp = false;
        gameMatrix[getLowestBlank(gameMatrix, column)][column] = myColor; //plays planned move on the column
        for (int i = -1; i < 2; i++)
        {
            if (column + i >=0 && column + i < gameMatrix[0].length)
            {
               int lowestBlank = getLowestBlank(gameMatrix, column + i);
               if (lowestBlank != -1)
               {
                    gameMatrix[lowestBlank][column + i] = theirColor;   //plays hypothetical move for opponent on column i
                    if (gameWon(gameMatrix) == theirColor)     //checks whether it's a winning move
                    {
                        settingUp = true;
                    }
                    gameMatrix[lowestBlank][column + i] = 'B';   //resets the slot to blank
               }
            }
        }
        return settingUp;
    }
}
