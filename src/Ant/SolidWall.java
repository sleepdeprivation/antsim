package Ant;

import java.util.PriorityQueue;
import java.util.function.Predicate;

import request.Request;

public class SolidWall implements Cell {

	/*
	 * A wall in the "to" field of a move will
	 * cause the move to removed from the requestQueue
	 * 
	 */
	@Override
	public void step(PriorityQueue<Request> requestQueue) {

	}

}
