package logic;

import java.util.Random;

public interface NumberGenerator {
	Random random = new Random();
	public int generate();
	
	public static class UniformGenerator
			implements NumberGenerator
	{
		int bottom;
		int top;
		public UniformGenerator(int a, int b){
			if(a > b){
				int temp = a;
				a = b;
				b = temp;
			}
			bottom = a;
			top = b;
		}
		@Override
		public int generate(){
			return random.nextInt(top - bottom) + bottom;
		}
	}
}
