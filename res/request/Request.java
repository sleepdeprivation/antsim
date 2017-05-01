package request;

import Ant.Cell;

public class Request implements Comparable<Request>{
	public Cell requestMaker = null;
	public int[] from = new int[2];
	public int[] to = new int[2];
	public int priority = 0;
	
	public Request(Cell m, int[] f, int[] t){
		requestMaker = m;
		from = f;
		to = t;
	}

	@Override
	public int compareTo(Request o) {
		return this.priority - o.priority;
	}
	
	
	
}
