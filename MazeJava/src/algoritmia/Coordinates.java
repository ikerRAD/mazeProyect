package algoritmia;

public class Coordinates {

	public int i;
	public int j;

	public Coordinates(int i, int j) {
		this.i = i;
		this.j = j;
	}

	@Override
	public boolean equals(Object e) {
		if (e instanceof Coordinates) {
			Coordinates o = (Coordinates) e;
			return this.i == o.i && this.j == o.j;
		}
		return false;
	}

}
