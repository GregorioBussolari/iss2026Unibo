package main.java.conway.domain;

public class Grid implements IGrid{
	private Cell[][] grid;
	private int cols; 
	private int rows;
	
	public Grid(int rows, int cols) {
		this.grid = new Cell[rows][cols];
		this.rows = rows;
		this.cols = cols;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.grid[i][j] = new Cell(); 
            }
        }
	}
	
	public int getNumRows() {
		return this.rows;
	}
	
	public int getNumCols() {
		return this.cols;
	}
	
	public void setCellStatus(int row, int col, boolean status) {
		this.grid[row][col].setStatus(status);
	}
	
	public boolean isCellAlive(int row, int col) {
		return this.grid[row][col].isAlive();
	}
	
	public String toString() {
		String res = "";
		for(int i = 0; i< rows; i++) {
			for(int j = 0 ; j<cols; j++) {
				res += grid[i][j].toString()+ " ";
			}
			res += "\n";
		}
		return res;
	}
}
