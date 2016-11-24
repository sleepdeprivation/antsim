package Ant;

import java.util.PriorityQueue;

import logic.Neighborhood;
import logic.NumberGenerator;
import Pheromone.PheromoneAreaMap;
import request.Request;

public abstract class Cell {
	
	Neighborhood neighboorhod;
	int coolDownCounter;
	NumberGenerator RNG;
	
	public abstract void loadNeighboorhood(Cell[][] grid, int[] myLocation, PheromoneAreaMap pheromoneMap);
	
	public abstract void step(PriorityQueue<Request> requestQueue);

}
