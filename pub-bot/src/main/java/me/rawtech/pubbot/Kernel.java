package me.rawtech.pubbot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Kernel {

    private String pubName;
    private String time;
    private PircBotX bot;
    private HashMap<String, String> orderList;
    private ArrayList<String> fatMesses;

    public Kernel(String pubName, String time) {
        this.orderList = new HashMap<>();
        this.pubName = pubName;
        this.time = time;

        this.fatMesses = new ArrayList<>();
        this.fatMesses.add("lewis");

        this.bot = new PircBotX();
        this.bot.setName("pub-bot");
        this.bot.getListenerManager().addListener(new MessageListener());

        try {
            this.bot.connect("irc.ebuyer.com");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        this.bot.joinChannel("#wren");
        this.bot.joinChannel("#wrenorder");
    }

    public void shutdown() {
        this.print("Shutting down");

        if (this.bot != null && this.bot.isConnected()) {
            this.bot.disconnect();
        }
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPubName() {
        return this.pubName;
    }

    public String getTime() {
        return this.time;
    }

    public void printPubInfo() {
        this.print("Pub name: " + this.pubName);
        this.print("Time: " + this.time);
    }

    private void print(String message) {
        System.out.println(message);
    }

    public void addOrder(final GenericMessageEvent event, String order) {
        String suffix = "";

        if (this.fatMesses.contains(event.getUser().getNick())) {
            suffix = " you fat mess";
        }

        if (this.hasOrder(event.getUser().getNick())) {
            event.respond("Your order has been updated" + suffix + ".");
        } else {
            event.respond("Your order has been added" + suffix + ".");
        }

        this.orderList.put(event.getUser().getNick(), order);
    }

    public void showOrders() {
        this.print("Order list:");

        for (Map.Entry<String, String> entry : this.orderList.entrySet()) {
            this.print(entry.getKey() + ":  " + entry.getValue());
        }
    }

    public Boolean hasOrder(String name) {
        return this.orderList.containsKey(name);
    }

    public String getOrder(String name) {
        if (this.hasOrder(name)) {
            return this.orderList.get(name);
        }

        return null;
    }

    public int getNumOrders() {
        return this.orderList.size();
    }

    public void cancelOrder(String name) {
        if (this.hasOrder(name)) {
            this.orderList.remove(name);
        }
    }
}
