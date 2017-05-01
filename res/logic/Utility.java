package logic;

public class Utility {
	public static int getWeightedRandomDirection(int[] weights){
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
}
