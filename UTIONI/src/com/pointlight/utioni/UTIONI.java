package com.pointlight.utioni;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
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

public class UTIONI extends JDialog
{
	public static String projectDir = System.getProperty("user.dir") + "/bin/com/pointlight/utioni";
	public static Preferences prefs = Preferences.userNodeForPackage(com.pointlight.utioni.Command.class);

	private static final long serialVersionUID = 1L;
	private static URL running = UTIONI.class.getResource("/com/pointlight/utioni/assets/img/1.gif");
	private static URL talking = UTIONI.class.getResource("/com/pointlight/utioni/assets/img/2.gif");
	private static Icon isRunning = new ImageIcon(running);
	private static Icon isTalking = new ImageIcon(talking);
	private ImageIcon imageIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/com/pointlight/utioni/assets/img/1.gif")));
	private Image image = imageIcon.getImage();
	private SystemTray sysTray;
	private PopupMenu menu;
	private MenuItem quit;
	private MenuItem hide;
	private MenuItem show;
	private TrayIcon trayIcon;
	private static JLabel label;
	private static Thread speech;

	public UTIONI() throws IOException
	{
		label = new JLabel();

		label.setIcon(isRunning);

		setType(Type.UTILITY);
		setSize(64, 64);
		setLocation(0, 0);
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setBackground(new Color(0, 0, 0, 1));
		add(label);
		pack();
		setVisible(true);

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
					setVisible(false);

					menu.add(show);
					menu.remove(hide);
				}
			});

			show.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					setVisible(true);

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
	}

	public static void main(String[] args)
	{
		speech = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println(projectDir);
				try
				{
					voce.SpeechInterface.init(projectDir, true, true, new File(projectDir + "/assets/gram").toURI().toURL().toString(), "main");
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}

				voce.SpeechInterface.synthesize("hello " + UTIONI.prefs.get("com.pointlight.koc.utioni.name", "user") + ", welcome back.");

				new Thread(new Command()).start();
			}
		});

		try
		{
			new UTIONI();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
