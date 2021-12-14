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
import java.util.concurrent.TimeUnit;

public class NewsCommand implements ServerCommand {

    private Main plugin;
    public NewsCommand(Main plugin) { this.plugin = plugin; }

    private String[] colours =  new String[] { "ff0000", "ff6600", "fff700", "59ff00", "00ff5e", "00eeff", "003cff", "45ffc4", "459fff", "4a2bfc", "000000", "b55400", "faef52", "93fc38", "76ff00" };

    @Override
    public void performCommand(Member member, TextChannel textChannel, Message message) {

        String args[] = message.getContentDisplay().split(" ");

        if(member.hasPermission(Permission.ADMINISTRATOR)){
            if(textChannel.getIdLong() == plugin.NEWS_CHANNEL || textChannel.getIdLong() == plugin.TEST_CHANNEL) {
                if(args.length >= 2) {
                    message.delete().queue();
                    textChannel.sendTyping().queue();

                    String news = "";
                    for(int i = 1; i < args.length; i++)
                        news = String.valueOf(news) + " " + args[i];

                    EmbedBuilder info = new EmbedBuilder()
                            .setTitle("🎉 **Neuigkeiten** 🎉")
                            .setAuthor(member.getUser().getName())
                            .setThumbnail(member.getGuild().getIconUrl())
                            .addField("Erwähnung", "<@&"+plugin.NEWS_NOTFIY+">", false)
                            .setDescription(news)
                            .setFooter(member.getUser().getName() + " hat eine Neuigkeit gepostet!" , member.getUser().getAvatarUrl())
                            .setTimestamp(LocalDateTime.now(Clock.systemUTC()))
                            .setColor(Color.GREEN);

                    textChannel.sendMessage("||<@&"+plugin.NEWS_NOTFIY+">|| \n").setEmbeds(info.build()).queue();
                    info.clear();

                }else {
                    message.delete().queue();
                    textChannel.sendTyping().queue();
                    textChannel.sendMessage("Benutze: #news <Neuigkeit>!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                }
            } else {
                message.delete().queue();
                textChannel.sendTyping().queue();
                textChannel.sendMessage("Benutze für den Command nicht diesen Channel!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            }
        }
    }
}
