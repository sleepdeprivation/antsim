package logic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.PriorityQueue;

import Ant.Ant;
import Ant.Cell;
import Pheromone.CircularKernel;
import Pheromone.Pheromone;
import Pheromone.PheromoneAreaMap;
import request.Request;

public class Board {
	int numRows;
	int numCols;
	Cell[][] grid;
	int timerInterval;
	public PheromoneAreaMap pheromoneMap = new PheromoneAreaMap();
	PriorityQueue<Request> requestQueue = new PriorityQueue<Request>();
	
	public Board(int row, int col){
		numRows = row;
		numCols = col;
		grid = new Cell[row][col];
		for(int ii = 0; ii < numRows; ii++)
			for(int kk = 0; kk < numCols; kk++)
				grid[ii][kk] = null;
		centerSet();
		System.out.println("created board");
	}
	
	public void run(){
		System.out.println("executing run function");
		stepAllCells();
		System.out.println("executing request processor");
		processRequests();
		System.out.println("completed board functions");
	}
	
	public void stepAllCells(){
		System.out.println("stepping all cells in the board");
		int[] location = new int[2];
		for(int ii = 0; ii < numRows; ii++){
		for(int kk = 0; kk < numCols; kk++){
			//System.out.println(ii + "," + kk);
			if(grid[ii][kk] != null){
				System.out.println(ii + "," + kk+" non-null");
				location[0] = ii;
				location[1] = kk;
				grid[ii][kk].loadNeighboorhood(grid, location, this.pheromoneMap);
				grid[ii][kk].step(this.requestQueue);
				System.out.println("completed step");
			}
		}}
	}

	void processRequests(){
		System.out.println("processing request queue");
		Request temp;
		while(!requestQueue.isEmpty()){
			temp = requestQueue.remove();
			grid[temp.to[0]][temp.to[1]] = grid[temp.from[0]][temp.from[1]];
			grid[temp.from[0]][temp.from[1]] = null;
		}
	}

	void processPheromones(){

	}

	public int getRows() {
		return this.numRows;
	}

	public int getCols() {
		return this.numCols;
	}

	public Cell get(int ii, int kk) {
		return grid[ii][kk];
	}
	
	//put an ant in the middle
	public void centerSet(){
		grid[numRows/2][numCols/2] = new Ant();
		pheromoneMap.addKernel(new CircularKernel(), new int[]{numRows/2, numCols/2});
		//ArrayList<Pheromone> asdf = new ArrayList<Pheromone>();
		//asdf.add(new Pheromone.LinearDecayPheromone(false));
		//pheromoneMap.put(new CoordinatePair(1, 1), asdf);
		System.out.println("break");
	}
}
