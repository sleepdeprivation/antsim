package Pheromone;

import java.util.PriorityQueue;

import request.Request;
import Ant.Cell;

public class PheromoneCell implements Cell {
	
	Pheromone pheromone;
	
	public PheromoneCell(Pheromone p){
		pheromone = p;
	}
	
	public PheromoneCell(){
		pheromone = new Pheromone.LinearDecayPheromone(false);
	}
	
	public int getRemainingDecay(){
		return pheromone.decayCounter;
	}
	
	public Pheromone getPheromone(){
		return this.pheromone;
	}
	
	public void renewPheromone(){
		this.pheromone.renew();
	}
	
	public boolean hasDecayed(){
		return pheromone.hasDecayed();
	}
	
	@Override
	public void step(PriorityQueue<Request> requestQueue) {
		pheromone.decay();
	}

	public double getNormalizedPConcentration() {
		return (double)pheromone.decayCounter / (double)Pheromone.DECAY_COUNTER_MAX;
	}

	public int getHeading() {
		if(this.pheromone != null){
			return this.pheromone.heading;
		}
		return -1;
	}

}
