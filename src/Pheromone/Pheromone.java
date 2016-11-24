package Pheromone;

import logic.NumberGenerator;
import logic.NumberGenerator.UniformGenerator;

public abstract class Pheromone {
	/*
	decayCounter : int
		at a minimum, this Pheromone will take decayCounter frames
		to fully decay
	PheromoneType : enum
	kernel
	decay() : boolean
		Implementations will drop the decay counter
		return true if we have fully decayed else false
	LinearDecayPheromone implements Pheromone
		A (possibly random) Pheromone that decays in a linear fashion
		RNG distribution
		random : boolean
		dDecay
		decay()
			simply subtract dDecay
	 */
	int decayCounter = 1000; //arbitrary
	int dDecay = 1;
	int pheromoneType = 0;
	public abstract boolean decay();
	public boolean hasDecayed(){
		return (decayCounter <= 0);
	}
	
	public static class LinearDecayPheromone extends Pheromone{
		
		//We can give a 1/(n) chance of actually decaying
		public boolean isRandom = false;
		private NumberGenerator generator = new NumberGenerator.UniformGenerator(0, 2); //1/3 default
		
		public LinearDecayPheromone(boolean r){
			isRandom = r;			
		}
		
		@Override
		public boolean decay() {
			int randomNumber = 1;
			if(isRandom){
				randomNumber = generator.generate();
			}
			if(randomNumber == 1){
				decayCounter -= dDecay;
			}
			return hasDecayed();
		}
		
	}
	
}
