package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Ant.Ant;
import Ant.Anthill;
import Ant.Cell;
import Ant.FoodCell;
import Pheromone.PheromoneCell;
import logic.Board;

/*
 * A class for extracting images from a Board object to be used in the UI
 */
public class BoardImageGenerator {
	
	public Board b;

	BufferedImage im;
	BufferedImage pIm;
	BufferedImage bgImage;
	BufferedImage[][] foodSprites;
	
	public BoardImageGenerator(Board bb){
		b = bb;
		im = new BufferedImage(b.getCols(), b.getRows(), BufferedImage.TYPE_INT_RGB);
		pIm = new BufferedImage(b.getCols(), b.getRows(), BufferedImage.TYPE_INT_ARGB);
		loadImages();
	}

	Color white = new Color(255, 255, 255);
	Color red = new Color(255, 0, 0);
	Color blue = new Color(0, 0, 255);
	Color green = new Color(0, 255, 0);
	Color black = new Color(0, 0, 0);
	Color ant = new Color(255, 0, 0);
	Color transparent = new Color(255, 255, 255, 0);
	
public void loadImages(){
	BufferedImage m = null;
	try {
		m = ImageIO.read(new File("res/ant_assets.png"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	foodSprites = new BufferedImage[4][4];
	//foods
	for(int ii = 0; ii < 3*50; ii+=50){
	for(int kk = 0; kk < 4*50; kk+=50){
		foodSprites[ii/50][kk/50] = m.getSubimage(ii, kk, 50, 50);
	}}
	
	bgImage = m.getSubimage(0, 200, 100, 100);
}
	
	/*
	final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);    

	Graphics2D g = dst.createGraphics();
	g.drawImage(src, x1, y1, x2, y2, null);
	g.dispose();

	ImageIO.write(dst, "PNG", new FileOutputStream("dst.png"));
	*/


public void drawBackground(BufferedImage onto){
	Graphics g = onto.createGraphics();
	for(int ii = 0; ii < b.getRows(); ii+=100){
	for(int kk = 0; kk < b.getCols(); kk+=100){
		g.drawImage(bgImage, ii, kk, null);
	}}
}

	
	public void whiteOut(){
		for(int ii = 0; ii < b.getRows(); ii++){
		for(int kk = 0; kk < b.getCols(); kk++){
			im.setRGB(ii, kk, white.getRGB());
		}}
	}
	
	/*
	 * print this onto an image
	 */
	public BufferedImage createIntegerBoard(){
		Cell x;
		whiteOut();
		//drawBackground(im);
		Graphics g = im.createGraphics();
		for(int ii = 0; ii < b.getRows(); ii++){
		for(int kk = 0; kk < b.getCols(); kk++){
			x = b.get(ii, kk);
			//im.setRGB(ii, kk, white.getRGB());
			if(x instanceof Ant)
				im.setRGB(ii, kk, ant.getRGB());
			else if(x instanceof FoodCell)
				im.setRGB(ii, kk, blue.getRGB());
			else if(x instanceof Anthill){
				//char[] s = Integer.toString(((Anthill) x).score).toCharArray();
				g.setColor(red);
			    g.setFont(new Font("TimesRoman", Font.PLAIN, 22)); 
				g.drawString(Integer.toString(((Anthill) x).score), ii, kk);
				//.drawChars(s, 0, s.length, ii, kk);
				//im.setRGB(ii, kk, green.getRGB());
				/*
				int r = ((Anthill)x).radius;
				int rows = b.getRows();
				int cols = b.getCols();
				im.setRGB(	Math.max(0,  ii - r)
							,Math.max(0, kk-r)
							,Math.min(2*r, rows-1)
							,Math.min(2*r, cols-1)
							,new int[]{green.getRGB()}, 0, 1);*/
			}
		}}
		drawFoodTiles(g);
		return im;
	}
	
	public void drawFoodTiles(Graphics g){
		for(int ii = 0; ii < b.foodTiles.size(); ii++){			
			BufferedImage[] row = this.foodSprites[(int) (4 - Math.ceil((double) b.foodTiles.get(ii).pointsRemaining/625.0))];
			BufferedImage sprite = row[b.foodTiles.get(ii).identifier%4];
			g.drawImage(sprite, b.foodTiles.get(ii).boundary.x, b.foodTiles.get(ii).boundary.y, null);
			//g.setColor(red);
		    //g.setFont(new Font("TimesRoman", Font.PLAIN, 22)); 
			g.drawString(Integer.toString(	b.foodTiles.get(ii).pointsRemaining),
											b.foodTiles.get(ii).boundary.x, 
											b.foodTiles.get(ii).boundary.y);
		}
	}

	/*
	 * print this onto an image
	 */
	public BufferedImage createPheromoneImage(){
		PheromoneCell ph;
		double pconcentration;
		for(int ii = 0; ii < b.getRows(); ii++){
		for(int kk = 0; kk < b.getCols(); kk++){
			ph = (PheromoneCell) b.pheromoneMap.get(ii, kk);
			if(ph != null){
				pconcentration = ph.getNormalizedPConcentration();
				//System.out.println(pconcentration);
				if(pconcentration > 0){
					pIm.setRGB(ii, kk, new Color(255, 0, 0, (int)(pconcentration*255)).getRGB());
				}else{
					//pIm.setRGB(ii, kk, transparent.getRGB());
				}
			}
		}}
		return pIm;
	}
	
}
