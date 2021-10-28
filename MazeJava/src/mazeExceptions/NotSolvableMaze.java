package mazeExceptions;

public class NotSolvableMaze extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotSolvableMaze(String string) {
		super(string);
	}

	public NotSolvableMaze() {
		super();
	}

}
