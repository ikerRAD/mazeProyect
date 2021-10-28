package algoritmia;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import mazeExceptions.NotSolvableMaze;
import stru.exceptionss.ElementNotFoundException;
import stru.exceptionss.EmptyCollectionException;
import stru.lists.LinkedList;
import stru.stacks.GenericLinkedStack;

/**
 * 
 * class defined to reference a maze
 * 
 * @author ikerPG
 *
 */
public class Maze {

	private final static int UNVISITED = -1;
	private final static int WALL = 0;
	private final static int CELL = 1;

	private int[][] maze;
	private int[][] maze_solved;

	private Coordinates start;
	private Coordinates end;

	private BufferedImage mazeImg;
	private BufferedImage mazeSolvdImg;

	/**
	 * Constructor that given a maze, lines and columns of itself and start and end
	 * points. Creates a Maze object if it is solvable
	 * 
	 * @param maze:  the maze matrix
	 * @param lines: lines of the maze matrix
	 * @param cols:  columns of the maze matrix
	 * @param start: starting point of the maze
	 * @param end:   ending point of the maze
	 * @throws NotSolvableMaze if the maze has no solution
	 */
	public Maze(int[][] maze, Coordinates start, Coordinates end) throws NotSolvableMaze {
		super();

		this.maze_solved = solveMaze(maze, start, end);
		this.maze = maze;
		this.start = start;
		this.end = end;
		BufferedImage[] img = generateImages();
		this.mazeImg = img[0];
		this.mazeSolvdImg = img[1];
	}

	/**
	 * Creates a random square maze of size 10x10
	 * 
	 * @throws NotSolvableMaze if the created maze has no solution
	 */
	public Maze() throws NotSolvableMaze {
		this(10);
	}

	/**
	 * creates a random square maze of size dim x dim
	 * 
	 * @param dims dimensions of the maze matrix
	 * @throws NotSolvableMaze if the created maze has no solution
	 */
	public Maze(int dims) throws NotSolvableMaze {
		this(dims, dims);
	}

	/**
	 * Constructor that given lines and columns creates a random maze with the
	 * specified lines and columns. If the lines or columns are less than 10, they
	 * will be automatically set to 10
	 * 
	 * @param lines: the lines of the maze matrix
	 * @param cols:  the columns of the lines matrix
	 * @throws NotSolvableMaze when the randomly generated maze is not solvable, we
	 *                         expect never to have this error
	 */
	public Maze(int lines, int cols) throws NotSolvableMaze {
		super();
		if (lines < 10) {
			lines = 10;
		}
		if (cols < 10) {
			cols = 10;
		}

		int[][] maze = Maze.GenerateRandomMaze(lines, cols);
		// Set the start
		for (int i = 0; i < cols; i++) {
			if (maze[1][i] == CELL) {
				maze[0][i] = CELL;
				this.start = new Coordinates(0, i);
				break;
			}
		}

		// Set the end
		for (int i = cols - 1; i >= 0; i--) {
			if (maze[lines - 2][i] == CELL) {
				maze[lines - 1][i] = CELL;
				this.end = new Coordinates(lines - 1, i);
				break;
			}
		}

		this.maze_solved = solveMaze(maze, this.start, this.end);
		this.maze = maze;
		BufferedImage[] img = generateImages();

		this.mazeImg = img[0];
		this.mazeSolvdImg = img[1];

	}

	public int[][] getMaze() {
		return maze;
	}

	public int[][] getSolvedMaze() {
		return maze_solved;
	}

	public Coordinates getEnd() {
		return end;
	}

	public Coordinates getStart() {
		return start;
	}

	public BufferedImage getImage() {
		return mazeImg;
	}

	public BufferedImage getSolImage() {
		return mazeSolvdImg;
	}

	/**
	 * Creates the images of the mazes. index 0: image of the maze index 1: image of
	 * the solved maze
	 * 
	 * @return an array of the two images
	 */
	public BufferedImage[] generateImages() {
		BufferedImage[] img = new BufferedImage[2];

		int height = maze.length * 4;
		int width = maze[0].length * 4;

		img[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		img[1] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D g0 = img[0].createGraphics();
		Graphics2D g1 = img[1].createGraphics();
		g0.setColor(Color.black);
		g1.setColor(Color.black);
		g0.fillRect(0, 0, width, height);
		g1.fillRect(0, 0, width, height);

		g0.setColor(Color.white);
		g1.setColor(Color.red);

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				if (maze[i][j] == 1) {
					g0.fillRect(j * 4, i * 4, 4, 4);
					if (maze_solved[i][j] == 3) {
						g1.fillRect(j * 4, i * 4, 4, 4);
					} else {
						g1.setColor(Color.white);
						g1.fillRect(j * 4, i * 4, 4, 4);
						g1.setColor(Color.red);
					}
				}

			}
		}

		g0.dispose();
		g1.dispose();

		return img;
	}

	/**
	 * Calculates how many cells surround a wall
	 * 
	 * @param rnd_wall: the random wall
	 * @param maze:     the maze
	 * @param lines:    the height of the maze
	 * @return the number of cells that surround a random wall
	 */
	private static int surroundingCells(Coordinates rnd_wall, int[][] maze, int lines) {
		int srdc = 0;
		if (rnd_wall.i != 0)
			if (maze[rnd_wall.i - 1][rnd_wall.j] == CELL) {
				srdc++;
			}
		if (rnd_wall.i != maze.length - 1)
			if (maze[rnd_wall.i + 1][rnd_wall.j] == CELL) {
				srdc++;
			}
		if (rnd_wall.j != 0)
			if (maze[rnd_wall.i][rnd_wall.j - 1] == CELL) {
				srdc++;
			}
		if (rnd_wall.i != maze[0].length - 1)
			if (maze[rnd_wall.i][rnd_wall.j + 1] == CELL) {
				srdc++;
			}

		return srdc;
	}

	/**
	 * static method to generate a random maze, the given maze will need to be
	 * settled the starting and ending point
	 * 
	 * @param lines: lines of the random maze matrix
	 * @param cols:  columns of the random maze matrix
	 * @return the random maze matrix
	 */
	@SuppressWarnings("finally")
	private static int[][] GenerateRandomMaze(int lines, int cols) {
		// if some value is not valid, we change it

		int[][] maze = new int[lines][cols];

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				maze[i][j] = UNVISITED;
			}
		}

		Random rnd = new Random();

		int starting_i = rnd.nextInt(lines - 2) + 1; // between 1 and lines-1
		int starting_j = rnd.nextInt(cols - 2) + 1; // between 1 and cols-1

		maze[starting_i][starting_j] = CELL;
		LinkedList<Coordinates> walls = new LinkedList<Coordinates>();

		walls.addToTail(new Coordinates(starting_i - 1, starting_j));
		walls.addToTail(new Coordinates(starting_i, starting_j - 1));
		walls.addToTail(new Coordinates(starting_i, starting_j + 1));
		walls.addToTail(new Coordinates(starting_i + 1, starting_j));

		maze[starting_i + 1][starting_j] = WALL;
		maze[starting_i - 1][starting_j] = WALL;
		maze[starting_i][starting_j + 1] = WALL;
		maze[starting_i][starting_j - 1] = WALL;

		Coordinates rnd_wall;
		int scells;

		while (!walls.isEmpty()) {
			rnd_wall = walls.get(rnd.nextInt(walls.size()));

			if (rnd_wall.j != 0) {
				if (maze[rnd_wall.i][rnd_wall.j - 1] == UNVISITED && maze[rnd_wall.i][rnd_wall.j + 1] == CELL) {
					scells = surroundingCells(rnd_wall, maze, lines);
					if (scells < 2) {
						maze[rnd_wall.i][rnd_wall.j] = CELL;

						// upper cell
						if (rnd_wall.i != 0) {
							if (maze[rnd_wall.i - 1][rnd_wall.j] != CELL) {
								maze[rnd_wall.i - 1][rnd_wall.j] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i - 1, rnd_wall.j))) {
								walls.addToTail(new Coordinates(rnd_wall.i - 1, rnd_wall.j));
							}
						}

						// Bottom cell
						if (rnd_wall.i != lines - 1) {
							if (maze[rnd_wall.i + 1][rnd_wall.j] != CELL) {
								maze[rnd_wall.i + 1][rnd_wall.j] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i + 1, rnd_wall.j))) {
								walls.addToTail(new Coordinates(rnd_wall.i + 1, rnd_wall.j));
							}
						}

						// Leftmost cell
						if (rnd_wall.j != 0) {
							if (maze[rnd_wall.i][rnd_wall.j - 1] != CELL) {
								maze[rnd_wall.i][rnd_wall.j - 1] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i, rnd_wall.j - 1))) {
								walls.addToTail(new Coordinates(rnd_wall.i, rnd_wall.j - 1));
							}
						}
					}
					try {
						while (walls.contains(rnd_wall)) {

							walls.remove(rnd_wall);

						}
					} catch (EmptyCollectionException | ElementNotFoundException e) {

					} finally {
						continue;
					}

				}
			}

			if (rnd_wall.i != 0) {

				if (maze[rnd_wall.i - 1][rnd_wall.j] == UNVISITED && maze[rnd_wall.i + 1][rnd_wall.j] == CELL) {
					scells = surroundingCells(rnd_wall, maze, lines);
					if (scells < 2) {
						maze[rnd_wall.i][rnd_wall.j] = CELL;

						// upper cell
						if (rnd_wall.i != 0) {
							if (maze[rnd_wall.i - 1][rnd_wall.j] != CELL) {
								maze[rnd_wall.i - 1][rnd_wall.j] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i - 1, rnd_wall.j))) {
								walls.addToTail(new Coordinates(rnd_wall.i - 1, rnd_wall.j));
							}
						}

						// Rightmost cell
						if (rnd_wall.j != cols - 1) {
							if (maze[rnd_wall.i][rnd_wall.j + 1] != CELL) {
								maze[rnd_wall.i][rnd_wall.j + 1] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i, rnd_wall.j + 1))) {
								walls.addToTail(new Coordinates(rnd_wall.i + 1, rnd_wall.j + 1));
							}
						}

						// Leftmost cell
						if (rnd_wall.i != 0) {
							if (maze[rnd_wall.i][rnd_wall.j - 1] != CELL) {
								maze[rnd_wall.i][rnd_wall.j - 1] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i, rnd_wall.j - 1))) {
								walls.addToTail(new Coordinates(rnd_wall.i, rnd_wall.j - 1));
							}
						}
					}

					try {
						while (walls.contains(rnd_wall)) {

							walls.remove(rnd_wall);

						}
					} catch (EmptyCollectionException | ElementNotFoundException e) {

					} finally {
						continue;
					}
				}

			}

			if (rnd_wall.i != lines - 1) {

				if (maze[rnd_wall.i + 1][rnd_wall.j] == UNVISITED && maze[rnd_wall.i - 1][rnd_wall.j] == CELL) {
					scells = surroundingCells(rnd_wall, maze, lines);
					if (scells < 2) {
						maze[rnd_wall.i][rnd_wall.j] = CELL;

						// lower cell
						if (rnd_wall.i != lines - 1) {
							if (maze[rnd_wall.i + 1][rnd_wall.j] != CELL) {
								maze[rnd_wall.i + 1][rnd_wall.j] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i + 1, rnd_wall.j))) {
								walls.addToTail(new Coordinates(rnd_wall.i + 1, rnd_wall.j));
							}
						}

						// Rightmost cell
						if (rnd_wall.j != cols - 1) {
							if (maze[rnd_wall.i][rnd_wall.j + 1] != CELL) {
								maze[rnd_wall.i][rnd_wall.j + 1] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i, rnd_wall.j + 1))) {
								walls.addToTail(new Coordinates(rnd_wall.i + 1, rnd_wall.j + 1));
							}
						}

						// Leftmost cell
						if (rnd_wall.i != 0) {
							if (maze[rnd_wall.i][rnd_wall.j - 1] != CELL) {
								maze[rnd_wall.i][rnd_wall.j - 1] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i, rnd_wall.j - 1))) {
								walls.addToTail(new Coordinates(rnd_wall.i, rnd_wall.j - 1));
							}
						}
					}

					try {
						while (walls.contains(rnd_wall)) {

							walls.remove(rnd_wall);

						}
					} catch (EmptyCollectionException | ElementNotFoundException e) {

					} finally {
						continue;
					}
				}

			}

			if (rnd_wall.j != cols - 1) {

				if (maze[rnd_wall.i][rnd_wall.j + 1] == UNVISITED && maze[rnd_wall.i][rnd_wall.j - 1] == CELL) {
					scells = surroundingCells(rnd_wall, maze, lines);
					if (scells < 2) {
						maze[rnd_wall.i][rnd_wall.j] = CELL;

						// upper cell
						if (rnd_wall.i != 0) {
							if (maze[rnd_wall.i - 1][rnd_wall.j] != CELL) {
								maze[rnd_wall.i - 1][rnd_wall.j] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i - 1, rnd_wall.j))) {
								walls.addToTail(new Coordinates(rnd_wall.i - 1, rnd_wall.j));
							}
						}

						// Bottom cell
						if (rnd_wall.i != lines - 1) {
							if (maze[rnd_wall.i + 1][rnd_wall.j] != CELL) {
								maze[rnd_wall.i + 1][rnd_wall.j] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i + 1, rnd_wall.j))) {
								walls.addToTail(new Coordinates(rnd_wall.i + 1, rnd_wall.j));
							}
						}

						// Rightmost cell
						if (rnd_wall.j != cols - 1) {
							if (maze[rnd_wall.i][rnd_wall.j + 1] != CELL) {
								maze[rnd_wall.i][rnd_wall.j + 1] = WALL;
							}
							if (!walls.contains(new Coordinates(rnd_wall.i, rnd_wall.j + 1))) {
								walls.addToTail(new Coordinates(rnd_wall.i, rnd_wall.j + 1));
							}
						}
					}

					try {
						while (walls.contains(rnd_wall)) {

							walls.remove(rnd_wall);

						}
					} catch (EmptyCollectionException | ElementNotFoundException e) {

					} finally {
						continue;
					}
				}

			}

			try {
				while (walls.contains(rnd_wall)) {

					walls.remove(rnd_wall);

				}
			} catch (EmptyCollectionException | ElementNotFoundException e) {

			} finally {
				continue;
			}

		}

		// mark remaining unvisited cells as walls
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				if (maze[i][j] == UNVISITED) {
					maze[i][j] = WALL;
				}
			}
		}

		return maze;
	}

	/**
	 * 0 - WALL 1 - CELL 2 - PASSED 3 - PATH
	 * 
	 * @param mazeprc: the maze to solve
	 * @param start:   coordinate of the start of the maze
	 * @param end:     coordinate of the end of the maze
	 * @return the maze but 'painted' with the new values PASSED and PATH
	 * @throws NotSolvableMaze when the maze has not solution
	 */
	private int[][] solveMaze(int[][] mazeprv, Coordinates start, Coordinates end) throws NotSolvableMaze {

		int[][] maze = new int[mazeprv.length][mazeprv[0].length];

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				maze[i][j] = mazeprv[i][j];
			}
		}

		// if the indexes of the start and end are out of bounds
		if (maze.length <= start.i || start.i < 0 || maze[0].length <= start.j || start.j < 0 || maze.length <= end.i
				|| end.i < 0 || maze[0].length <= end.j || end.j < 0) {
			throw new NotSolvableMaze("The indexes of the start or end are not in the maze");
		}

		if (maze[start.i][start.j] == WALL || maze[end.i][end.j] == WALL) {
			throw new NotSolvableMaze("The start or the end do not belong to the path");
		}

		Coordinates current = new Coordinates(start.i, start.j);
		GenericLinkedStack<Coordinates> stack = new GenericLinkedStack<Coordinates>();

		maze[current.i][current.j] = 2;
		stack.push(current);

		while (!stack.isEmpty()) {

			current = stack.peek();

			if (maze[current.i][current.j] == 3) {
				stack.pop();
				maze[current.i][current.j] = 2;
			} else {
				maze[current.i][current.j] = 3;
			}

			// check if there is a solution
			if (end.i == current.i && end.j == current.j) {
				return maze;
			}

			if (current.j - 1 >= 0) {
				if (maze[current.i][current.j - 1] == 1) {
					stack.push(new Coordinates(current.i, current.j - 1));
				}
			}
			if (current.i - 1 >= 0) {
				if (maze[current.i - 1][current.j] == 1) {
					stack.push(new Coordinates(current.i - 1, current.j));
				}
			}
			if (current.i + 1 < maze.length) {
				if (maze[current.i + 1][current.j] == 1) {
					stack.push(new Coordinates(current.i + 1, current.j));
				}
			}
			if (current.j + 1 < maze[0].length) {
				if (maze[current.i][current.j + 1] == 1) {
					stack.push(new Coordinates(current.i, current.j + 1));
				}
			}
		}

		throw new NotSolvableMaze("the maze has no solution");

	}

	@Override
	public String toString() {
		String str = "";

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				str += maze[i][j] + " ";
			}
			str += "\n";
		}

		return str;
	}

	public String toStringSolved() {
		String str = "";

		for (int i = 0; i < maze_solved.length; i++) {
			for (int j = 0; j < maze_solved[0].length; j++) {
				str += maze_solved[i][j] + " ";
			}
			str += "\n";
		}

		return str;
	}

	@Override
	public boolean equals(Object e) {

		if (e == null)
			return false;

		if (e instanceof Maze) {
			Maze o = (Maze) e;
			if (o.getMaze().length != maze.length || o.getMaze()[0].length != maze[0].length) {
				return false;
			} else {
				for (int i = 0; i < maze.length; i++) {
					for (int j = 0; j < maze[0].length; j++) {
						if (maze[i][j] != o.getMaze()[i][j]) {
							return false;
						}
					}
				}
			}
		} else if (e instanceof int[][]) {
			int[][] o = (int[][]) e;
			if (o.length != maze.length || o[0].length != maze[0].length) {
				return false;
			} else {
				for (int i = 0; i < maze.length; i++) {
					for (int j = 0; j < maze[0].length; j++) {
						if (maze[i][j] != o[i][j]) {
							return false;
						}
					}
				}
			}
		} else {
			return false;
		}

		return true;

	}

}
