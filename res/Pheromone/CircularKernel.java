package Pheromone;

import java.awt.Shape;
import java.util.ArrayList;

import logic.CoordinatePair;

/*
 * A pheromone kernel that emits/controls a set of pheromones
 * in a circular pattern
 */
public class CircularKernel extends PheromoneKernel {

	int radius = 5;
	int[] center;
	
	private Pheromone generatePheromone(int x, int y){
		if(	(x*x) + (y*y) < (radius*radius) ){
			Pheromone.LinearDecayPheromone l = new Pheromone.LinearDecayPheromone(false);
			int distance = (int) Math.sqrt(Math.abs(((x*x) + (y*y))));
			System.out.println(distance);
			double pct = 1 - ((double) distance / (radius));
			System.out.println(pct);
			l.decayCounter = (int) (pct*(1000));
			return l;
		}
		return null;
	}
	
	@Override
	public void apply(PheromoneAreaMap pheromoneMap, int[] origin) {
		center = origin;
		for(int ii = origin[0] - radius; ii < origin[0] + radius; ii++){
		for(int kk = origin[1] - radius; kk < origin[1] + radius; kk++){
			Pheromone p = generatePheromone(ii - center[0], kk - center[1]);
			if(p != null){
				if(pheromoneMap.get(new CoordinatePair(ii, kk)) == null){
					pheromoneMap.put(new CoordinatePair(ii, kk), new ArrayList<Pheromone>());
				}
				pheromoneMap.get(new CoordinatePair(ii, kk)).add(p);
			}
		}}
	}
	
	
}
