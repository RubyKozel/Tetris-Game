import javax.swing.JApplet;
import javax.swing.JFrame;
  
@SuppressWarnings("serial")
public class Main extends JApplet {
	
	public Main(){
		add(new TetrisPanel());
	}
	
	public static void main(String [] args){
		Main app = new Main();
		JFrame frame = new JFrame("Tetris Game!");
		frame.setSize(320, 480);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(app);
		frame.setVisible(true);
	}
}
