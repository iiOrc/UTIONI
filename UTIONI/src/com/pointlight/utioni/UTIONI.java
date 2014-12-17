package com.pointlight.utioni;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UTIONI extends JDialog
{
	private static final long serialVersionUID = 1L;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private double width = screenSize.getWidth();
	private double height = screenSize.getHeight();
	private Image image;
	private SystemTray sysTray;
	private PopupMenu menu;
	private MenuItem quit;
	private TrayIcon trayIcon;

	public UTIONI()
	{
		setType(Type.UTILITY);
		setSize(64, 64);
		setLocation((int) width - 64, (int) height - 96);
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
		// setBackground(new Color(0, 0, 0, 1));
		setVisible(true);

		JPanel panel = new JPanel();
		ImageIcon icon = new ImageIcon("com.pointlight.utioni.assets.img");
		JLabel label = new JLabel();
		label.setIcon(icon);
		panel.add(label);
		add(panel);

		if (SystemTray.isSupported())
		{
			sysTray = SystemTray.getSystemTray();

			image = Toolkit.getDefaultToolkit().getImage("./dog.jpg");

			menu = new PopupMenu();

			quit = new MenuItem("quit");

			menu.add(quit);

			quit.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					System.exit(0);
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
		new UTIONI();
	}
}
