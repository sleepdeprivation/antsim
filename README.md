```Cell (Interface)
	neighborhood : Neighborhood
		A slice of overall grid that this cell knows about
	cooldownCounter : int
		How long until we run the step() function again?
		This will be set from the output of cooldownDistribution
	loadNeighboorhood(Cell[][], location int[2])
		Implementations will load some neighborhood around them
		into the neighborhood field so the step function can
		react accordingly
		location is in absolute coordinates
	step(RequestQueue)
		Using information contained in neighborhood, we may make
		an arbitrary number of Requests to the given RequestQueue
	cooldownDistribution : RNG
		The random distribution used to reload our cooldownCounter
		We can set a different cooldown distribution based on our
		actions in step()
	eject() : Cell
		Remove the contents of this cell, whatever that means
	insert(Cell)
		Call eject() on the Cell and insert whatever
		it returns into this cell, if possible

Pheromone (Interface)
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

PheromoneFactory
	Create Pheromones of the given type using the given class
	pheromoneType : PheromoneType
	pheromoneClass : Class<Pheromone>
	generate(Intensity) : Pheromone

PheromoneKernel (Interface)
	Controls and creates pheromones
	pheromoneList : ArrayList<Pheromone>
	boundingBox : int[] //x1,y1,x2,y2
	generatePheromone(x, y)
		used internally from apply
	apply(Cell[][] grid)
		apply this PheromoneKernel to the grid
	decay()
		decay all the pheromones

PheromoneBaseCell
	Standard cell that has a place for Pheromones and
	another cell
	Overloads eject() and insert() to mimic a regular cell,
	ignoring the Pheromones
	ArrayList<Pheromone> pheremoneList
	sumPheromone(PheromoneType) int
		add up all the pheromonetypes
	sumAllPheromones() int[]
		perform sumPheromone for all pheromonetypes

RNG (Interface)
	An interface for wrapping arbitrary random number distributions
	<params for distribution>
	getInteger()
		We will always want an integer out of this
	
Request (Interface)
	A wrapper for different kinds of requests, among these are :
	MovementRequest
		move the cell in `from` to the cell in `to`
		from	: int[2]
		to	: int[2]
	DamageRequest
		the cell in `from` wants to damage the cell in `to`
		from	: int[2]
		to	: int[2]
		
Neighborhood (Class)
	A collection of cells and their coordinates
	Neighborhood(Cell[][], row1, col1, row2, col2, origin)
		Loop through the grid and create the neighborhood
		consider creating both upfront	
	LabeledCell (Class)
		cell : Cell
		coordinates : int[2]
	origin : int[2]
		the centerpoint for the relative labeling
	getLabeledRelative() : LabeledCell[][]
		return a square array of cells labeled relative to the origin
	getLabeledAbsolute() : LabeledCell[][]
		return a square array of cells labeled with their absolute coordinates
	relative2Absolute(const LabeledCell) : LabeledCell
		change the coordinates of the LabeledCell to
		absolute coordinates (ie add the origin)
		does not modify the original
	absolute2Relative(const LabeledCell) : LabeledCell
		change the coordinates of the LabeledCell to
		relative coordinates (ie subtract the origin)
		does not modify the original

Board (Class)
	grid : Cell[][]
	numRows : int
	numCols : int
	timerInterval : int
		How often to step all cells
	requestQueue  : PriorityQueue<Request>
		A queue of requests made by the cells during stepAllCells
	pheromoneKernelList : ArrayList<PheromoneKernel>
	run()
		performs:
		stepAllCells()
			go through all cells and
				cell.loadNeighborhood and place it in the cell,
				perform their step() function with the requestqueue
		processRequests()
			go through all requests and perform them on the grid
		processPheromones()
			decay all the PheromoneKernels





```



































