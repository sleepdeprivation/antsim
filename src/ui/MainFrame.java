package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import Ant.Ant;
import logic.Board;

public class MainFrame extends JPanel{
	
	ScheduledExecutorService clock;// = new ThreadPoolExecutor();
	Board board;
	BoardImageGenerator imageGenerator;
	public int NUMROWS = 800;
	public int NUMCOLS = 800;
	public int CLOCK_SPEED = 50; //in millis
	
	public Runnable runner = new Runnable(){

		int count = 0;
		Object lock = new Object();
		@Override
		public void run() {
			//synchronized(lock){
				//System.out.println("tick..." + (count++)%100);
				board.run();
				imageGenerator.createIntegerBoard();
				imageGenerator.createPheromoneImage();
				repaint();
			//}
		}

	};
	
	public MainFrame() {
		setUpWindow();
		board = new Board(NUMROWS, NUMCOLS);
		imageGenerator = new BoardImageGenerator(board);
		imageGenerator.createIntegerBoard();
		imageGenerator.createPheromoneImage();

		//keyRun();
		clockRun();
	}
	
	public void keyRun(){
		while(true){
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			runner.run();
		}
	}
	
	public void clockRun(){
		clock = Executors.newSingleThreadScheduledExecutor();
		clock.scheduleAtFixedRate(
				    runner,
				    0,
				    CLOCK_SPEED, TimeUnit.MILLISECONDS);
	}

	public void setUpWindow(){
	      JPanel main = new JPanel();
	      main.setPreferredSize(new Dimension(25, 400));
	      main.setLayout(new FlowLayout());
	      JFrame frame = new JFrame();
	      frame.pack();
	      frame.setLocationRelativeTo(null);
	      // frame.setContentPane(main);
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.setSize(new Dimension(800,800));
	      frame.setLocationRelativeTo(null);
	      frame.setVisible(true);
	      frame.add(this);
	  }
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(imageGenerator.im, 0, 0, null);
		g.drawImage(imageGenerator.pIm, 0, 0, null);		
	}
	
	  
	public static void main(String[] args) throws IOException {
		MainFrame frame = new MainFrame();
		//wheelTest();
		//int[] one = new int[]{ 1 , 2 } ;
		//int[] two = new int[]{ 1 , 2 } ;
		//System.out.println(Arrays.equals(one, two));
	}
	
	public static void wheelTest(){
		/*int[][] i = Ant.getEmpiricalDist();
		for(int ii = 0; ii < i.length; ii++){
		for(int kk = 0; kk < i[ii].length; kk++){
			System.out.print(i[ii][kk]);
		}System.out.println("");}*/
	}
	
	public void empTest(){
		for(int ii = 0; ii < 100; ii++){
			//int i = Ant.getWeightedRandomDirection(new int[]{20, 20});
			//System.out.println(i);
		}
	}
}
