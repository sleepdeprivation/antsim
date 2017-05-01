package Pheromone;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Aggregator.SquareAggregator;
import Ant.Cell;
import logic.CellGrid2D;
import logic.CoordinatePair;

/*
 * Another Cell Grid that deals with pheromones
 * TODO: parameterize with Pheromone when
 * arbitrary pheromones are implemented
 */
public class PheromoneAreaMap extends CellGrid2D{
	
	public SquareAggregator aggregator = new SquareAggregator(this);
	
	public void reloadAggregator(){
		aggregator.setAreaMap(this);
		aggregator.aggregate();
	}

	public PheromoneAreaMap(int row, int col) {
		super(row, col);
	}
	
	@Override
	public PheromoneCell get(int ii, int kk) {
		return (PheromoneCell) super.get(ii, kk);
	}
	
	public void decayAll(){
		for(int ii = 0; ii < numRows; ii++){
		for(int kk = 0; kk < numCols; kk++){
			Cell n = get(ii, kk);
			if(n != null && n instanceof PheromoneCell){
				n.step(null);
				if(((PheromoneCell) n).hasDecayed()){
					this.set(ii, kk, null);
				}
			}
		}}
	}
	
}
