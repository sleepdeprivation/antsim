package Ant;

import java.util.PriorityQueue;

import logic.NumberGenerator;
import Pheromone.PheromoneCell;
import request.PlacementRequest;
import request.Request;

public class Anthill implements Cell {
	
	//how many ants can we make?
	int num_ants = 100;
	//what is the range we are allowed to create ants within?
	public int radius = 20;
	
	public int score = 0;
	
	NumberGenerator.UniformGenerator UNG = new NumberGenerator.UniformGenerator(-radius, radius);
	int[] location;
	
	public Anthill(int[] l){
		location = l;
	}

	@Override
	public void step(PriorityQueue<Request> requestQueue) {
		if(num_ants > 0){
			//System.out.println("making an ant");
			requestQueue.add(new PlacementRequest(new Ant(location, this), generateNewLocation()));
			num_ants--;
		}
	}
	
	public int[] generateNewLocation(){
		return new int[]{location[0] + UNG.generate(), location[1] + UNG.generate()};
	}

	public void checkInAnt(Ant ant) {
		this.num_ants++;
		if(ant.hasItems){
			score++;
		}
	}

}
