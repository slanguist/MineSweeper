package game.ui;

import javax.swing.*;


public class MineSweeperCell extends JButton {

    public final int row;
    public final int column;

    public MineSweeperCell(int thisRow, int thisColumn) {
        setSize(10, 10);
        row = thisRow;
        column = thisColumn;
    }
}