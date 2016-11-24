package logic;

/*
 * 
 */
public class CoordinatePair {
	public int[] pair = new int[2];
	

	public CoordinatePair() {};
	
	public CoordinatePair(int ii, int kk) {
		pair[0] = ii;
		pair[1] = kk;
	}

	@Override
	public int hashCode(){
		int result = 23;
		result = 37 * result + pair[0];
		result = 37 * result + pair[1];
		return result;
	}
	
	@Override
	public boolean equals(Object x){
		if(x instanceof CoordinatePair){
			return 	(
						((CoordinatePair) x).pair[0] == this.pair[0] &&
						((CoordinatePair) x).pair[1] == this.pair[1]
					);
		}
		return false;
	}
}
