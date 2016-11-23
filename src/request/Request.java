package request;

import logic.Cell;

public interface Request {
	Cell requestMaker = null;
	int[] from = new int[2];
	int[] to = new int[2];	
}
