package logic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.function.Predicate;

import Ant.Ant;
import Ant.Anthill;
import Ant.Cell;
import Ant.FoodCell;
import Ant.SolidWall;
import Pheromone.PheromoneAreaMap;
import Pheromone.PheromoneCell;
import request.MoveRequest;
import request.PickupRequest;
import request.PlacementRequest;
import request.RemovalRequest;
import request.Request;

public class Board extends CellGrid2D{

	int timerInterval;
	public PheromoneAreaMap pheromoneMap;
	PriorityQueue<Request> requestQueue = new PriorityQueue<Request>();
	int NUM_HILLS = 1;
	public ArrayList<FoodTile> foodTiles = new ArrayList<FoodTile>();
	
	public Board(int row, int col){
		super(row, col);
		pheromoneMap = new PheromoneAreaMap(row, col);
		demoSetup();
		/*for(int ii = 0; ii < NUM_HILLS; ii++){
			this.placeAnthill();
			this.placeFood();
		}*/
	}
	
	public void run(){
		//System.out.println("executing run function");
		stepAllCells();
		removeWalls();
		processFoodTiles();	//fill the food tiles
		//be careful not to overwrite ants!
		//System.out.println("executing request processor");
		processRequests();
		//System.out.println("completed board functions");
		pheromoneMap.decayAll();
	}
	
	public void processFoodTiles(){
		for(int ii = 0; ii < foodTiles.size(); ii++){
			boolean died = foodTiles.get(ii).step(this);
			if(died){
				foodTiles.remove(ii);
			}
		}
	}
	
	public void stepAllCells(){
		//System.out.println("stepping all cells in the board");
		int[] location = new int[2];
		Cell x;
		for(int ii = 0; ii < numRows; ii++){
		for(int kk = 0; kk < numCols; kk++){
			x = grid[ii][kk];
			if(x != null && x instanceof Ant){
				//System.out.println(ii + "," + kk+" non-null");
				location[0] = ii;
				location[1] = kk;
				((Ant) x).loadNeighboorhood(this, location, this.pheromoneMap);
				x.step(this.requestQueue);
				//System.out.println("completed step");
			}else if(x != null && x instanceof Anthill){
				((Anthill)x).step(this.requestQueue);
			}
		}}
	}

	void processRequests(){
		//System.out.println("processing request queue");
		Request temp;
		while(!requestQueue.isEmpty()){
			temp = requestQueue.remove();
			if(temp instanceof MoveRequest){
				safeMove(temp);
			}else if(temp instanceof PlacementRequest){
				//System.out.println("placing... ");
				safePlace(temp);
				//System.out.println("done placing... ");
			}else if(temp instanceof PickupRequest){
				safePickup(temp);
			}else if(temp instanceof RemovalRequest){
				safeRemove(temp);
			}
		}
	}
	
	
	
	private boolean safeRemove(Request temp) {
		Cell M = this.get(temp.from);
		if(M instanceof Ant){
			((Ant) M).notifyAnthill();
			this.set(temp.from, null);
			return true;
		}
		return false;
	}

	private boolean safePickup(Request temp) {
		//System.out.println("pickup request");
		Cell M = this.get(temp.to);
		if(M instanceof FoodCell){
			//System.out.println("picking up food cell");
			((Ant) temp.requestMaker).give(M);
			this.set(temp.to, null);
			return true;
		}
		return false;
	}

	private boolean safePlace(Request temp) {
		Cell M = temp.requestMaker;
		if(M instanceof PheromoneCell){
			PheromoneCell W = (PheromoneCell) this.pheromoneMap.get(temp.to);
			if(W == null){
				pheromoneMap.set(temp.to, M);
				return true;
			}else{
				W.renewPheromone();
			}
		}else if(M instanceof Ant){
			if(this.get(temp.to) == null){
				this.set(temp.to, temp.requestMaker);
			}else{
				((Ant) temp.requestMaker).notifyAnthill();
			}
		}
		return false;
	}

	/*
	 * Move only if there's nothing in the way, otherwise do nothing
	 * returns false on failure
	 */
	public boolean safeMove(Request temp){
		//System.out.println("safe move");
		Cell t = this.get(temp.to);
		Cell f = this.get(temp.from);
		if(t == null){
			//System.out.println("safe move being executed");
			this.set(temp.to, f);
			this.set(temp.from, null);
			return true;
		}
		return false;
	}
	
	public void demoSetup(){
		int lR = numRows/2;
		int lC = numCols/2;
		this.set(lR, lC, new Anthill(new int[]{lR, lC}));
		for(int ii = lR - 200; ii <= lR + 200; ii+=100){
			placeFoodTile(ii, lC-105);
			placeFoodTile(ii, lC+105);
		}
	}
	

	public void placeFoodTile(int x, int y){
		this.foodTiles.add(new FoodTile(x, y, 50, 50, this));
	}
	
	public void placeFoodBlock(int x, int y){
		int sx = 50;
		int sy = 50;
		for(int ii = x; ii < x+sx; ii++){
		for(int kk = y; kk < y+sy; kk++){
			this.set(ii, kk, new FoodCell());
		}}
	}
	
	public void removeWalls(){
		Board b = this;
		requestQueue.removeIf(new Predicate<Object>(){
			@Override
			public boolean test(Object t) {
				if(t instanceof Request){
					if(	((Request) t).to != null 	&& 
						b.get(((Request) t).to) instanceof SolidWall){
						return true;
					}
				}
				return false;
			}
		});
	}


	public void placeAnthill(){
		NumberGenerator.UniformGenerator uR = new NumberGenerator.UniformGenerator(0, numRows);
		NumberGenerator.UniformGenerator uC = new NumberGenerator.UniformGenerator(0, numCols);
		int x = uR.generate();
		int y = uC.generate();
		this.set(x, y, new Anthill(new int[]{x, y}));
	}

	int FOOD_MIN_SIZE = 8;
	int FOOD_MAX_SIZE = 25;
	public void placeFood(){
		NumberGenerator.UniformGenerator uR = new NumberGenerator.UniformGenerator(0, numRows);
		NumberGenerator.UniformGenerator uC = new NumberGenerator.UniformGenerator(0, numCols);
		NumberGenerator.UniformGenerator uS = new NumberGenerator.UniformGenerator(FOOD_MIN_SIZE, FOOD_MAX_SIZE);
		
		int x = uR.generate();
		int y = uC.generate();
		int sx = uS.generate();
		int sy = uS.generate();
		
		for(int ii = x; ii < x+sx; ii++){
		for(int kk = y; kk < y+sy; kk++){
			if(this.get(ii,kk) == null)
				this.set(ii, kk, new FoodCell());
		}}
	}

	//test thing for anthill future developent	
	int antHillRadius = 100;
	//put an ant in the middle
	public void centerSet(){
		NumberGenerator.UniformGenerator u = new NumberGenerator.UniformGenerator(0, antHillRadius);
		int lR = numRows/2;
		int lC = numCols/2;
		int numAnts = 100;
		while( numAnts > 0){
			int x = u.generate() + lR;
			int y = u.generate() + lC;
			if(this.get(x, y) == null){
				this.set(x, y, new Ant());
				numAnts--;
			}
		}

		for(int ii = lR + 100; ii < lR + 120; ii++){
		for(int kk = lC + 100; kk < lR + 120; kk++){
			this.set(ii, kk, new FoodCell());
		}}
		
		System.out.println("break");
	}
}
