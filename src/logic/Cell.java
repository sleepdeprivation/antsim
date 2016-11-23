package logic;

import java.util.PriorityQueue;

import request.Request;

public abstract class Cell {
	
	Neighborhood neighboorhod;
	int coolDownCounter;
	NumberGenerator RNG;
	
	public abstract void loadNeighboorhood(Cell[][] grid, int[] myLocation);
	
	public abstract void step(PriorityQueue<Request> requestQueue);

}
