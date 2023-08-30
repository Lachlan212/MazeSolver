This program was coded and designed in pairs for my second year Algorithms course. My partner and I collaboratively worked on
	MazeGenerator.java, mazeState.java and Node.java. I individually worked on MazeSolverBFS.java

There are 5 different Java files in this application. They work as follows:

MazeGenerator.java
	-Generates a random, 2D maze, of size m*n. This maze is written to a file (file name is decided upon user input).
	-The maze is written in form as determined by mazeState.java
	-To run this MazeGenerator:
		javac MazeGenerator.java
		java MazeGenerator {width} {height} {FileName}

mazeState.java
	-Converts randomly generated maze into a form usable by both MazeSolverBFS.java and MazeSolverDFS.java
	-This should compile and run alongside MazeGenerator.java. There is no need to compile this file yourself.
	-Maze state format is as follows:
		{width},{height}:{start_node}:{finish_node}:{cell_openness_list}
	//NOTE: Cell openness list is made up of integers that determine which walls of the node are open.
		-0: Both Closed, 1: Right only open, 2: Down only open, 3: both open

Node.java
	-A class containing logic and data of each node that make up a maze.
	-This will compile with MazeGenerator.java

MazeSolverBFS.java
	-Solves the maze using Breadth First Search. 
	-The maze solved is determined by user input
	-Maze solution is output to the screen, including NodePath, Number of Steps in solution, Number of Steps in the search, 
		time taken, and a visualisation of the maze solution.
	-To run MazeSolverBFS.java:
		javac MazeSolverBFS.java
		java MazeSolverBFS {randomly generated maze file name}
		//NOTE: Random maze must be generated before running BFS.

MazeSolverDFS.java
	-Solves the maze using Depth First Search
	-The maze solved is determined by user input
	-Maze solution is output to the screen, including NodePath, Number of Steps in solution, Number of Steps in the search, 
		time taken, and a visualisation of the maze solution.
	-To run MazeSolverBFS.java:
		javac MazeSolverDFS.java
		java MazeSolverDFS {randomly generated maze file name}
		//NOTE: Random maze must be generated before running BFS.