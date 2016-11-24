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

import logic.Board;

public class MainFrame extends JPanel{
	
	ScheduledExecutorService clock;// = new ThreadPoolExecutor();
	Board board;
	BufferedImage blankImage;
	BufferedImage im;
	public int NUMROWS = 1000;
	public int NUMCOLS = 1000;
	
	public Runnable runner = new Runnable(){

		@Override
		public void run() {
			System.out.println("tick...");
			board.stepAllCells();
			board.createIntegerBoard(im);
			repaint();
		}

	};
	
	public MainFrame() {
		setUpWindow();
		board = new Board(NUMROWS, NUMCOLS);
		blankImage = new BufferedImage(NUMCOLS, NUMROWS, BufferedImage.TYPE_INT_RGB);
		im = new BufferedImage(NUMCOLS, NUMROWS, BufferedImage.TYPE_INT_RGB);
		clock = Executors.newSingleThreadScheduledExecutor();
		clock.scheduleAtFixedRate(
				    runner,
				    0,
				    10, TimeUnit.MILLISECONDS);
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
		g.drawImage(im, 0, 0, null);
	}
	
	  
	public static void main(String[] args) throws IOException {
		//MainFrame frame = new MainFrame();
		int[] one = new int[]{ 1 , 2 } ;
		int[] two = new int[]{ 1 , 2 } ;
		System.out.println(Arrays.equals(one, two));
	} 
}
