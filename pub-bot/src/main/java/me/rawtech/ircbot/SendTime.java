package me.rawtech.ircbot;

import org.pircbotx.PircBotX;
import org.pircbotx.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SendTime {

	private User user;
	private PircBotX bot;
	long delay = 60000;
	LoopTask task;
	Timer timer;

	public SendTime(PircBotX bot, User user) {
		this.bot = bot;
		this.user = user;

		this.task = new LoopTask(this.bot, this.user);
	}

	public void start() {
		timer = new Timer("SendTime");
		Date executionDate = new Date();
		timer.scheduleAtFixedRate(task, executionDate, delay);
	}

	private class LoopTask extends TimerTask {
		private User user;
		private PircBotX bot;
		private DateFormat timeFormat;
		private DateFormat dateFormat;

		public LoopTask(PircBotX bot, User user) {
			this.user = user;
			this.bot = bot;
			this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			this.timeFormat = new SimpleDateFormat("HH:mm:ss");
		}

		public void run() {
			Date date = new Date();
			bot.sendAction(this.user, "The time is currently: " + this.timeFormat.format(date) + " on " + this.dateFormat.format(date) + " AD");
		}
	}
}
