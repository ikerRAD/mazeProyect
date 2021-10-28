package Main;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import algoritmia.Maze;
import mazeExceptions.NotSolvableMaze;

public class Main {

	public static void main(String[] args) throws IOException {

		Maze m1 = null, m2 = null, m3 = null;
		try {
			m1 = new Maze(60);
		} catch (NotSolvableMaze e) {
			System.out.println("fallo en 1");
		}
		try {
			m2 = new Maze(40);
		} catch (NotSolvableMaze e) {
			System.out.println("fallo en 2");
		}
		try {
			m3 = new Maze(600);
		} catch (NotSolvableMaze e) {
			System.out.println("fallo en 3");
		}

		System.out.println("maze 1:\n" + m1);
		System.out.println("maze 2:\n" + m2);
		System.out.println("maze 3:\n" + m3);

		System.out.println("maze 1 solution:\n" + m1.toStringSolved());
		System.out.println("maze 2 solution:\n" + m2.toStringSolved());
		System.out.println("maze 3 solution:\n" + m3.toStringSolved());

		File file = new File("image1.jpg");
		ImageIO.write(m1.getImage(), "jpg", file);
		file = new File("image1solved.jpg");
		ImageIO.write(m1.getSolImage(), "jpg", file);

		file = new File("image2.jpg");
		ImageIO.write(m2.getImage(), "jpg", file);
		file = new File("image2solved.jpg");
		ImageIO.write(m2.getSolImage(), "jpg", file);

		file = new File("image3.jpg");
		ImageIO.write(m3.getImage(), "jpg", file);
		file = new File("image3solved.jpg");
		ImageIO.write(m3.getSolImage(), "jpg", file);

	}

}
