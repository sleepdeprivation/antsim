package ui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import Ant.Ant;
import logic.Board;

/*
 * A class for extracting images from a Board object to be used in the UI
 */
public class BoardImageGenerator {
	
	public Board b;

	BufferedImage im;
	BufferedImage pIm;
	
	public BoardImageGenerator(Board bb){
		b = bb;
		im = new BufferedImage(b.getCols(), b.getRows(), BufferedImage.TYPE_INT_RGB);
		pIm = new BufferedImage(b.getCols(), b.getRows(), BufferedImage.TYPE_INT_ARGB);
	}

	Color white = new Color(255, 255, 255);
	Color red = new Color(255, 0, 0);
	Color black = new Color(0, 0, 0);
	Color transparent = new Color(255, 255, 255, 0);

	/*
	 * print this onto an image
	 */
	public BufferedImage createIntegerBoard(){
		for(int ii = 0; ii < b.getRows(); ii++){
		for(int kk = 0; kk < b.getCols(); kk++){
			if(b.get(ii, kk) instanceof Ant)
				im.setRGB(ii, kk, black.getRGB());
			else
				im.setRGB(ii, kk, white.getRGB());
		}}
		return im;
	}

	/*
	 * print this onto an image
	 */
	public BufferedImage createPheromoneImage(){
		int pconcentration;
		for(int ii = 0; ii < b.getRows(); ii++){
		for(int kk = 0; kk < b.getCols(); kk++){
			pconcentration = b.pheromoneMap.getConcentration(ii, kk);
			if(pconcentration > 0){
				int magic_number = (int)((double)pconcentration/1000.0*255.0);
				System.out.println("pconc " + pconcentration);
				System.out.println("magic " + magic_number);
				pIm.setRGB(ii, kk, new Color(255, 0, 0, magic_number).getRGB());
			}else{
				pIm.setRGB(ii, kk, transparent.getRGB());
			}
		}}
		return pIm;
	}
	
}
