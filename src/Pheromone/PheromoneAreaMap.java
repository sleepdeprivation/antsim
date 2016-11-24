package Pheromone;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Ant.Cell;
import logic.CoordinatePair;

public class PheromoneAreaMap extends HashMap<CoordinatePair, ArrayList<Pheromone>>{
	
	ArrayList<PheromoneKernel> kernelList = new ArrayList<PheromoneKernel>();
	
	public void addKernel(PheromoneKernel kernel, int[] origin){
		kernelList.add(kernel);
		kernel.apply(this, origin);
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
