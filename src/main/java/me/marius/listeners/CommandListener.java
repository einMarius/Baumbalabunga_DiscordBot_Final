package me.marius.listeners;

import me.marius.commands.type.ServerCommand;
import me.marius.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class CommandListener extends ListenerAdapter {

    private Main plugin;
    public CommandListener(Main plugin) { this.plugin = plugin; }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String message = event.getMessage().getContentDisplay();

        if(event.getAuthor().isBot()) return;
        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel channel = event.getTextChannel();
            if (message.startsWith("#")) {
                String args[] = message.substring(1).split(" ");
                if (args.length > 0) {
                    if (!plugin.getCommandManager().perform(args[0], event.getMember(), channel, event.getMessage())) {

                        EmbedBuilder info = new EmbedBuilder()
                                .setTitle("Information")
                                .setDescription("Der Befehl ist nicht bekannt")
                                .setFooter("Bot created by Marius")
                                .setColor(Color.RED);

                        channel.sendTyping().queue();
                        channel.sendMessageEmbeds(info.build()).complete().delete().queueAfter(6, TimeUnit.SECONDS);
                        info.clear();
                    }
                }
            }
        }
    }
}
