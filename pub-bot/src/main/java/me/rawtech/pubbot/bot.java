package me.rawtech.pubbot;

import org.pircbotx.PircBotX;
import org.pircbotx.User;

import java.util.Scanner;

public class bot
{
	public static void main(String [] args)
	{
		System.out.println("Starting irc bot.");

		PircBotX bot = new PircBotX();
		bot.setName("si-time-bot");
		bot.setVerbose(true);

		try {
			bot.connect("irc.ebuyer.com");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		User user = bot.getUser("simonhayre");

		SendTime timeSender = new SendTime(bot, user);
		timeSender.start();

		Scanner sc = new Scanner(System.in);

		System.out.print("Press any char to close.");

		sc.next();

		if(bot.isConnected()) {
			bot.disconnect();
		}

		System.out.println("Irc bot has stopped.");

		System.exit(0);
	}
}
