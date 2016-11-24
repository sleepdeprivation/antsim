package logic;

import java.util.ArrayList;
import java.util.List;

import Ant.Cell;
import Pheromone.Pheromone;
import Pheromone.PheromoneAreaMap;

public class Neighborhood {
	
	LabeledCell[][] absGrid;
	
	public Neighborhood(	Cell[][] grid, PheromoneAreaMap map,
							int row1, int col1, int row2, int col2,
							int[] origin){
		absGrid = new LabeledCell[row2-row1][col2-col1];
		CoordinatePair c = new CoordinatePair();
		for(int ii = row1; ii < row2; ii++){
		for(int kk = col1; kk < col2; kk++){
			c.pair[0] = ii;
			c.pair[1] = kk;
			absGrid[ii][kk] =	new LabeledCell(
									grid[ii][kk], map.get(c),
									new int[]{ii, kk},
									origin
								);
		}}
	}
	
	public class LabeledCell{
		Cell cell;
		int[] origin;
		ArrayList<Pheromone> pheromones;
		public int[] absolute_location;
		public LabeledCell(Cell c, ArrayList<Pheromone> l,  int[] abs_loc, int[] o){
			cell = c;
			absolute_location = abs_loc;
			origin = o;
			pheromones = l;
		}
		public int[] getRelativeCoordinates(){
			return new int[]{
						absolute_location[0] - origin[0],
						absolute_location[1] - origin[1],
					};
		}
		public int[] getAbsoluteCoordinates(){
			return absolute_location;
		}
	}

}
