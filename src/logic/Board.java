package logic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.PriorityQueue;

import Ant.Cell;
import Pheromone.PheromoneAreaMap;
import request.Request;

public class Board {
	int numRows;
	int numCols;
	Cell[][] grid;
	int timerInterval;
	PheromoneAreaMap pheromoneMap = new PheromoneAreaMap();
	PriorityQueue<Request> requestQueue = new PriorityQueue<Request>();
	
	public Board(int row, int col){
		numRows = row;
		numCols = col;
		grid = new Cell[row][col];
		for(int ii = 0; ii < numRows; ii++)
			for(int kk = 0; kk < numCols; kk++)
				grid[ii][kk] = null;
	}
	
	void run(){
		
	}
	
	public void stepAllCells(){
		int[] location = new int[]{2};
		for(int ii = 0; ii < numRows; ii++){
		for(int kk = 0; kk < numCols; kk++){
			if(grid[ii][kk] != null){
				location[0] = ii;
				location[0] = kk;
				grid[ii][kk].loadNeighboorhood(grid, location, this.pheromoneMap);
				grid[ii][kk].step(this.requestQueue);
			}
		}}
	}


	Color white = new Color(255, 255, 255);

	/*
	 * print this onto an image
	 */
	public BufferedImage createIntegerBoard(BufferedImage im){
		for(int ii = 0; ii < numRows; ii++){
		for(int kk = 0; kk < numCols; kk++){
			im.setRGB(ii, kk, white.getRGB());
		}}
		return im;
	}

	void processRequests(){
		
	}
}
