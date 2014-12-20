package com.pointlight.utioni;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class UTIONI
{
	private static URL left = UTIONI.class.getResource("/com/pointlight/utioni/assets/img/1.gif");
	private static URL right = UTIONI.class.getResource("/com/pointlight/utioni/assets/img/2.gif");
	private static URL strait = UTIONI.class.getResource("/com/pointlight/utioni/assets/img/3.gif");
	private static ImageIcon imageIcon;
	private static Image image;
	private static SystemTray sysTray;
	private static PopupMenu menu;
	private static MenuItem quit;
	private static MenuItem hide;
	private static MenuItem show;
	private static TrayIcon trayIcon;
	private static VoiceManager voiceManager;
	private static Thread speech;
	public static Thread walk;

	public static String projectDir = System.getProperty("user.dir") + "/bin/com/pointlight/utioni";
	public static Preferences prefs = Preferences.userNodeForPackage(com.pointlight.utioni.Command.class);
	public static JDialog window;
	public static Voice voice;
	public static JLabel label;
	public static Icon isLeft = new ImageIcon(left);
	public static Icon isRight = new ImageIcon(right);
	public static Icon isStrait = new ImageIcon(strait);

	public static void createWindow()
	{
		window = new JDialog();

		try
		{
			imageIcon = new ImageIcon(ImageIO.read(UTIONI.class.getResourceAsStream("/com/pointlight/utioni/assets/img/1.gif")));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		image = imageIcon.getImage();

		label = new JLabel();

		label.setIcon(isRight);

		window.setType(Type.UTILITY);
		// window.setResizable(false);
		window.setUndecorated(true);
		window.setAlwaysOnTop(true);
		window.setBackground(new Color(0, 0, 0, 1));
		window.add(label);
		window.pack();
		window.setLocation(0, 0);
		window.setVisible(true);

		speech.start();

		if (SystemTray.isSupported())
		{
			sysTray = SystemTray.getSystemTray();
			menu = new PopupMenu();

			quit = new MenuItem("quit");
			hide = new MenuItem("hide");
			show = new MenuItem("show");

			menu.add(quit);
			menu.add(hide);

			quit.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					speech.interrupt();
					System.exit(0);
				}
			});

			hide.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					window.setVisible(false);

					menu.add(show);
					menu.remove(hide);
				}
			});

			show.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					window.setVisible(true);

					menu.add(hide);
					menu.remove(show);
				}
			});

			trayIcon = new TrayIcon(image, "UTIONI", menu);

			try
			{
				sysTray.add(trayIcon);
			}
			catch (AWTException e)
			{
				System.out.println(e.getMessage());
			}
		}

		walk = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					for (int i = 0; i < Toolkit.getDefaultToolkit().getScreenSize().width; i++)
					{
						window.setLocation(window.getLocation().x + 1, 0);

						try
						{
							Thread.sleep(200);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}

					label.setIcon(isLeft);

					for (int i = 0; i < Toolkit.getDefaultToolkit().getScreenSize().width; i++)
					{
						window.setLocation(window.getLocation().x - 1, 0);

						try
						{
							Thread.sleep(200);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}

					label.setIcon(isRight);
				}
			}
		});

		walk.start();
	}

	public static void main(String[] args)
	{
		speech = new Thread(new Runnable()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				System.setProperty("mbrola.base", projectDir + "/assets/mbrola");

				voiceManager = VoiceManager.getInstance();
				voice = voiceManager.getVoice("mbrola_us1");
				voice.allocate();

				try
				{
					voce.SpeechInterface.init(projectDir, false, true, new File(projectDir + "/assets/gram").toURI().toURL().toString(), "main");
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
				
				Icon icon = label.getIcon();
				
				walk.suspend();
				label.setIcon(isStrait);
				voice.speak("hello " + UTIONI.prefs.get("com.pointlight.koc.utioni.name", "user") + ", welcome back, I am utioni.");
				label.setIcon(icon);
				walk.resume();

				new Thread(new Command()).start();
			}
		});

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				createWindow();
			}
		});
	}
}
