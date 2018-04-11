import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

public class TetrisPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private GamePanel gamePanel = new GamePanel();
	private BackgroundPanelRight background1 = new BackgroundPanelRight();
	private BackgroundPanelDown background2 = new BackgroundPanelDown();
	private static final int startx = 140, starty = 10;
	private int squareW = 20;
	private int[][] shapes = gamePanel.getShapes();
	private Point[] pattern = { new Point(startx, starty), // 0
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

	public TetrisPanel() {
		setLayout(new GridBagLayout());
		background2.setBackground(Color.GRAY);
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.fill = GridBagConstraints.HORIZONTAL;
		setConstraints(gbc1, 379, 0, 0, 0.7);
		add(gamePanel, gbc1);
		setConstraints(gbc1, 379, 1, 0, 0.36);
		add(background1, gbc1);
		gbc1.gridwidth = 3;
		setConstraints(gbc1, 150, 0, 1, 0.0);
		add(background2, gbc1);
	}

	private void setConstraints(GridBagConstraints gbc1, int ipady, int gridx, int gridy, double weightx) {
		gbc1.ipady = ipady;
		gbc1.gridx = gridx;
		gbc1.gridy = gridy;
		gbc1.weightx = weightx;
	}

	class BackgroundPanelDown extends JPanel {
		private static final long serialVersionUID = 1L;
		private int shape;
		private int backx = startx, backy = starty;
		private Color color;
		private Timer t = new Timer(0, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				shape = gamePanel.getNextShape();
				color = gamePanel.getNextShapeColor();
				repaint();
			}
		});

		public BackgroundPanelDown() {
			setBackground(Color.GRAY);
			t.start();
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(new Color(0.2f, 0.1f, 0.1f, 0.1f));
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					g.draw3DRect(backx, backy, squareW, squareW, true);
					backx += squareW;
				}
				backx = startx;
				backy += squareW;
			}
			backy = starty;
			g.setColor(Color.WHITE);
			g.setFont(new Font("Sherif", Font.BOLD, 15));
			g.drawString("Next shape: ", 50, 50);
			for (int i = 0; i < pattern.length; i++) {
				if (i == shapes[shape][0] || i == shapes[shape][1] || i == shapes[shape][2] || i == shapes[shape][3])
					g.setColor(color);
				else
					g.setColor(new Color(1f, 0f, 0f, 0));
				g.fill3DRect(pattern[i].x, pattern[i].y, squareW, squareW, true);

			}
		}

	}

	class BackgroundPanelRight extends JPanel {

		private static final long serialVersionUID = 1L;
		private String scoreText = "0";
		private Timer scoreTimer = new Timer(0, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setScore("" + gamePanel.getScore());
				repaint();
			}
		});
		public BackgroundPanelRight() {
			scoreTimer.start();
			setBackground(Color.GRAY);
			setBorder(new LineBorder(Color.BLACK, 2));
			scoreTimer.start();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.WHITE);
			g.drawString("Score: ", 20, 20);
			g.drawString(scoreText, 80, 20);
			g.drawString("", 20, 40);
			g.drawString("Pause: ", 10, 60);
			g.drawString("p", 60, 60);
			g.drawString("Restart: ", 10, 80);
			g.drawString("ENTER", 60, 80);
			g.drawString("Rotate: ", 10, 100);
			g.drawString("SPACE", 60, 100);
			g.drawString("Left: ", 10, 120);
			g.drawString("<--", 60, 120);
			g.drawString("Right: ", 10, 140);
			g.drawString("-->", 60, 140);
			g.drawString("Down: ", 10, 160);
			g.drawString("Down", 60, 160);
		}

		public void setScore(String text) {
			scoreText = text;
			repaint();
		}

	}

}
