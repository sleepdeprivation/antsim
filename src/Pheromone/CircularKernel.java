package Pheromone;

import java.awt.Shape;

import logic.CoordinatePair;

public class CircularKernel extends PheromoneKernel {

	int radius = 50;
	
	private Pheromone generatePheromone(int x, int y){
		if(	(x*x) < (radius*radius) - (y*y) ){
			return new Pheromone.LinearDecayPheromone(false);
		}
		return null;
	}
	
	@Override
	public void apply(PheromoneAreaMap pheromoneMap, int[] origin) {
		for(int ii = origin[0] - radius; ii < origin[0] + radius; ii++){
		for(int kk = origin[1] - radius; kk < origin[1] + radius; kk++){
			Pheromone p = generatePheromone(ii, kk);
			if(p != null){
				pheromoneMap.get(new CoordinatePair(ii, kk)).add(p);
			}
		}}
	}
	
	
}
