package Ant;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

import logic.Board;
import logic.CellGrid2D;
import logic.Neighborhood;
import logic.NumberGenerator;
import request.MoveRequest;
import request.PickupRequest;
import request.PlacementRequest;
import request.RemovalRequest;
import request.Request;
import Pheromone.PheromoneAreaMap;
import Pheromone.PheromoneCell;

public class Ant implements Cell {

	int[] location = new int[]{-1, -1};
	int[] origin = new int[]{-1, -1};
	int windowRadius = 30;
	int coolDownCounter;
	NumberGenerator RNG = new NumberGenerator.UniformGenerator(0, 8);
	public PheromoneCell[][] phNeighborhood;
	public Cell[][] cellNeighborhood;
	public ArrayList<Cell> inventory = new ArrayList<Cell>();
	boolean hasItems = false;
	//public Stack<MoveRequest> moves = new Stack<MoveRequest>();
	private Anthill anthill;
	private int life = 500;

	public Ant(){
		super();
	}
	
	public Ant(int[] o, Anthill hill){
		origin = o;
		anthill = hill;
	}

	public void loadNeighboorhood(Board grid, int[] myLocation,
			PheromoneAreaMap pheromoneMap) {
		//System.out.println("loading neighborhood ant");
		phNeighborhood = new PheromoneCell[windowRadius*2 + 1][windowRadius*2 + 1];
		cellNeighborhood = new Cell[windowRadius*2 + 1][windowRadius*2 + 1];
		location[0] = myLocation[0];
		location[1] = myLocation[1];
		//load a window
		for(int ii = myLocation[0] - windowRadius; ii < myLocation[0] + windowRadius; ii++){
		for(int kk = myLocation[1] - windowRadius; kk < myLocation[1] + windowRadius; kk++){
			phNeighborhood[ii - (myLocation[0] - windowRadius)][kk - (myLocation[1] - windowRadius)] = pheromoneMap.get(ii, kk);
			cellNeighborhood[ii - (myLocation[0] - windowRadius)][kk - (myLocation[1] - windowRadius)] = grid.get(ii, kk);
		}}
		//System.out.println("completed neighborhood load");
	}
	
	/*
	 * [-,-,-]
	 * [0,0,0]
	 * [+,+,+] 
	 * 
	 * [-,0,+]
	 * [-,0,+]
	 * [-,0,+]
	 * 
	 * [3,2,1]
	 * [4,0,0]
	 * [5,6,7]
	 */
	public int[] moveDirection(int i, int[] location){
		if(i < 0 || i > 7){
			throw new IndexOutOfBoundsException();
		}
		int[] newLocation = new int[]{location[0], location[1]};
		if(0 < i && i < 4){
			newLocation[1]--;
		}else if(4 < i && i < 8){
			newLocation[1]++;
		}
		if(2 < i && i < 6){
			newLocation[0]--;
		}else if(6 < i || i < 2){
			newLocation[0]++;
		}
		return newLocation;
	}
	
	
	public int[] getEmpiricalDist(){
		//System.out.println("generating empd");
		int[] vals = new int[]{4,4,4,4, 4,4,4,4};
		int l = phNeighborhood.length;
		//int[][] test = new int[l][l];
		if(!this.hasItems){
			for(int ii = 0; ii < l; ii++){
			for(int kk = 0; kk < l; kk++){
				if(phNeighborhood[ii][kk] != null){
					double angle = Math.atan2(ii - l/2, kk - l/2);
					int wheel = (int) ((2*Math.PI + angle)/(Math.PI/4))%8;
					//if(Math.abs(ii - l/2) > 5 && Math.abs(kk - l/2) > 5){
						vals[wheel] += 1;
					//}
				}
				if(this.cellNeighborhood[ii][kk] instanceof FoodCell){
					double angle = Math.atan2(ii - l/2, kk - l/2);
					int wheel = (int) ((Math.PI+angle + Math.PI/2)/(Math.PI/4))%8;
					//if(Math.abs(ii - l/2) > 1 && Math.abs(kk - l/2) > 1){
						vals[wheel] += 100;
					//}
				}
			}}
		}else{
			double angle = Math.atan2( location[0] - origin[0], location[1] - origin[1]);
			int wheel = (int) ((2*Math.PI+angle + Math.PI/2)/(Math.PI/4))%8;
			//System.out.println(wheel);
			//if(location[0] - origin[0] > 5 && location[1] - origin[1] > 5){
				vals[wheel] += 100;
			//}
		}
		//System.out.println("returning empd");
		return vals;
	}
	
	public int getWeightedRandomDirection(int[] weights){
		//weights = new int[]{80,1,1,1,1,1,1,1};
		int[] cumSum = new int[weights.length];
		int sum = 0;//weights[0];
		for(int ii = 0; ii < weights.length; ii++){
			sum += weights[ii];
			cumSum[ii] = sum;
		}
		int ra = new NumberGenerator.UniformGenerator(0, sum).generate();
		int winningIndex = 0;
		while(winningIndex < weights.length && ra > cumSum[winningIndex%8]){
			winningIndex++;
		}
		//System.out.println(winningIndex + ",");
		return winningIndex%8;
	}
	
	public boolean give(Cell c){
		this.inventory.add(c);
		this.hasItems = true;
		//System.out.println("got inventory");
		return true;
	}
	
	public Cell take(){
		int size = this.inventory.size();
		Cell c = this.inventory.remove(size);
		this.hasItems = size - 1 >= 0;
		return c;
	}
	
	int count = 500;
	@Override
	public void step(PriorityQueue<Request> requestQueue) {

		if(life <= 0){
			requestQueue.add(new RemovalRequest(this, location));
			requestQueue.add(new PlacementRequest(new FoodCell(), location));
			this.hasItems = false;
			this.inventory = null;
			return;
		}life--;
		
		if(nearToAnthill()){
			requestQueue.add(new RemovalRequest(this, location));
			return;
		}
		
		int[] foodLocation = null;
		if(!this.hasItems){
			foodLocation = detectFood();
		}
		if(foodLocation != null){
			requestQueue.add(new PickupRequest(this, this.location, foodLocation));
		}else{
			empiricalBehavior(requestQueue);
		}
	}
	
	/*
	public boolean returningBehavior(PriorityQueue<Request> requestQueue){
		if(this.hasItems){
			//System.out.println("returning behavior...");
			//Request r = this.moves.pop();
			//requestQueue.add(r);
			requestQueue.add(new PlacementRequest(new PheromoneCell(), r.from));
			return true;
		}
		return false;
	}
	*/
	
	public boolean nearToAnthill(){
		return this.hasItems && (Math.abs(location[0] - origin[0]) < 20) &&
				(Math.abs(location[1] - origin[1]) < 20);
	}
	
	public int[] detectFood(){
		int[] direction, actualLocation;
		for(int ii = 0; ii < 8; ii++){
			actualLocation = moveDirection(ii, this.location);
			direction = correctToRelative(actualLocation);
			if(cellNeighborhood[direction[0]][ direction[1]] instanceof FoodCell){
				return actualLocation;
			}
		}
		return null;
	}
	
	public int[] correctToRelative(int[] direction){
		return new int[]{direction[0] - (this.location[0] - windowRadius), direction[1] - (this.location[1] - windowRadius)};
	}

	public void empiricalBehavior(PriorityQueue<Request> requestQueue){
		int[] weights = this.getEmpiricalDist();
		int dir = getWeightedRandomDirection(weights);
		//System.out.println(dir);
		int[] newLocation = this.moveDirection(dir, location);
		//make a request to perform the move
		requestQueue.add(new MoveRequest(this, location, newLocation));
		if(hasItems){
			requestQueue.add(new PlacementRequest(new PheromoneCell(), newLocation));
		}
	}
	
	public void randomBehavior(PriorityQueue<Request> requestQueue){
		System.out.println("stepping ant");
		int[] newLocation = moveDirection(RNG.generate(), location);
		requestQueue.add(new MoveRequest(this, location, newLocation));
		requestQueue.add(new PlacementRequest(new PheromoneCell(), newLocation));
		System.out.println("returning from ant");
	}
	
	public void circularBehavior(PriorityQueue<Request> requestQueue){
		System.out.println("stepping ant");
		int[] newLocation = moveDirection(count, location);
		count=(count+1)%8;
		requestQueue.add(new MoveRequest(this, location, newLocation));
		requestQueue.add(new PlacementRequest(new PheromoneCell(), newLocation));
		System.out.println("returning from ant");
	}
	
	public void testBehavior(PriorityQueue<Request> requestQueue){
		System.out.println("stepping ant");
		int[] newLocation = new int[]{location[0], location[1] - 1};
		requestQueue.add(new MoveRequest(this, location, newLocation));
		requestQueue.add(new PlacementRequest(new PheromoneCell(), newLocation));
		System.out.println("returning from ant");
	}

	public void notifyAnthill() {
		if(this.anthill != null){
			this.anthill.checkInAnt(this);
		}
	}

}
