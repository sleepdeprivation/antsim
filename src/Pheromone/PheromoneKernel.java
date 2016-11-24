package Pheromone;

import java.util.ArrayList;
import java.util.HashMap;

import logic.CoordinatePair;

public abstract class PheromoneKernel{
	/*
	Controls and creates pheromones
	pheromoneList : HashMap<int[2], Pheromone>
	boundingBox : int[] //x1,y1,x2,y2
	generatePheromone(x, y)
		used internally from apply
	apply(Cell[][] grid)
		apply this PheromoneKernel to the grid
	decay()
		decay all the pheromones
	 */
	
	ArrayList<Pheromone> pheromoneList = new ArrayList<Pheromone>();
	public CoordinatePair origin;
	public PheromoneAreaMap map;
	
	public abstract void apply(PheromoneAreaMap pheromoneMap, int[] origin);
	
	public boolean decay(){
		boolean allDecayed = true;
		boolean didDecay;
		for (Pheromone p : pheromoneList){
			didDecay = p.decay();
			map.removePheromone(origin, p);
			allDecayed = allDecayed && didDecay;
		}
		return allDecayed;
	}
}