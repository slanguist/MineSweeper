package game;
import java.util.Random;

public class MineSweeper
{
    public final int SIZE = 10;
    public enum CellStatus { EXPOSED, SEALED, HIDDEN}
    public enum GameStatus {INPROGRESS, LOST, WON}
    private CellStatus[][] cellStatus = new CellStatus[SIZE][SIZE];
    public boolean mines[][] = new boolean[SIZE][SIZE];

     public MineSweeper() {
       for(int i = 0; i < SIZE; i++)
         for(int j = 0; j < SIZE; j++)
           cellStatus[i][j] = CellStatus.HIDDEN;
         }



    public void exposeCell(int row, int column) {
        verifyBounds(row, column);

        if (getCellStatus(row, column) == CellStatus.HIDDEN) {
            cellStatus[row][column] = CellStatus.EXPOSED;
            if(!isAdjacentCell(row, column) && !isMined(row, column))
            {
                exposeNeighbors(row, column);
            }
        }
    }

    protected void exposeNeighbors(int row, int column) {
        for(int i = -1; i <= 1; i++) {
          for(int j = -1; j <= 1; j++) {
            if(row + i >= 0 && column + j >= 0 && row + i < SIZE && column + j < SIZE)
              exposeCell(row + i, column + j);
          }
        }
    }

    public CellStatus getCellStatus(int row, int column) {
        return cellStatus[row][column];
    }

    public void toggleSeal(int row, int column)
    {
        verifyBounds(row, column);

        if (cellStatus[row][column] == CellStatus.HIDDEN )
            cellStatus[row][column] = CellStatus.SEALED;
        else if (cellStatus[row][column] == CellStatus.SEALED)
            cellStatus[row][column] = CellStatus.HIDDEN;
    }

    public void verifyBounds(int row, int column)
    {
        CellStatus status = cellStatus[row][column];
    }

    public boolean isMined(int row, int column) {
        if (mines[row][column])
            return true;
        else
            return false;
    }

    public boolean isAdjacentCell(int row, int column)
    {
        if (isMined(row,column))
            return false;

        for(int i = -1; i <= 1; i++) {
             for (int j = -1; j <= 1; j++) {
                 if (row + i >= 0 && column + j >= 0 && row + i < SIZE && column + j < SIZE)
                 {
                     if (isMined(row + i, column + j))
                         return true;
                 }
             }
        }

        return false;
    }

    public void setMine(int row, int column)
    {
        mines[row][column] = true;
    }

    public GameStatus getGameStatus(){
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                if(getCellStatus(i, j) == CellStatus.EXPOSED && isMined(i, j))
                    return GameStatus.LOST;

        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
            {
                if(!(getCellStatus(i, j) == CellStatus.EXPOSED || isMined(i, j)&& getCellStatus(i, j) == CellStatus.SEALED))
                    return GameStatus.INPROGRESS;
            }

        return GameStatus.WON;
    }

    public void placeMines() {
       Random rand = new Random();
       int placedMines = 0;

       while (placedMines < 10)
       {
         int row = rand.nextInt(10);
         int column = rand.nextInt(10);

         if(!isMined(row, column))
         {
             setMine(row, column);
             placedMines++;
         }
       }
    }
}
