package request;

import Ant.Cell;


public class MoveRequest extends Request {
	public MoveRequest(Cell r, int[] f, int[] t){
		requestMaker = r;
		from = f;
		to = t;
	}
}
