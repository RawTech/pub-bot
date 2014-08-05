package me.rawtech.pubbot;

import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {
        String message = event.getMessage();
        String[] parts = message.split(" ");
        String root = parts[0];

        List<String> respondTo = new ArrayList<>();
        respondTo.add("!pubbot");
        respondTo.add("!pub-bot");
        respondTo.add("!pub");
        respondTo.add("!simon");
        respondTo.add("!josh");

        if (respondTo.contains(root)) {
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
                    case "wimpout":
                        this.remove(event);
                        break;
                    case "menu":
                        this.showMenu(event);
                        break;
                    case "song":
                    case "sing":
                        if (root.equals("!simon")) {
                            switch (new Random().nextInt(1)) {
                                case 0:
                                    event.respond("What's in your bag Simon, tell us what's in your bag, you bastard!");
                                    event.respond("I've got a bag a chips, groovy hips and a Maddy Mccan in a tin!");
                            }
                        } else if (root.equals("!josh")) {
                            switch (new Random().nextInt(1)) {
                                case 0:
                                    event.respond("What's in your burger Josh? Tell us what's in your burger, you bastard.");
                                    event.respond("I've got a saveloy and a donna kebab and a piece of black pudding.");
                            }
                        }
                        break;
                    case "sudo":
                        if (PubBot.kernel.isSudoer(event.getUser().getNick())) {
                            this.handleSudo(event);
                        }
                        break;
                }
            }
        }
    }

    private void handleSudo(final GenericMessageEvent event)
    {
        String message = event.getMessage();
        String[] parts = message.split(" ");
        String root    = parts[0];

        if (parts.length < 3) {
            event.respond("Not enough args.");
        }

        String targetUsername = parts[2];
        String command = root + " ";
        for (int i = 3; i < parts.length; i++) {
            command+= parts[i] + " ";
        }

        User user = null;
        try {
            user = PubBot.kernel.getBot().getUser(targetUsername);
        } catch (Exception e) {
            event.respond(targetUsername + " not found.");
            return;
        }

        if (user == null) {
            event.respond(targetUsername + " not found.");
            return;
        }

        GenericMessageEvent newEvent = null;

        if (event instanceof PrivateMessageEvent) {
            newEvent = new PrivateMessageEvent(PubBot.kernel.getBot(), user, command);
        } else if (event instanceof MessageEvent) {
            newEvent = new MessageEvent(PubBot.kernel.getBot(), ((MessageEvent) event).getChannel(), user, command);
        }

        if (newEvent != null) {
            try {
                this.onGenericMessage(newEvent);
            } catch (Exception e) {
                event.respond(e.getMessage());
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
