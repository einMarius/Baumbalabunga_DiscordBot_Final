package me.marius.commands.use;

import me.marius.commands.type.ServerCommand;
import me.marius.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class ZitatCommand implements ServerCommand {

    private Main plugin;

    public ZitatCommand(Main plugin) { this.plugin = plugin; }

    @Override
    public void performCommand(Member member, TextChannel textChannel, Message message) {

        String args[] = message.getContentDisplay().split(" ");

        if(textChannel.getIdLong() == plugin.ZITATE_CHANNEL){
            if(args.length >= 2) {

                String lehrer = args[1];
                String zitat = "";
                for (int i = 2; i < args.length; i++)
                    zitat = String.valueOf(zitat) + " " + args[i];

                EmbedBuilder umfrage = new EmbedBuilder()
                        .setTitle("Zitat")
                        .setDescription(lehrer + " sagte: " + zitat + "")
                        .setFooter("Zitat erstellt von " + member.getUser().getName(), member.getUser().getAvatarUrl())
                        .setColor(Color.RED);

                message.delete().submit()
                        .thenComposeAsync((v) -> textChannel.sendTyping().submit())
                        .whenCompleteAsync((s, error) -> {
                            textChannel.sendMessageEmbeds(umfrage.build()).queue();
                            umfrage.clear();

                            if (error != null) {
                                error.printStackTrace();
                                System.out.println("[AngelBot] Es gab einen Fehler beim Senden der Nachricht!");
                                umfrage.clear();
                            }
                        });

            } else {
                message.delete().queue();
                textChannel.sendTyping().queue();
                textChannel.sendMessage("Benutze: #zitate <Person> <Zitat>!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            }
        } else {
            message.delete().queue();
            textChannel.sendTyping().queue();
            textChannel.sendMessage("Benutze für den Command nicht diesen Channel!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
        }
    }
}
