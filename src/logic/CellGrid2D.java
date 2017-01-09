package logic;

import Ant.Cell;

public class CellGrid2D {
	protected int numRows;
	protected int numCols;
	protected Cell[][] grid;

	public int getRows() {
		return this.numRows;
	}

	public int getCols() {
		return this.numCols;
	}

	public Cell get(int ii, int kk) {
		return grid[(ii + this.numRows)%this.numRows][(kk +  + this.numCols)%this.numCols];
	}

	public Cell get(int[] w){
		return this.get(
				(w[0] + this.numRows)%this.numRows,
				(w[1] + this.numCols)%this.numCols);
	}
	
	public void set(int ii, int kk, Cell m) {
		grid[(ii + this.numRows)%this.numRows][(kk + this.numCols)%this.numCols] = m;
	}

	public void set(int[] w, Cell m){
		this.set(w[0], w[1], m);
	}
	
	public CellGrid2D(int row, int col){
		numRows = row;
		numCols = col;
		grid = new Cell[row][col];
		for(int ii = 0; ii < numRows; ii++)
			for(int kk = 0; kk < numCols; kk++)
				grid[ii][kk] = null;
		System.out.println("created board");
	}
	
	public boolean inBounds(int r, int c){
		return 
		(r < numRows && c < numCols) &&
		(r >= 0 && c >= 0);
	}
	
}
