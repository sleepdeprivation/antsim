package logic;

import java.awt.Rectangle;

import Ant.FoodCell;
import Ant.SolidWall;

public class FoodTile {
	
	public static int howMany = 0;
	public int identifier = 0;

	public Rectangle boundary;// = new Rectangle();
	
	public int pointsRemaining = 2500;
	
	public FoodTile(int row, int column, int width, int height){
		identifier = howMany++;
		boundary = new Rectangle(row, column, width, height);
	}
	
	public FoodTile(int row, int column, int width, int height, CellGrid2D g) {
		identifier = howMany++;
		boundary = new Rectangle(row, column, width, height);
		initialize(g);
	}

	public boolean consumedFlag = false;
	
	public void initialize(CellGrid2D g){
		fillSolid(g);
		replenishPerimeter(g);
	}
	
	public int foodMargin = 20;

	public void fillSolid(CellGrid2D grid){
		for(int ii = boundary.x+foodMargin; ii < boundary.x + boundary.getWidth() - foodMargin;ii++){
		for(int kk = boundary.y+foodMargin; kk < boundary.y + boundary.getHeight() - foodMargin;kk++){
			grid.set(ii, kk, new SolidWall());
		}}
	}
	
	

	public void removeSolid(CellGrid2D grid){
		for(int ii = boundary.x+foodMargin; ii < boundary.x + boundary.getWidth() - foodMargin;ii++){
		for(int kk = boundary.y+foodMargin; kk < boundary.y + boundary.getHeight() - foodMargin;kk++){
			grid.set(ii, kk, null);
		}}
	}
		
	public void replenishPerimeter(CellGrid2D grid){
		for(int ii = boundary.x+foodMargin; ii < boundary.x + boundary.getWidth() - foodMargin;ii++){
				if(grid.get(ii, boundary.y + foodMargin) == null){
					grid.set(ii, boundary.y + foodMargin, new FoodCell());
					pointsRemaining--;
				}
				if(grid.get(ii, boundary.y + (int) boundary.getHeight() - foodMargin) == null){
					//grid.set(ii, boundary.y + (int) boundary.getHeight() - foodMargin, new FoodCell());
					pointsRemaining--;
				}
		}
		for(int kk = boundary.y+foodMargin; kk < boundary.y + boundary.getHeight() - foodMargin;kk++){
				if(grid.get(boundary.x+foodMargin, kk) == null){
					grid.set(boundary.x+foodMargin, kk, new FoodCell());
					pointsRemaining--;
				}
				if(grid.get(boundary.x + (int) boundary.getWidth()-foodMargin, kk) == null){
					//grid.set(boundary.x + (int) boundary.getWidth()-foodMargin, kk, new FoodCell());
					pointsRemaining--;
				}
		}
	}
		
	public void checkIfConsumed(){
		consumedFlag = pointsRemaining <= 0;
	}
	
	public boolean step(CellGrid2D g){
		replenishPerimeter(g);
		checkIfConsumed();
		if(consumedFlag){
			removeSolid(g);
			return true;
		}
		return false;
	}
}
