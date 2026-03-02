package main.java.conway.domain;

public interface IGrid {
	public boolean isCellAlive(int col, int row);
	public int getNumRows();
	public int getNumCols();
	public void setCellStatus(int col, int row, boolean status);
}
