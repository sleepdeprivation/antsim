package Ant;

import java.util.ArrayList;
import java.util.PriorityQueue;

import Pheromone.PheromoneAreaMap;
import Pheromone.PheromoneCell;
import logic.Board;
import logic.NumberGenerator;
import logic.Utility;
import request.MoveRequest;
import request.PlacementRequest;
import request.Request;

/*
 * Ant2 will have a heading that defines it's direction (out of the 8 possible)
 * it will only be able to move in that direction or turn one to the right or left
 * in addition it will drop pheromones with a heading and will be influenced by them
 */
public class Ant2 extends Ant implements Cell{

	NumberGenerator.UniformGenerator headingGenerator = new NumberGenerator.UniformGenerator(0, 8);
	int heading = headingGenerator.generate();
	
	int homing_direction = (heading + 4)%8;
	
	int[] aggregations;

	public Ant2() {
		super();
	}
	
	public Ant2(int[] location, Anthill anthill) {
		super(location, anthill);
	}

	@Override
	public void loadNeighboorhood(Board grid, int[] myLocation,
			PheromoneAreaMap pheromoneMap) {
		super.loadNeighboorhood(grid, myLocation, pheromoneMap);
		int[] squareTopLeft = new int[]{myLocation[0] - windowRadius, myLocation[1] - windowRadius};
		int[] squareBottomRight = new int[]{myLocation[0] + windowRadius, myLocation[1] + windowRadius};
		aggregations = pheromoneMap.aggregator.getSquare(squareTopLeft, squareBottomRight);
	}

	@Override
	public void defaultBehavior(PriorityQueue<Request> requestQueue) {
		int direction;
		double angle;
		if(hasItems){
			angle = Math.atan2(this.origin[0] - this.location[0], this.origin[1] - this.location[1]);
			direction = (int) ((2*Math.PI + angle - Math.PI/2)/(Math.PI/4))%8;
			if(hasItems){
				requestQueue.add(new PlacementRequest(new PheromoneCell(), this.location));
			}
		}else{
			direction = Utility.getWeightedRandomDirection(
								new int[]{
										aggregations[(this.heading-1 + 8) %8]+5,
										aggregations[(this.heading + 8) %8] + 5,
										aggregations[(this.heading+1 + 8) %8]+5
								});
			direction += this.heading;
			direction = direction%8;
		}
		requestQueue.add(new MoveRequest(this, this.location, this.moveDirection(direction, this.location)));
		
	}

}
