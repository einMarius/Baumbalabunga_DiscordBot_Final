package me.marius.commands.use;

import me.marius.commands.type.ServerCommand;
import me.marius.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class UmfrageCommand implements ServerCommand {

    private Main plugin;
    public UmfrageCommand(Main plugin) { this.plugin = plugin; }

    private String thumbsup = "✅";
    private String thumbsdown = "❌";
    private String[] colours =  new String[] { "ff0000", "ff6600", "fff700", "59ff00", "00ff5e", "00eeff", "003cff", "45ffc4", "459fff", "4a2bfc", "000000", "b55400", "faef52", "93fc38", "76ff00" };

    @Override
    public void performCommand(Member member, TextChannel textChannel, Message message) {

        String args[] = message.getContentDisplay().split(" ");

        if(member.hasPermission(Permission.ADMINISTRATOR)) {
            if (textChannel.getIdLong() == plugin.UMFRAGE_CHANNEL || textChannel.getIdLong() == plugin.TEST_CHANNEL) {
                if (args.length >= 2) {

                    String umfrage = "";
                    for (int i = 1; i < args.length; i++)
                        umfrage = String.valueOf(umfrage) + " " + args[i];

                    Random rand = new Random();
                    int colour_random = rand.nextInt(colours.length);
                    String colour = colours[colour_random];

                    EmbedBuilder abstimmung = new EmbedBuilder()
                            .setTitle("🎆 **Umfrage** 🎆")
                            .setThumbnail(member.getGuild().getIconUrl())
                            .setDescription(umfrage)
                            .setFooter(member.getUser().getName() + " hat eine Umfarge gestartet!", member.getUser().getAvatarUrl())
                            .setTimestamp(LocalDateTime.now(Clock.systemUTC()))
                            .setColor(Color.decode("0x" + colour));

                    message.delete().submit()
                            .thenComposeAsync((v) -> textChannel.sendTyping().submit())
                            .thenComposeAsync((m) -> textChannel.sendTyping().submit())
                            .whenCompleteAsync((s, error) -> {
                                textChannel.sendMessage("||<@&" + plugin.UMFRAGE_NOTIFY + ">|| \n").setEmbeds(abstimmung.build()).queue((embedMessage) -> {

                                    embedMessage.addReaction(thumbsup).queue();
                                    embedMessage.addReaction(thumbsdown).queue();

                                });
                                abstimmung.clear();
                                if (error != null) {
                                    error.printStackTrace();
                                    System.out.println("[AngelBot] Es gab einen Fehler beim Sender der Nachricht!");
                                    abstimmung.clear();
                                }
                            });

                }
            } else {
                message.delete().queue();
                textChannel.sendTyping().queue();
                textChannel.sendMessage("Benutze für den Command nicht diesen Channel!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            }
        }
    }
}
