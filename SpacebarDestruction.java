import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class SpacebarDestruction implements Runnable{
	private Thread thread;
	private Window window;
	private JLabel label, subLabel, stopwatch;
	private JButton[] responses;
	
	private float clicks;
	private boolean timeIsUp;
	
	Font stdFont;
	Color stdColor;
	
	public SpacebarDestruction() {
		stdFont = new Font("Plain", Font.PLAIN, 22);
		stdColor = new Color(20, 20, 20);
		
		window = new Window("Spacebar Destruction", 1200, 600, this);
		window.getContentPane().setBackground(stdColor);
		
		label = new JLabel("Is your hate for the spacebar sufficient to break it?", SwingConstants.CENTER);
		label.setFont(stdFont);
		label.setForeground(new Color(215, 215, 215));
		label.setBounds(10, 230, 1180, 50);
		
		subLabel = new JLabel("Presses : ", SwingConstants.CENTER);
		subLabel.setFont(stdFont);
		subLabel.setForeground(new Color(215, 215, 215));
		subLabel.setBounds(10, 300, 1180, 50);
		subLabel.setVisible(false);
		
		stopwatch = new JLabel("00 : 00 : 20", SwingConstants.CENTER);
		stopwatch.setFont(new Font("Plain", Font.PLAIN, 45));
		stopwatch.setForeground(new Color(215, 215, 215));
		stopwatch.setBounds(10, 120, 1180, 50);
		stopwatch.setVisible(false);
		
		responses = new JButton[3];
		for (int i = 0, x = 150; i < responses.length; i++, x += 300) {
			responses[i] = new JButton("---");
			responses[i].setBackground(stdColor);
			responses[i].setForeground(new Color(215, 215, 215));
			responses[i].setBounds(x, 320, 285, 35);
			responses[i].setFont(new Font("Verdana", Font.PLAIN, 15));
			
			window.add(responses[i]);
		}
		responses[0].setText("Yes");
		responses[1].setText("No");
		responses[2].setText("I'm unsure");
		
		window.add(label);
		window.add(subLabel);
		window.add(stopwatch);
		
		window.setVisible(true);
	}
	
	@Override
	public void run() {
		System.out.println("Entering role-play mode now...");
		
		// PATH - 1
		
		responses[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeText("I hope you really despise the spacebar then, since simple spite is insufficient");
				responses[0].setVisible(false);
				responses[1].setVisible(false);
				responses[2].setVisible(false);
				
				Timer timer = new Timer(4000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						changeText("Tap the spacebar as swiftly as you can and empty your wrath upon it!");
					}
				});
				timer.setRepeats(false);
				timer.start();
				
				Timer countDown = new Timer(1000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						stopwatch.setVisible(true);
						int sec = Integer.parseInt(stopwatch.getText().substring(10));
						if (sec == 0) {
							timeIsUp = true;
							subLabel.setText("Presses per second : " + (float) (clicks / 30));
							changeText("<program is now safe to terminate>");
							return;
						}
						--sec;
						stopwatch.setText("00 : 00 : " + ((sec > 9) ? sec : "0" + sec));
					}
				});
				countDown.setRepeats(true);
				countDown.setInitialDelay(8000);
				countDown.start();
				
				window.requestFocus();
				
				window.addKeyListener(new KeyListener() {
					
					@Override
					public void keyTyped(KeyEvent arg0) {
						System.out.println("Typed event fired");
						if (arg0.getKeyCode() == KeyEvent.VK_SPACE && !timeIsUp) {
							System.out.println("Pressed!");
							subLabel.setVisible(true);
							clicks++;
							subLabel.setText("Presses : " + clicks);
						}
					}
					
					@Override
					public void keyReleased(KeyEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void keyPressed(KeyEvent arg0) {
						System.out.println("Pressed event fired");
						if (arg0.getKeyCode() == KeyEvent.VK_SPACE && !timeIsUp) {
							System.out.println("Pressed!");
							subLabel.setVisible(true);
							clicks++;
							subLabel.setText("Presses : " + clicks);
						}
					}
				});
			}
		});
		
		// PATH - 2
		
		responses[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeText("Modesty, I see... just another way to lie");
				responses[0].setVisible(false);
				responses[2].setVisible(false);
				new Timer(5000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				}).start();
			}
		});
		
		// PATH - 3
		
		responses[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeText("You lack assertiveness. How pathetic are you? I almost pity your plight");
				responses[0].setVisible(false);
				responses[1].setVisible(false);
				new Timer(5000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				}).start();
			}
		});
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		try {
			thread.join();
		} catch(InterruptedException e) {}
	}
	
	public void changeText(String message) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (int r = 215, g = 215, b = 215; r > 20 && g > 20 && b > 20; r--, g--, b--) {
						label.setForeground(new Color(r, g, b));
						Thread.sleep(5);
					}
					label.setText(message);
					Thread.sleep(100);
					for (int r = 20, g = 20, b = 20; r < 215 && g < 215 && b < 215; r++, g++, b++) {
						label.setForeground(new Color(r, g, b));
						Thread.sleep(5);
					}
				} catch (InterruptedException e) {}
			}
		}).start();
	}
	
	public static void main(String[] args) {
		SpacebarDestruction game = new SpacebarDestruction();
		game.start();
	}
}

class Window extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public Window(String name, int width, int height, SpacebarDestruction game) {
		super(name);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width, height);
		setLocationRelativeTo(null);
		setLayout(null);
	}
	
	public void updateWindow() {
		setVisible(false);
		setVisible(true);
	}
}
