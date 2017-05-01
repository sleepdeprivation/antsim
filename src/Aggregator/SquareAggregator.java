package Aggregator;

import logic.CellGrid2D;
import Ant.Cell;
import Pheromone.PheromoneAreaMap;
import Pheromone.PheromoneCell;

/*
 * Calculate the "integral image"
 * for 8 different counts
 */
public class SquareAggregator{
	public int[][][] aggregate;
	public PheromoneAreaMap board;
	
	public SquareAggregator(PheromoneAreaMap b){
		board = b;
		aggregate = new int[b.getRows()][b.getCols()][8];
	}
	
	//move all the integers over
	public void moveCounts(int ii, int kk, int xx, int yy){
		for(int qq = 0; qq < 8; qq++){
			aggregate[xx][yy][qq] = aggregate[ii][kk][qq]; 
		}
	}
	
	public void setAreaMap(PheromoneAreaMap b){
		board = b;
	}
	
	public int[] subtract(int[] a, int[] b){
		int[] v = new int[8];
		for(int ii = 0; ii < 8; ii++){
			v[ii] = a[ii] - b[ii];
		}
		return v;
	}
	
	public int[] add(int[] a, int[] b){
		int[] v = new int[8];
		for(int ii = 0; ii < 8; ii++){
			v[ii] = a[ii] + b[ii];
		}
		return v;
	}
	
	public void makeSafe(int[] a){
		if(a[0] < 0){
			a[0] = 0;
		}else if(a[0] > this.board.getRows()-1){
			a[0] = this.board.getRows()-1;
		}
		if(a[1] < 0){
			a[1] = 0;
		}else if(a[1] > this.board.getCols()-1){
			a[1] = this.board.getCols()-1;
		}
	}
	
	//make sure a[0] is < b[0] by swapping if necessary
	public void makeOrdered(int[] a, int[] b){
		int[] temp;
		if(a[0] > b[0]){
			temp = a;
			a = b;
			b = temp;
		}
	}
		
	public boolean areEqual(int[] a, int[] b){
		return a[0] == b[0] && a[1] == b[1];
	}
	
	/*
	 * ------------
	 * --a----b----
	 * ------------
	 * --c----d----
	 * ------------
	 * 
	 * we want d + c + b - a
	 * but c is just [d[0], a[1]]
	 * and b is just [a[0], d[1]]
	 */
	public int[] getSquare(int[] a, int[] d){
		makeSafe(a); makeSafe(d);
		//makeOrdered(a, d);
		if(areEqual(a, d)){
			return aggregate[a[0]][a[1]];
		}
		//no pretty way to write this without unnecessary generalization
		int retval[] = subtract(
				add( 	aggregate[d[0]][d[1]],	//	d
				add(	aggregate[a[0]][d[1]],	//+ b
						aggregate[a[0]][d[1]])),//+ c
						aggregate[a[0]][a[1]]); //- a
		
		for(int ii = 0; ii < retval.length; ii++){
			if(retval[ii] < 0){
				retval[ii] = 0;
			}
		}
		return retval;
	}

	/*
	 * 
	 */
	public void addPrevious(int ii, int kk){
		for(int qq = 0; qq < 8; qq++){
			aggregate[ii][kk][qq]
			= aggregate[ii-1][kk][qq]
			+ aggregate[ii][kk-1][qq]
			- aggregate[ii-1][kk-1][qq];
		}
	}
	
	public  int[][][] aggregate(){
		int heading;
		PheromoneCell n;
		for(int ii = 0; ii < board.getRows(); ii++){
		for(int kk = 0; kk < board.getCols(); kk++){
			n = board.get(ii, kk);
			if(n != null){
				heading = n.getHeading();
				if(heading == -1)
					continue;
			}else{
				continue;
			}
			if(ii == 0){	//base case: first row
				//get the sum of the previous entries
				moveCounts(ii, kk, ii-1, kk);
				//simply count the heading
				aggregate[ii][kk][heading]++;
			}else if(kk == 0){ //base case: leftmost column
				//same thing, sum of previous entries
				moveCounts(ii, kk, ii-1, kk);
				//and now count our heading
				aggregate[ii][kk][heading]++;
			}else{
				//perform dynamic programming algorithm
				addPrevious(ii, kk);
				//we still need to count the heading
				aggregate[ii][kk][heading]++;
			}
		}}
		return aggregate;
	}
	
}
