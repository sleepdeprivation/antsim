package Ant;

import java.util.PriorityQueue;

import logic.Neighborhood;
import logic.NumberGenerator;
import Pheromone.PheromoneAreaMap;
import request.Request;

public interface Cell {

	public abstract void step(PriorityQueue<Request> requestQueue);

}
