import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int startx = 50, starty = -20;
	private int width = 150,
				height = 340,
				x = startx,
				y = starty,
				squareH = 20,
				squareW = 20,
				backx = 0,
				backy = 0,
				score = 0,
				shape = 0, 
				nextShape = (int) (Math.random() * 19);
	private ArrayList<Point> shapeList = new ArrayList<Point>();
	private ArrayList<Color> colorList = new ArrayList<Color>();
	private boolean rotate = false,
					first = true,
					disableDown = false;
	private Color[] colors = { 
			Color.MAGENTA, 
			Color.green, 
			Color.BLUE, 
			Color.RED, 
			Color.ORANGE, 
			Color.YELLOW 
			};
	private Color colorShape = null, 
			nextColor = colors[(int) (Math.random() * colors.length)];
	private boolean[][] isFull = new boolean[17][10];
	private Timer t = new Timer(500, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			y += squareH;
			checkIfContinueDown();
			checkToEraseLine();
			if (checkForGameOver()) {
				t.stop();
				startOver();
			}
			switch (getMaximum()) {
			case 12:
				t.setDelay(400);
				break;
			case 8:
				t.setDelay(300);
				break;
			case 4:
				t.setDelay(200);
				break;
			default:
				t.setDelay(500);
				break;
			}
			repaint();
		}
	});

	private Point[] pattern = { 
			new Point(startx, starty), // 0
			new Point(startx + squareW, starty), // 1
			new Point(startx + squareW * 2, starty), // 2
			new Point(startx + squareW * 3, starty), // 3
			new Point(startx, starty + squareW), // 4
			new Point(startx + squareW, starty + squareW), // 5
			new Point(startx + squareW * 2, starty + squareW), // 6
			new Point(startx + squareW * 3, starty + squareW), // 7
			new Point(startx, starty + squareW * 2), // 8
			new Point(startx + squareW, starty + squareW * 2), // 9
			new Point(startx + squareW * 2, starty + squareW * 2), // 10
			new Point(startx + squareW * 3, starty + squareW * 2), // 11
			new Point(startx, starty + squareW * 3), // 12
			new Point(startx + squareW, starty + squareW * 3), // 13
			new Point(startx + squareW * 2, starty + squareW * 3), // 14
			new Point(startx + squareW * 3, starty + squareW * 3)// 15
	};

	private int[][] shapes = { { 0, 4, 8, 12 }, // I // n=0
			{ 12, 13, 14, 15 }, // Rotated I //n=1
			{ 9, 10, 12, 13 }, // S //n=2
			{ 4, 8, 9, 13 }, // Rotated S //n=3
			{ 8, 9, 13, 14 }, // Z // n=4
			{ 5, 8, 9, 12 }, // Rotated Z // n=5
			{ 8, 9, 10, 13 }, // T // n=6
			{ 9, 12, 13, 14 }, // T Rot 1 // n=7
			{ 5, 8, 9, 13 }, // T Rot 2 // n=8
			{ 4, 8, 9, 12 }, // T Rot 3 // n=9
			{ 5, 9, 13, 14 }, // L // n=10
			{ 10, 12, 13, 14 }, // L Rot 1 // n=11
			{ 4, 5, 9, 13 }, // L Rot 2 // n=12
			{ 8, 9, 10, 12 }, // L Rot 3 // n=13
			{ 5, 9, 12, 13 }, // J // n=14
			{ 8, 9, 10, 14 }, // J Rot 1 // n=15
			{ 4, 5, 8, 12 }, // J Rot 2 // n=16
			{ 8, 12, 13, 14 }, // J Rot 3 // n=17
			{ 8, 9, 12, 13 }, // Square // // n=18
	};
	
	/**
	 * Constructor of GamePanel
	 * sets the background to gray, a border line of 2 pixels
	 * sets the @keylistener and starts the timer
	 */
	
	public GamePanel() {
		setBackground(Color.gray);
		setBorder(new LineBorder(Color.black, 2));
		setKeyListener();
		t.start();
	}
	
	/**
	 * sets a listener for all the keys included in the game
	 * @VK_LEFT - moves left;
	 * @VK_RIGHT - moves right;
	 * @VK_DOWN - moves down;
	 * @VK_SPACE - rotate the shape
	 * @VK_ENTER - restarts the game
	 * @Char_P - pauses/resumes the game
	 */
	
	public void setKeyListener(){
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					x -= squareW;
					checkLeftBoundry();
					if (checkIfFull())
						x += squareW;
					repaint();
					break;
				case KeyEvent.VK_RIGHT:
					x += squareW;
					checkRightBoundry();
					if (checkIfFull())
						x -= squareW;
					repaint();
					break;
				case KeyEvent.VK_DOWN:
					if (!disableDown) {
						y += squareH;
						if (getMaximum() == 1)
							disableDown = true;
						checkIfContinueDown();
						repaint();
					}
					break;
				case KeyEvent.VK_SPACE:
					rotate = true;
					repaint();
					break;
				case KeyEvent.VK_ENTER:
					startOver();
					break;
				}

				switch (e.getKeyChar()) {
				case 'p':
					if (t.isRunning())
						t.stop();
					else
						t.start();
					break;
				}
			}

		});
	}
	
	/**
	 * @return the shapes matrix
	 */

	public int[][] getShapes() {
		return shapes;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		/**
		 * draws the matrix of the background
		 */
		g.setColor(new Color(0.2f, 0.1f, 0.1f, 0.1f));
		for (int i = 0; i < isFull.length; i++) {
			for (int j = 0; j < isFull[0].length; j++) {
				g.draw3DRect(backx, backy, squareW, squareH, true);
				backx += squareW;
			}
			backx = 0;
			backy += squareH;
		}
		backy = 0;
		/**
		 * generates new shape and next shape
		 */
		if (first) {
			shape = nextShape;
			nextShape = (int) (Math.random() * 19);
			colorShape = nextColor;
			nextColor = colors[(int) (Math.random() * colors.length)];
			first = false;
		}
		/**
		 * checks if there was invoked a rotate method
		 */
		if (rotate) {
			rotate();
			rotate = false;
		}
		
		/**
		 * draws the new shapes and moves the drawn shape down
		 */	
		for (int i = 0; i < pattern.length; i++) {
			if (i == shapes[shape][0] || i == shapes[shape][1] || i == shapes[shape][2] || i == shapes[shape][3])
				g.setColor(colorShape);
			else
				g.setColor(new Color(1f, 0f, 0f, 0));
			checkLeftBoundry();
			checkRightBoundry();
			g.fill3DRect(pattern[i].x + x, pattern[i].y + y, squareW, squareH, true);

		}
		for (int i = 0; i < shapeList.size(); i++) {
			g.setColor(colorList.get(i));
			g.fill3DRect(shapeList.get(i).x, shapeList.get(i).y, squareW, squareW, true);
		}

	}

	/**
	 * checks if the block can continue down, if it encounters the floor or a
	 * slot from @isFull that is true, it bring down another shape
	 */

	public void checkIfContinueDown() {
		if (y + squareH * 4 > height || checkIfFull()) {
			score += 17;
			for (int i = 0; i < 4; i++) {
				int tmpx = x + squareW * (shapes[shape][i] % 4) + startx;
				int tmpy = y + squareH * (shapes[shape][i] / 4 - 1);
				shapeList.add(new Point(tmpx, tmpy));
				colorList.add(colorShape);
				try {
					isFull[tmpy / squareH][tmpx / squareW] = true;
				} catch (Exception e) {
					startOver();
				}
			}
			y = starty;
			x = startx;
			first = true;
		}
	}


	public int getScore() {
		return score;
	}

	public int getMaximum() {
		for (int i = 0; i < isFull.length; i++) {
			for (int j = 0; j < isFull[0].length; j++) {
				if (isFull[i][j])
					return i;
			}
		}
		return -1;
	}
	
	/**
	 * erases the line that is given as parameter, changes all slots in line k
	 * of @isFull to false and brings down the rest of the tiles, changes
	 * their @isFull slots to true
	 * 
	 * @param k
	 *            - the line which will be erased
	 */
	
	
	public void erase(int k) {

		for (int i = 0; i < isFull[k].length; i++) {
			isFull[k][i] = false;
		}

		for (int i = k - 1; i >= 0; i--) {
			for (int j = 0; j < isFull[k].length; j++) {
				if (isFull[i][j]) {
					isFull[i][j] = false;
					isFull[i + 1][j] = true;
				}
			}
		}

		for (int i = 0; i < shapeList.size(); i++) {
			if (shapeList.get(i).y / squareH == k) {
				shapeList.remove(i);
				colorList.remove(i);
				i--;
			}
		}
		for (int i = 0; i < shapeList.size(); i++)
			if (shapeList.get(i).y / squareH < k)
				shapeList.get(i).y += squareH;

	}

	/**
	 * checks whether there's a line to erase, returns the line that should be
	 * erased
	 * 
	 * @return -1 if none line has found
	 */

	public void checkToEraseLine() {
		int count = 0;
		for (int i = 0; i < isFull.length; i++) {
			for (int j = 0; j < isFull[0].length; j++) {
				if (isFull[i][j]) {
					count++;
				}
			}
			if (count == isFull[0].length) {
				score += 50;
				erase(i);
			} else
				count = 0;
		}
	}

	public boolean checkForGameOver() {
		for (int i = 0; i < isFull[0].length; i++)
			if (isFull[1][i])
				return true;
		return false;
	}

	public void startOver() {
		colorList.clear();
		shapeList.clear();
		first = true;
		disableDown = false;
		for (int i = 0; i < isFull.length; i++) {
			for (int j = 0; j < isFull[0].length; j++) {
				isFull[i][j] = false;
			}
		}
		y = starty;
		x = startx;
		score = 0;
		t.start();
	}

	public boolean checkIfFull() {
		try {
			int x = this.x + squareW * (shapes[shape][0] % 4) + startx;
			int x1 = this.x + squareW * (shapes[shape][1] % 4) + startx;
			int x2 = this.x + squareW * (shapes[shape][2] % 4) + startx;
			int x3 = this.x + squareW * (shapes[shape][3] % 4) + startx;
			int y = this.y + squareH * (shapes[shape][0] / 4);
			int y1 = this.y + squareH * (shapes[shape][1] / 4);
			int y2 = this.y + squareH * (shapes[shape][2] / 4);
			int y3 = this.y + squareH * (shapes[shape][3] / 4);
			return isFull[y / squareH][x / squareH] || isFull[y1 / squareH][x1 / squareH]
					|| isFull[y2 / squareH][x2 / squareH] || isFull[y3 / squareH][x3 / squareH];
		} catch (Exception e) {
			return false;
		}
	}

	public int getMaxWidth(int row) {
		int max = 0;
		for (int i = 0; i < 4; i++) {
			if (shapes[row][i] % 4 > max)
				max = shapes[row][i] % 4;
		}
		return max;
	}

	public int getMinWidth(int row) {
		int min = 4;
		for (int i = 0; i < 4; i++) {
			if (shapes[row][i] % 4 < min)
				min = shapes[row][i] % 4;
		}
		return min;
	}

	public int getNextShape() {
		return nextShape;
	}

	public Color getNextShapeColor() {
		return nextColor;
	}

	public int getMaxHeight(int row) {
		int max = 0;
		switch (row) {
		case 0:
			max = 0;
			break;
		case 1:
			max = 3;
			break;
		case 18:
			max = 2;
			break;
		case 2:
			max = 2;
			break;
		case 4:
			max = 2;
			break;
		case 6:
			max = 2;
			break;
		case 7:
			max = 2;
			break;
		case 11:
			max = 2;
			break;
		case 13:
			max = 2;
			break;
		case 15:
			max = 2;
			break;
		case 17:
			max = 2;
			break;
		default:
			max = 1;
			break;

		}
		return max;
	}

	public void checkRightBoundry() {
		if (x + (squareW * (getMaxWidth(shape) + 1)) > width)
			x = width - (squareW * (getMaxWidth(shape) + 1));
	}

	public void checkLeftBoundry() {
		if (pattern[getMinWidth(shape)].x + x < 0)
			x = Math.negateExact(startx) - squareW * getMinWidth(shape);
	}

	///// Rotate Method //////

	public void rotate() {
		switch (shape) {
		case 0:
			shape = 1;
			break;
		case 1:
			shape = 0;
			break;
		case 2:
			shape = 3;
			break;
		case 3:
			shape = 2;
			break;
		case 4:
			shape = 5;
			break;
		case 5:
			shape = 4;
			break;
		case 6:
			shape = 7;
			break;
		case 7:
			shape = 8;
			break;
		case 8:
			shape = 9;
			break;
		case 9:
			shape = 6;
			break;
		case 10:
			shape = 11;
			break;
		case 11:
			shape = 12;
			break;
		case 12:
			shape = 13;
			break;
		case 13:
			shape = 10;
			break;
		case 14:
			shape = 15;
			break;
		case 15:
			shape = 16;
			break;
		case 16:
			shape = 17;
			break;
		case 17:
			shape = 14;
			break;
		}
		checkRightBoundry();
		checkLeftBoundry();
		repaint();
	}

}
