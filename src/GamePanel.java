import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

	//to set the width and height of the screen, unit size. 
	// the variable game units defines the total number of tiles in the game
	//delay is the time delay in milliseconds between frame updates
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75;

	//
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	//declaring variables for the initial size of the snake
	// score counter for the number of apples eaten
	// x and y position of the apple
	// starting direction of the snake
	// boolean to check if the game is running
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	//GamePanel function sets the size of the panel, changes the color to black, focuses it at the centre
	//it finally calls the function to start the game
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
 

	//startGame function
	// it calls the newApple function, sets the boolean game running to true, starts the timer
	public void startGame() {
		running = true;

		newApple();
		timer = new Timer(DELAY, this);
		timer.start();
	}

	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	//function to update the visual gamestate
	//the first for loop draws the grid of the game
	//g.fillOval puts in the new apple
	// the second for loop colors in the snake
	// the else statement changes the color back to black
	public void draw(Graphics g) {
		if (running) {
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for (int i=0;i<bodyParts;i++) {
				if (i ==0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Arial",Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH-metrics.stringWidth("Score: "+applesEaten))/2, 20);
		}
		else {
			gameOver(g);
		}
	}

	//newApple function sets the position of the new apple in the game
	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}

	// the snake moves in a new direction, the first bodypart is turned gray
	public void move() {
		for(int i = bodyParts;i>0;i--) {
			x[i]=x[i-1];
			y[i]=y[i-1];
		}
		switch(direction) {
			case 'U':
				y[0] = y[0]-UNIT_SIZE;
				break;
			case 'D':
				y[0] = y[0]+UNIT_SIZE;
				break;
			case 'L':
				x[0] = x[0]-UNIT_SIZE;
				break;
			case 'R':
				x[0] = x[0]+UNIT_SIZE;
				break;
		}
	}

	public void checkApple() {
		if ((x[0]==appleX)&&(y[0]==appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}

	public void checkCollisions() {
		//checks if head collides with body
		for (int i = bodyParts;i>0;i--) {
			if ((x[0]==x[i])&&y[0]==y[i]) {
				running = false;
			}
		}
		//check if head touches left/right border
		if (x[0]<0||x[0]>SCREEN_WIDTH) {
			running = false;
		}
		//check if head touches top/bottom border
		if (y[0]<0||y[0]>SCREEN_HEIGHT) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {
		//Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Arial",Font.BOLD,75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH-metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
			repaint();
		}
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			
			switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (direction!='R') {
						direction = 'L';
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (direction!='L') {
						direction = 'R';
					}
					break;
				case KeyEvent.VK_UP:
					if (direction!='D') {
						direction = 'U';
					}
					break;
				case KeyEvent.VK_DOWN:
					if (direction!='U') {
						direction = 'D';
					}
					break;
				case KeyEvent.VK_W:
					if (direction!='D') {
						direction = 'U';
					}
					break;
				
			}
		}
	}
	
}