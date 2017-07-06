package game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import game.MineSweeper.CellStatus;
import java.util.*;

public class MineSweeperTest {
    MineSweeper mineSweeper;

    @Before
    public void setUp()
    {
        mineSweeper = new MineSweeper();
    }

    @Test
    public void canary()
    {
        assertTrue(true);
    }

    @Test
    public void exposeAnUnexposedCell() {
        mineSweeper.exposeCell(0, 1);

        assertEquals(CellStatus.EXPOSED, mineSweeper.getCellStatus(0, 1));
    }

    @Test
    public void exposingAnAlreadyExposedCellStaysExposed() {
        mineSweeper.exposeCell(0, 1);
        mineSweeper.exposeCell(0, 1);

        assertEquals(CellStatus.EXPOSED, mineSweeper.getCellStatus(0, 1));
    }

    @Test
    public void verifyBoundsForCellOutOfRightTopRowRange() {
        try {
            mineSweeper.verifyBounds(-1, 0);

            fail("Expected exception for out of bounds parameters.");
        } catch (IndexOutOfBoundsException ex) {
        }
    }

    @Test
    public void verifyBoundsForCellOutOfRightBottomRowRange() {
        try {
            mineSweeper.verifyBounds(11, 0);

            fail("Expected exception for out of bounds parameters.");
        } catch (IndexOutOfBoundsException ex) {
        }
    }

    @Test
    public void verifyBoundsForCellOutOfRightColumnRange() {
        try {
            mineSweeper.verifyBounds(0, -1);

            fail("Expected exception for out of bounds parameters.");
        } catch (IndexOutOfBoundsException ex) {
        }
    }

    @Test
    public void verifyBoundsForCellOutOfLeftColumnRange() {
        try {
            mineSweeper.verifyBounds(0, 11);

            fail("Expected exception for out of bounds parameters.");
        } catch (IndexOutOfBoundsException ex) {
        }
    }

    class MineSweeperWithVerifyBoundsStubbed extends MineSweeper {
        public boolean verifyBoundsCalled = false;

        @Override
        public void verifyBounds(int row, int column)
        {
            verifyBoundsCalled = true;
        }
    }

    @Test
    public void exposeCellCallsVerifyBounds() {
        MineSweeperWithVerifyBoundsStubbed mineSweeperStubbed = new MineSweeperWithVerifyBoundsStubbed();
        mineSweeperStubbed.exposeCell(0, 1);

        assertTrue(mineSweeperStubbed.verifyBoundsCalled);
    }

    class MineSweeperWithExposeNeighborsStubbed extends MineSweeper {
        public boolean exposeNeighborsCalled = false;

        @Override
        public void exposeNeighbors(int row, int column)
        {
            exposeNeighborsCalled = true;
        }
    }

   @Test
   public void exposeCellTriggersExposeOnNeighbors()
   {
       MineSweeperWithExposeNeighborsStubbed mineSweeperStubbed = new MineSweeperWithExposeNeighborsStubbed();
       mineSweeperStubbed.exposeCell(1, 1);

       assertTrue(mineSweeperStubbed.exposeNeighborsCalled);
   }

    @Test
    public void exposeNeighborsNotCalledIfCallAlreadyExposed()
    {
        MineSweeperWithExposeNeighborsStubbed mineSweeperStubbed = new MineSweeperWithExposeNeighborsStubbed();
        mineSweeperStubbed.exposeCell(1, 1);
        mineSweeperStubbed.exposeNeighborsCalled = false;
        mineSweeperStubbed.exposeCell(1, 1);

        assertFalse(mineSweeperStubbed.exposeNeighborsCalled);
    }

    class MineSweeperWithExposeCellStubbed extends MineSweeper {
        public List<Integer> rows = new ArrayList<>();
        public List<Integer> columns = new ArrayList<>();

        @Override
        public void exposeCell(int row, int column){
            rows.add(row);
            columns.add(column);
        }
    }

   @Test
   public void exposeNeighborsCallExposeCellOnNeighborsAndSelf()
   {
       MineSweeperWithExposeCellStubbed mineSweeperStubbed = new MineSweeperWithExposeCellStubbed();
       mineSweeperStubbed.exposeNeighbors(1, 1);

       assertEquals(Arrays.asList(0, 0, 0, 1, 1, 1, 2, 2, 2), mineSweeperStubbed.rows);
       assertEquals(Arrays.asList(0, 1, 2, 0, 1, 2, 0, 1, 2), mineSweeperStubbed.columns);
   }                                       

   @Test
   public void exposeNeighborsOnTheTopLeftAndSelf()
   {
       MineSweeperWithExposeCellStubbed mineSweeperStubbed = new MineSweeperWithExposeCellStubbed();
       mineSweeperStubbed.exposeNeighbors(0, 0);

       assertEquals(Arrays.asList(0, 0, 1, 1), mineSweeperStubbed.rows);
       assertEquals(Arrays.asList(0, 1, 0, 1), mineSweeperStubbed.columns);
   }

    @Test
    public void exposeNeighborsOnTheTopRightAndSelf()
    {
        MineSweeperWithExposeCellStubbed mineSweeperStubbed = new MineSweeperWithExposeCellStubbed();
        mineSweeperStubbed.exposeNeighbors(0, 9);

        assertEquals(Arrays.asList(0, 0, 1, 1), mineSweeperStubbed.rows);
        assertEquals(Arrays.asList(8, 9, 8, 9), mineSweeperStubbed.columns);
    }

    @Test
    public void exposeNeighborsOnTheBottomLeftAndSelf()
    {
        MineSweeperWithExposeCellStubbed mineSweeperStubbed = new MineSweeperWithExposeCellStubbed();
        mineSweeperStubbed.exposeNeighbors(9, 0);

        assertEquals(Arrays.asList(8, 8, 9, 9), mineSweeperStubbed.rows);
        assertEquals(Arrays.asList(0, 1, 0, 1), mineSweeperStubbed.columns);
    }

    @Test
    public void exposeNeighborsOnTheBottomRightAndSelf()
    {
        MineSweeperWithExposeCellStubbed mineSweeperStubbed = new MineSweeperWithExposeCellStubbed();
        mineSweeperStubbed.exposeNeighbors(9, 9);

        assertEquals(Arrays.asList(8, 8, 9, 9), mineSweeperStubbed.rows);
        assertEquals(Arrays.asList(8, 9, 8, 9), mineSweeperStubbed.columns);
    }

    @Test
    public void exposeCellCallsExposeOnNeighborsForCellsOnColumnEdge()
    {
        MineSweeperWithExposeCellStubbed mineSweeperStubbed = new MineSweeperWithExposeCellStubbed();
        mineSweeperStubbed.exposeNeighbors(0, 4);

        assertEquals(Arrays.asList(0, 0, 0, 1, 1, 1), mineSweeperStubbed.rows);
        assertEquals(Arrays.asList(3, 4, 5, 3, 4, 5), mineSweeperStubbed.columns);
    }

    @Test
    public void exposeCellCallsExposeOnNeighborsForCellsOnRowEdge()
    {
        MineSweeperWithExposeCellStubbed mineSweeperStubbed = new MineSweeperWithExposeCellStubbed();
        mineSweeperStubbed.exposeNeighbors(4, 0);

        assertEquals(Arrays.asList(3, 3, 4, 4, 5, 5), mineSweeperStubbed.rows);
        assertEquals(Arrays.asList(0, 1, 0, 1, 0, 1), mineSweeperStubbed.columns);
    }

    @Test
    public void sealAnUnexposedCell() {
        mineSweeper.toggleSeal(0, 1);

        assertEquals(CellStatus.SEALED, mineSweeper.getCellStatus(0, 1));
    }

    @Test
    public void sealingASealedCellUnsealsCell()
    {
        mineSweeper.toggleSeal(3, 3);
        mineSweeper.toggleSeal(3, 3);

        assertEquals(CellStatus.HIDDEN, mineSweeper.getCellStatus(3, 3));
        assertEquals(CellStatus.HIDDEN, mineSweeper.getCellStatus(3, 3));
    }

    @Test
    public void sealingAnAlreadyExposedCellStaysExposed() {
        mineSweeper.exposeCell(0, 1);
        mineSweeper.toggleSeal(0, 1);

        assertEquals(CellStatus.EXPOSED, mineSweeper.getCellStatus(0, 1));
    }

    @Test
    public void exposingASealedCellStaysSealed(){
        mineSweeper.toggleSeal(0, 1);
        mineSweeper.exposeCell(0, 1);

        assertEquals(CellStatus.SEALED,mineSweeper.getCellStatus(0, 1));

    }

    @Test
    public void toggleSealCellCallsVerifyBounds() {
        MineSweeperWithVerifyBoundsStubbed mineSweeperStubbed = new MineSweeperWithVerifyBoundsStubbed();
        mineSweeperStubbed.toggleSeal(0, 1);

        assertTrue(mineSweeperStubbed.verifyBoundsCalled);
    }

    @Test
    public void exposeCellOnASealedCellDoesNotCallExposeNeighbors()
    {
        MineSweeperWithExposeNeighborsStubbed mineSweeperStubbed = new MineSweeperWithExposeNeighborsStubbed();
        mineSweeperStubbed.toggleSeal(1, 1);  
        mineSweeperStubbed.exposeCell(1, 1);

        assertFalse(mineSweeperStubbed.exposeNeighborsCalled);
    }


    class MineSweeperWithExposeNeighborsAndIsMinedStubbed extends MineSweeperWithExposeNeighborsStubbed {

        @Override
        public boolean isMined(int row, int column)
        {
            return true;
        }
    }

    @Test
    public void cellWithMineDoesNotCallExposeNeighborWhenExposed()
    {
        MineSweeperWithExposeNeighborsAndIsMinedStubbed mineSweeperStubbed = new MineSweeperWithExposeNeighborsAndIsMinedStubbed();
        mineSweeperStubbed.exposeCell(1, 1);

        assertFalse(mineSweeperStubbed.exposeNeighborsCalled);
    }

    class MineSweeperWithExposeNeighborsAndIsAdjancentStubbed extends MineSweeperWithExposeNeighborsStubbed{

        @Override
        public boolean isAdjacentCell(int row, int column){return true;}
    }

    @Test
    public void adjacentDoesNotCallExposeNeighborWhenExposed()
    {
                                    
        MineSweeperWithExposeNeighborsAndIsAdjancentStubbed mineSweeperStubbed = new MineSweeperWithExposeNeighborsAndIsAdjancentStubbed();
        mineSweeperStubbed.exposeCell(1, 1);

        assertFalse(mineSweeperStubbed.exposeNeighborsCalled);
    }

    @Test
    public void ifCellHasMineIsMinedReturnsTrue()
    {
        mineSweeper.setMine(0, 1);
        assertTrue(mineSweeper.isMined(0, 1));
    }

    @Test
    public void ifCellDoesNotHaveMineReturnFalse()
    {
        assertFalse(mineSweeper.isMined(0, 1));
    }

    @Test
    public void adjacentCellisAdjacentToMinedCell(){
        mineSweeper.setMine(0, 0);

        assertTrue(mineSweeper.isAdjacentCell(0, 1));
    }
    @Test
    public void emptyCellisNotAdjacentToMine(){
        mineSweeper.setMine(0, 0);

        assertFalse(mineSweeper.isAdjacentCell(0, 2));
    }

    @Test
    public void gameStatusInProgressWhenNoCellsExposedOrSealed(){
        assertEquals(MineSweeper.GameStatus.INPROGRESS, mineSweeper.getGameStatus());
    }

    @Test
    public void gameStatusWonWhenAllMinesSealedAndAllUnminedCellsExposed(){ 

      for(int i = 0; i < 10; i++)
        mineSweeper.setMine(i, i);
        
      for(int i = 0; i < 10; i++)
          for(int j = 0; j < 10; j++) {
             if(mineSweeper.isMined(i, j))
              mineSweeper.toggleSeal(i, j);
             else
              mineSweeper.exposeCell(i, j);
          }

        assertEquals(MineSweeper.GameStatus.WON, mineSweeper.getGameStatus());

    }
    @Test
    public void gameStatusLostWhenMinedCellExposed(){
        mineSweeper.setMine(1, 1);
        mineSweeper.exposeCell(1, 1);
        assertEquals(MineSweeper.GameStatus.LOST, mineSweeper.getGameStatus());
    }

    @Test
    public void gameStatusInProgressIfAllMinesPlusACellAreSealedAndAllOtherCellsExposed(){
        for(int i = 0; i < 10; i++)
            mineSweeper.setMine(i, i);

        mineSweeper.toggleSeal(2, 3);

        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++) {
                if(mineSweeper.isMined(i, j))
                    mineSweeper.toggleSeal(i, j);
                else
                    mineSweeper.exposeCell(i, j);
            }

        assertEquals(MineSweeper.GameStatus.INPROGRESS, mineSweeper.getGameStatus());

    }

    @Test
    public void gameStatusInProgressIfThereAreHiddenCells(){
        for(int i = 0; i < 10; i++)
            mineSweeper.setMine(i, i);

        mineSweeper.toggleSeal(2, 3);

        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++) {
                if(!mineSweeper.isMined(i, j))
                    mineSweeper.exposeCell(i, j);
            }

        assertEquals(MineSweeper.GameStatus.INPROGRESS, mineSweeper.getGameStatus());
    }

    @Test
    public void tenMinesPlacedByPlaceMines(){
        int mineCounter = 0;

        mineSweeper.placeMines();
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++) {
                if(mineSweeper.isMined(i, j))
                    mineCounter++;
            }

        assertEquals(10, mineCounter);

    }

    @Test
    public void placeMinesRandomlyPlacesMines(){
        MineSweeper mineSweeper2 = new MineSweeper();
        int minesEqualCounter = 0;
        mineSweeper.placeMines();
        mineSweeper2.placeMines();
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++) {
                if(mineSweeper.isMined(i, j) && mineSweeper2.isMined(i, j))
                    minesEqualCounter++;
            }
        assertNotEquals(10, minesEqualCounter);
    }

    @Test
    public void placeMinesCallsIsMinedToVerifyNoMineIsPlacedInAMinedSpot()
    {
        class MineSweeperWithIsMinedStubbed extends MineSweeper{
            public boolean isMinedCalled = false;

            @Override
            public boolean isMined(int row, int column)
            {
                isMinedCalled = true;
                if (mines[row][column])
                    return true;
                else
                    return false;
            }
        }

        MineSweeperWithIsMinedStubbed mineSweeperPlaceMinesStubbed = new MineSweeperWithIsMinedStubbed();
        mineSweeperPlaceMinesStubbed.placeMines();

        assertTrue(mineSweeperPlaceMinesStubbed.isMinedCalled);
    }

    @Test
    public void testToQuiteCoverageForEnumGameStatus()
    {
        MineSweeper.GameStatus.values();
        MineSweeper.GameStatus.valueOf("INPROGRESS");
    }

    @Test
    public void testToQuiteCoverageForEnumCellStatus()
    {
        MineSweeper.CellStatus.values();
        MineSweeper.CellStatus.valueOf("SEALED");
    }
}
