package me.rawtech.pubbot;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {
        String message = event.getMessage();
        String[] parts = message.split(" ");
        String root = parts[0];

        if (root.equals("!pubbot") || root.equals("!pub-bot") || root.equals("!pub")) {
            if (parts.length == 1) {
                this.showHelp(event);
            } else {
                String command = parts[1];

                switch (command) {
                    case "help":
                        this.showHelp(event);
                        break;
                    case "order":
                        String order =  "";
                        for (int i = 2; i < parts.length; i++) {
                            order += parts[i] + " ";
                        }

                        this.addOrder(event, order);
                        break;
                    case "info":
                        this.sendInfo(event);
                        break;
                    case "myorder":
                        this.showOrder(event);
                        break;
                    case "count":
                        this.count(event);
                        break;
                    case "remove":
                        this.remove(event);
                        break;
                    case "menu":
                        this.showMenu(event);
                        break;
                }
            }
        }
    }

    private void showMenu(final GenericMessageEvent event) {
        event.respond("You'll probably find the menu here:");
        event.respond("https://wrenkitchens.atlassian.net/wiki/display/DEV/Pub");
    }

    private void count(final GenericMessageEvent event) {
        event.respond("Currently got " + PubBot.kernel.getNumOrders() + " orders.");
    }

    private void remove(final GenericMessageEvent event) {
        if (PubBot.kernel.hasOrder(event.getUser().getNick())) {
            PubBot.kernel.cancelOrder(event.getUser().getNick());
            event.respond("I've cancelled your order.");
        } else {
            event.respond("You haven't made an order.");
        }
    }

    private void showOrder(final GenericMessageEvent event) {
        if (PubBot.kernel.hasOrder(event.getUser().getNick())) {
            event.respond("Ive currently got down a '" + PubBot.kernel.getOrder(event.getUser().getNick()) + "' for you.");
        } else {
            event.respond("I currently don't have anything ordered for you.");
        }
    }

    private void sendInfo(final GenericMessageEvent event) {
        event.respond("We'll be going to " + PubBot.kernel.getPubName() + " at " + PubBot.kernel.getTime() + ".");
    }

    private void addOrder(final GenericMessageEvent event, String order) {
        if (order.equals("")) {
            event.respond("You must tell me your order you dummy!");
        } else {
            PubBot.kernel.addOrder(event, order.trim());
        }
    }

    private void showHelp(final GenericMessageEvent event) {
        event.respond("You can use the following commands:");
        event.respond("[info], [count], [myorder], [remove], [menu], [order <your order>]");
    }
}
