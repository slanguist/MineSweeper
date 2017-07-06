package game.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import game.MineSweeper.GameStatus;

import game.MineSweeper;

public class MineSweeperFrame extends JFrame {
    private static final int SIZE = 10;
    MineSweeper mineSweeper;
    MineSweeperCell cells[][];

    @Override
    protected void frameInit() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cells = new MineSweeperCell[SIZE][SIZE];
        mineSweeper = new MineSweeper();
        mineSweeper.placeMines();
        super.frameInit();
        setLayout(new GridLayout(10, 10));
        MouseClickedHandler handleClick = new MouseClickedHandler();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                MineSweeperCell cell = new MineSweeperCell(i, j);
                cell.addMouseListener(handleClick);
                getContentPane().add(cell);
                cells[i][j] = cell;
            }
        }
    }

    public static void main(String[] args){
        JFrame frame = new MineSweeperFrame();
        frame.setSize(1000,1000);
        frame.setVisible(true);
    }

    private class MouseClickedHandler extends MouseAdapter implements MouseListener{

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            MineSweeperCell cellClicked = (MineSweeperCell) mouseEvent.getComponent();

            if (mouseEvent.getButton() == MouseEvent.BUTTON1){
                mineSweeper.exposeCell(cellClicked.row, cellClicked.column);
            }
            else if (mouseEvent.getButton() == MouseEvent.BUTTON3){
                mineSweeper.toggleSeal(cellClicked.row, cellClicked.column);
            }
            updateUI();
            switch (mineSweeper.getGameStatus()){
                case WON:
                    JOptionPane.showMessageDialog(cellClicked, "Congrats! You Win :D");
                    System.exit(0);
                    break;
                case LOST:
                    JOptionPane.showMessageDialog(cellClicked, "Sorry, you lose :(");
                    System.exit(0);
                    break;
                case INPROGRESS:
                    break;
            }
        }
    }

    private void updateUI() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(mineSweeper.getCellStatus(i, j) == MineSweeper.CellStatus.EXPOSED){
                    if(mineSweeper.isMined(i, j)){
                        cells[i][j].setBackground(Color.RED);
                        cells[i][j].setText("M");
                    }
                    else if(mineSweeper.isAdjacentCell(i, j)){
                        cells[i][j].setBackground(Color.LIGHT_GRAY);
                        cells[i][j].setText(String.valueOf(numberOfAdjacentMines(i, j)));
                    }
                    else {
                        cells[i][j].setBackground(Color.LIGHT_GRAY);
                    }
                }
                else if (mineSweeper.getCellStatus(i, j) == MineSweeper.CellStatus.SEALED){
                    cells[i][j].setText("S");
                    cells[i][j].setBackground(Color.YELLOW);
                }
                else
                {
                    cells[i][j].setBackground(null);
                    cells[i][j].setText(null);
                }
            }
        }
    }

    private int numberOfAdjacentMines(int row, int column) {
        int numAdjacentMines = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (row + i >= 0 && column + j >= 0 && row + i < SIZE && column + j < SIZE) {
                    if (mineSweeper.isMined(row + i, column + j))
                        numAdjacentMines++;
                }
            }
        }
        return numAdjacentMines;
    }
}
