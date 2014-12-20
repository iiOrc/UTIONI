package com.pointlight.utioni;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Icon;

public class Command implements Runnable
{
	private boolean isTalking = true;

	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{
		while (!Thread.currentThread().isInterrupted())
		{
			if (voce.SpeechInterface.getRecognizerQueueSize() > 0)
			{
				String s = voce.SpeechInterface.popRecognizedString();

				if (s.equals("you may speak now") || isTalking)
				{
					isTalking = true;

					if (s.startsWith("start calling me by the name"))
					{
						String s2 = "start calling me by the name ";

						UTIONI.prefs.put("com.pointlight.koc.utioni.name", s.substring(s2.length(), s.length()));

						UTIONI.voice.speak("Very well, I will start calling you by the name " + UTIONI.prefs.get("com.pointlight.koc.utioni.name", "user."));

					} else
					{
						Icon icon = UTIONI.label.getIcon();

						switch (s)
						{
							case "be quite":
							case "please be quite":

								UTIONI.walk.suspend();
								UTIONI.label.setIcon(UTIONI.isStrait);

								UTIONI.voice.speak("Very well.");

								isTalking = false;

								UTIONI.label.setIcon(icon);
								UTIONI.walk.resume();

								sleep();

								break;

							case "thanks a lot":

								UTIONI.walk.suspend();
								UTIONI.label.setIcon(UTIONI.isStrait);
								UTIONI.voice.speak("You are welcome " + UTIONI.prefs.get("com.pointlight.koc.utioni.name", "user."));
								UTIONI.label.setIcon(icon);
								UTIONI.walk.resume();

								sleep();

								break;

							case "you may speak now":

								UTIONI.walk.suspend();
								UTIONI.label.setIcon(UTIONI.isStrait);
								UTIONI.voice.speak("Thank you.");
								UTIONI.label.setIcon(icon);
								UTIONI.walk.resume();

								sleep();

								break;

							case "can you open my browser":

								UTIONI.walk.suspend();
								UTIONI.label.setIcon(UTIONI.isStrait);

								if (Desktop.isDesktopSupported())
								{
									UTIONI.voice.speak("Opening browser.");

									Desktop desktop = Desktop.getDesktop();

									if (desktop.isSupported(Desktop.Action.BROWSE))
									{
										try
										{
											desktop.browse(new URI("http://www.google.com"));
										}
										catch (IOException ioe)
										{
											UTIONI.voice.speak("I failed to open your browser.");

											ioe.printStackTrace();
										}
										catch (URISyntaxException use)
										{
											use.printStackTrace();
										}
									} else
									{
										UTIONI.voice.speak("Apparently I cannot open your browser.");
									}
								}

								UTIONI.label.setIcon(icon);
								UTIONI.walk.resume();

								sleep();

								break;

							default:

								UTIONI.walk.suspend();
								UTIONI.label.setIcon(UTIONI.isStrait);

								UTIONI.voice.speak("I am sorry, I cannot process that request.");

								UTIONI.label.setIcon(icon);
								UTIONI.walk.resume();

								sleep();

								break;
						}
					}
				}
			}
		}
	}

	private void sleep()
	{
		try
		{
			Thread.sleep(200);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
