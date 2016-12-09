package Pheromone;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Ant.Cell;
import logic.CoordinatePair;

/*
 * Controls two separate entities
 * A hashmap sending coordinates lists of pheromones
 * A list of all kernels in operation
 */
public class PheromoneAreaMap extends HashMap<CoordinatePair, ArrayList<Pheromone>>{
	
	ArrayList<PheromoneKernel> kernelList = new ArrayList<PheromoneKernel>();
	
	public void addKernel(PheromoneKernel kernel, int[] origin){
		kernelList.add(kernel);
		kernel.apply(this, origin);
	}
	
	public CoordinatePair p = new CoordinatePair();
	public int getConcentration(int ii, int kk){
		p.pair[0] = ii;
		p.pair[1] = kk;
		ArrayList<Pheromone> plist = this.get(p);
		if(plist == null) return 0;
		int sum = 0;
		for(Pheromone ph : plist){
			sum += ph.decayCounter;
		}
		return sum;
	}
	
	public void decayAll(){
		for(PheromoneKernel k : kernelList){
			k.decay();
		}
	}

	public void removePheromone(CoordinatePair where, Pheromone p) {
		this.get(where).remove(p);
	}
}
