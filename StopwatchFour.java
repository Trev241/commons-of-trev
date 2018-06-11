// WARNING: By continuing ahead to read the program, you must also remember that your brain cells are vulnerable and may decay
// from the utter clusterf*** of code written here. This stopwatch was in no way made just for utility purposes, it was made to
// pursue a dreadful hobby

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

// "STOPWATCH FOUR??? YOU'VE MADE THREE OF THESE ALREADY???" well, yes, I have made three of these types of programs already
// in the past. The very first one was terribly small and so was the second if I remember correctly. With each Stopwatch
// version, they got more efficient and sizeable. I can still remember how pathetically inefficient and dirty the first
// Stopwatch's source was
// All of them had GUI though. I was no fool to make a stopwatch in the console but I was foolish enough to make one in the
// first place
public class StopwatchFour implements Runnable {
	private Window window;
	private JLabel timer;
	private JMenuBar menuBar;                                 // I don't even know why I made a menu in the first place
	private JMenu help, settings;
	private JMenuItem hGeneral, sTheme;
	private JButton[] buttons = new JButton[5];               // This is a cheap trick I keep using to save time and lines
	
	private Thread thread;
	private Timer timerForward, timerBackward;
	
  // These boolean variables prevent the program from breaking almost all the time
	private boolean isDarkTheme, isCountingF, isCountingB, isPaused;  // isPaused is a redundant variable, I haven't used it
	
	Color stdColor, stdMenuColor;
	Color darkThemeColor = new Color(35, 35, 35);
	Color fontColorDT = new Color(215, 215, 215);
	Color fontColorLT = new Color(10, 10, 10);
	
	Font stdMenuFont = new Font("Verdana", Font.PLAIN, 12);
	
  // This is going to be an overwhelmingly long constructor, please be prepared. In fact, a majority of the program and its 
  // dynamics are coded in this constructor
	public StopwatchFour() {
		window = new Window("Stopwatch IV", 1200, 600, this);
		
		stdColor = window.getBackground();
		
		timer = new JLabel("00   :   00   :   00", SwingConstants.CENTER);
		timer.setFont(new Font("Verdana", Font.PLAIN, 93));
		timer.setBounds(10, 185, 1160, 100);
		timer.setForeground(fontColorLT);
		
    // Even though I mention in the JMenuItem that F2 can be used to change the theme as well, I didn't implement it because
    // it didn't work ._.
		sTheme = new JMenuItem("Change Theme     F2");
		sTheme.setFont(stdMenuFont);
		sTheme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Theme change was requested");
				changeTheme();
			}
		});
		
		hGeneral = new JMenuItem("General");
		hGeneral.setFont(stdMenuFont);
		hGeneral.addActionListener(new UnimplementedFeatureResponse(window));     // Ahahaha, I never bothered to add anything
		
		help = new JMenu("Help");
		help.setFont(stdMenuFont);
		help.add(hGeneral);
		
		settings = new JMenu("Settings");
		settings.setFont(stdMenuFont);
		settings.add(sTheme);
		
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, window.getWidth(), 27);
		menuBar.setBorderPainted(false);
		menuBar.add(settings);
		menuBar.add(help);
		
		stdMenuColor = menuBar.getBackground();
		
    // See what I mean? Without this loop, I'd be writing the same statements five times
		for (int i = 0, x = 100; i < buttons.length; i++, x += 200) {
			buttons[i] = new JButton("[i]");
			buttons[i].setFont(stdMenuFont);
			buttons[i].setBackground(window.getContentPane().getBackground());
			buttons[i].setBounds(x, 350, 185, 35);
			
			window.add(buttons[i]);
		}
		buttons[0].setText("Forward");
		buttons[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttons[0].setText("Forward");
				buttons[1].setText("Reverse");
				if (!isCountingF && !isCountingB) {
					timerForward = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							int sec = Integer.parseInt(timer.getText().substring(18));
							int min = Integer.parseInt(timer.getText().substring(9, 11));
							int hour = Integer.parseInt(timer.getText().substring(0, 2));
							if (sec >= 59) {
								sec = 0;
								if (min >= 59) {
									min = 0;
									++hour;
								} else 
									++min;
							} else {
								++sec;
							}
							String hourAsStr = (hour < 10) ? "0" + String.valueOf(hour) : String.valueOf(hour),
								   minAsStr = (min < 10) ? "0" + String.valueOf(min) : String.valueOf(min),
								   secAsStr = (sec < 10) ? "0" + String.valueOf(sec) : String.valueOf(sec);
							timer.setText(hourAsStr +  "   :   " + minAsStr + "   :   " + secAsStr);
						}
					});
					timerForward.setRepeats(true);
					timerForward.start();
					isCountingF = true;
				} else if (isCountingF)
					showMessage("Calm down! It's already counting forward doofus. Don't try to break me");
				else if (isCountingB)
					showMessage("I can only allow you to do this when you stop the stopwatch from counting backwards... smh");
			}
		});
		buttons[1].setText("Reverse");
		buttons[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				buttons[1].setText("Reverse");
				buttons[0].setText("Forward");
				if (!isCountingB && !isCountingF) {
					// SAFETY MEASURE TO CALL OFF COUNTDOWN IF TIMER IS ALREADY AT ABSOLUTE ZERO
					if (timer.getText().equalsIgnoreCase("00   :   00   :   00")) {
						showMessage("HOW DO YOU COUNT DOWN WHEN THE TIMER IS AT ZERO");
						isCountingB = false;
						return;
					}
					
					timerBackward = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							int sec = Integer.parseInt(timer.getText().substring(18));
							int min = Integer.parseInt(timer.getText().substring(9, 11));
							int hour = Integer.parseInt(timer.getText().substring(0, 2));
							
							if (sec <= 0) {
								if (min <= 0) {
									if (hour > 0) {
										--hour;
										min = 59;
										sec = 59;
									}
									else {
										timerBackward.stop();
										isCountingB = false;
									}
								} else {
									--min;
									sec = 59;
								}
							} else
								--sec;
							String hourAsStr = (hour < 10) ? "0" + String.valueOf(hour) : String.valueOf(hour),
									   minAsStr = (min < 10) ? "0" + String.valueOf(min) : String.valueOf(min),
									   secAsStr = (sec < 10) ? "0" + String.valueOf(sec) : String.valueOf(sec);
							timer.setText(hourAsStr +  "   :   " + minAsStr + "   :   " + secAsStr);
						}
					});
					timerBackward.setRepeats(true);
					timerBackward.start();
					isCountingB = true;
				} else if (isCountingB)
					showMessage("I wonder if your brain runs backward because, in case you didn't notice, the stopwatch is already counting backward...");
				else if (isCountingF)
					showMessage("Are you dimwitted? The stopwatch is counting forward already, stop that first if you want to do anything else");
			}
		});
		buttons[2].setText("Reset");
		buttons[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isCountingF)
					timerForward.stop();
				else if (isCountingB)
					timerBackward.stop();
				else
					showMessage("The timer isn't even running ._.");
				isCountingF = false;
				isCountingB = false;
				timer.setText("00   :   00   :   00");
			}
		});
		buttons[3].setText("Set Time");
		buttons[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!isCountingF && !isCountingB) {
					try {
						int hrs = 0, min = 0, sec = 0;
						String secInput = showInputMessage("Enter seconds parameter");
						if (Integer.parseInt(secInput) > 59) {
							showMessage(secInput + " seconds translates to more than a minute...");
							return;
						}
						String minInput = showInputMessage("Enter minutes parameter");
						if (Integer.parseInt(minInput) > 59) {
							showMessage(minInput + "minutes translates to more than an hour...");
							return;
						}
						String hrsInput = showInputMessage("Enter hours parameter");
						if (Integer.parseInt(hrsInput) > 99) {
							showMessage("The format does not allow for more than 99 hours to be inputted :(");
							return;
						}
						sec = Integer.parseInt(secInput);
						min = Integer.parseInt(minInput);
						hrs = Integer.parseInt(hrsInput);
						String hourAsStr = (hrs < 10) ? "0" + String.valueOf(hrs) : String.valueOf(hrs),
							   minAsStr = (min < 10) ? "0" + String.valueOf(min) : String.valueOf(min),
							   secAsStr = (sec < 10) ? "0" + String.valueOf(sec) : String.valueOf(sec);
						timer.setText(hourAsStr + "   :   " + minAsStr + "   :   " + secAsStr);
					} catch (NumberFormatException e) {
						showMessage("<facepalm> You need to input a goddamn number, not alphabets or anything else of the sort");
						System.out.println("The user is pathetically dumb");
					}
				} else
					showMessage("Something could go wrong if you tried setting the time while the stopwatch was running...");
			}
		});
		buttons[4].setText("Pause");
		buttons[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isCountingB) {
					buttons[1].setText("Resume");
					timerBackward.stop();
					isCountingB = false;
				} else if (isCountingF) {
					buttons[0].setText("Resume");
					timerForward.stop();
					isCountingF = false;
				} else
					showMessage("What are you trying to pause...");
			}
		});
		
		window.add(timer);
		window.add(menuBar);
		
		window.setVisible(true);
	}
	
  // I don't even know why I had even implemented the Runnable interface in the first place when all the run() method ever had
  // was just a useless and worthless print statement
	@Override
	public void run() {
		System.out.println("The runnable interface has successfully been implemented and executed");
	}
	
	public void start() {
		thread =  new Thread(this);
		thread.start();
	}
	
  // Do I even need this method?
	public void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.err.println(thread.getName() + " (" + thread.getId() + ") was interrupted while/during join() invokation");
		}
	}
	
  // 3 lines compressed to a simple function call
	public void showMessage(String message) {
		JLabel tempComponent = new JLabel(message);
		tempComponent.setFont(new Font("Verdana", Font.PLAIN, 13));
		JOptionPane.showMessageDialog(window, tempComponent, "What are you doing...", JOptionPane.INFORMATION_MESSAGE);
	}
	
  // s a e m    t i n g
	public String showInputMessage(String message) {
		JLabel tempComponent = new JLabel(message);
		tempComponent.setFont(new Font("Verdana", Font.PLAIN, 13));
		return JOptionPane.showInputDialog(window, tempComponent, "What are you doing...", JOptionPane.INFORMATION_MESSAGE);
	}
	
  // This could've been done better, but I didn't have time nor will to think it out. So I have done it in the most raw method
  // possible
	public void changeTheme() {
		if (!isDarkTheme) {
			System.out.println("Committing changes");
			window.getContentPane().setBackground(darkThemeColor);
			timer.setForeground(fontColorDT);
			menuBar.setBackground(new Color(50, 50, 50));
			menuBar.setForeground(fontColorDT);
			help.setForeground(fontColorDT);
			settings.setForeground(fontColorDT);
			for (int i = 0; i < buttons.length; i++) {
				buttons[i].setBackground(window.getContentPane().getBackground());
				buttons[i].setForeground(fontColorDT);
			}
			isDarkTheme = true;
		} else  {
			window.getContentPane().setBackground(stdColor);
			timer.setForeground(fontColorLT);
			menuBar.setBackground(stdMenuColor);
			menuBar.setForeground(fontColorLT);
			help.setForeground(fontColorLT);
			settings.setForeground(fontColorLT);
			for (int i = 0; i < buttons.length; i++) {
				buttons[i].setBackground(stdMenuColor);
				buttons[i].setForeground(fontColorLT);
			}
			isDarkTheme = false;
		}
		System.out.println("Successfully changed theme");
	}
	
	public static void main(String[] args) {
		StopwatchFour stopwatch = new StopwatchFour();
		stopwatch.start();                  // Without this statement, you can still run the program, if I'm not mistaken. Try it out
	}
}

// I made this seperate ActionListener class since I suspected that I would be using that error message quite often. It would've
// save me time from writing multiple annonymous inner class ActionListeners that all did the same thing. But the most saddening
// part about this is that I only used it once...
class UnimplementedFeatureResponse implements ActionListener {
	private Window window;
	
	public UnimplementedFeatureResponse(Window window) {
		this.window = window;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JLabel tempLabelComponent = new JLabel("This feature has yet to be implemented, therefore as of now, it is unavailable!");
		tempLabelComponent.setFont(new Font("Verdana", Font.PLAIN, 13));
		JOptionPane.showMessageDialog(window, tempLabelComponent, "Uh oh...", JOptionPane.ERROR_MESSAGE);
	}
}

class Window extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public Window(String name, int width, int height, StopwatchFour watch) {
		super(name);
		
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
	}
}
