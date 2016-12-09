package Ant;

import java.util.PriorityQueue;

import request.MoveRequest;
import request.Request;
import Pheromone.PheromoneAreaMap;

public class Ant extends Cell {
	
	int[] location;
	
	@Override
	public void loadNeighboorhood(Cell[][] grid, int[] myLocation,
			PheromoneAreaMap pheromoneMap) {
		System.out.println("loading neighborhood ant");
		location = myLocation;
	}

	@Override
	public void step(PriorityQueue<Request> requestQueue) {
		System.out.println("stepping ant");
		int[] newLocation = new int[]{location[0] + 1, location[1] + 1};
		requestQueue.add(new MoveRequest(this, location, newLocation));
		System.out.println("returning from ant");
	}

}
