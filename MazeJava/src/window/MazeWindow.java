package window;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;

import algoritmia.Coordinates;
import algoritmia.Maze;
import mazeExceptions.NotSolvableMaze;
import stru.stacks.GenericLinkedStack;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ImageScroller scroller;
	private GenericLinkedStack<Coordinates> path; 
	private Maze maze;
	private MazeWindow self;
	
	
	private class ImageScroller extends JScrollPane 
    {
         /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ImageScroller(Icon icon, Coordinates coord) 
         {
              super();
  
             // Panel to hold the icon image 
             JPanel p = new JPanel(new BorderLayout()); 
             p.add(new JLabel(icon)); 
             getViewport().add(p);
       
             JScrollBar vsb = getVerticalScrollBar(); 
             JScrollBar hsb = getHorizontalScrollBar(); 
             int ii = coord.i*10;
             int jj = coord.j*10;
             if(ii-250 < 0) {
            	 ii = 0;
             }else {
            	 ii=ii-250;
             }
             
             if(jj-250 < 0) {
            	 jj = 0;
             }else {
            	 jj = jj-250;
             }
             vsb.setMaximum(ii);
             hsb.setMaximum(jj);
             vsb.setValue(ii); 
             hsb.setValue(jj);
            
         }//end of constructor
    }
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MazeWindow frame = new MazeWindow(30, 30);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws NotSolvableMaze 
	 */
	public MazeWindow(int i, int j) throws NotSolvableMaze {
		self = this;
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				Coordinates current = path.peek();
				Coordinates proposed = null;
				
				switch (e.getKeyCode()) {
				case 87:
				case 38: //arriba
					proposed = new Coordinates(current.i-1, current.j);
					break;
				case 65:
				case 37://izq
					proposed = new Coordinates(current.i, current.j-1);
					break;
				case 83:
				case 40://abajo
					proposed = new Coordinates(current.i+1, current.j);
					break;
				case 68:
				case 39://dcha
					proposed = new Coordinates(current.i, current.j+1);
					break;
				default:
					
					proposed = new Coordinates(-1, -1);
					break;
				}
				
				boolean changes = false;
				//dentro de los márgenes
				if (!(proposed.i < 0 || proposed.i == maze.getMaze().length || proposed.j < 0 || proposed.j == maze.getMaze()[0].length)) {
					//si es el final
					if(proposed.equals(maze.getEnd())) {
						self.setVisible(false);
						self.dispose();
					//si no es el inicio
					}else if(!proposed.equals(maze.getStart())) {
						int[][] aux = maze.getMaze();
						//si se puede pasar
						if(aux[proposed.i][proposed.j] == 1) {
							path.push(proposed);
							changes = true;
							maze.setMaze(proposed, 2);
							maze.paintPath(proposed);
						//si es justo el paso anterior
						}else if((aux[proposed.i][proposed.j] == 2)) {
							path.pop();
							if(path.peek().equals(proposed)) {
								maze.erasePath(current);
								maze.setMaze(current, 1);
								changes = true;
							}else {
								path.push(current);
							}
						}
					}
				}
				
				if(changes) {
					contentPane.remove(scroller);
					scroller = new ImageScroller(new ImageIcon(maze.getImage()), proposed);
					contentPane.add(scroller, BorderLayout.CENTER);
					contentPane.revalidate();
				}
				
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 864, 498);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		maze = new Maze(i, j);

		path = new GenericLinkedStack<Coordinates>();
		path.push(maze.getStart());
		maze.setMaze(maze.getStart(), 2);
		maze.paintPath(maze.getStart());
		
		scroller = new ImageScroller(new ImageIcon(maze.getImage()), maze.getStart());
		contentPane.add(scroller, BorderLayout.CENTER);
		
	}
}
