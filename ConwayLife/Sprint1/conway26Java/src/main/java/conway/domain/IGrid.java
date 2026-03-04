package main.java.conway.domain;

public interface IGrid {
	public boolean isCellAlive(int row, int col);
	public int getNumRows();
	public int getNumCols();
	public void setCellStatus(int row, int col, boolean status);
	public ICell getCell(int row, int col);
	public void reset();
}
