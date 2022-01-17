package me.marius.commands.use;

import me.marius.commands.type.ServerCommand;
import me.marius.main.Main;
import me.marius.mysql.MySQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class TopCommand implements ServerCommand {

    private Main plugin;
    public TopCommand(Main plugin) { this.plugin = plugin; }

    @Override
    public void performCommand(Member member, TextChannel textChannel, Message message) {

        String args[] = message.getContentDisplay().split(" ");

        if(!(textChannel.getIdLong() == plugin.STATS_CHANNEL)) return;
        if(!(args.length == 1)) return;

        message.delete().queue();
        textChannel.sendTyping().queue();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("⚠ **Die Top 10** ⚠");
        builder.setDescription("**Das ist die aktuelle Top 10**");
        for(int i = 0; i < MySQL.getRanking().size(); i++){
            int place = i + 1;
            builder.addField(">>> Platz " + place, MySQL.getRanking().get(i) + " mit " + MySQL.getPunkteForTop().get(i) + " Punkten", false);
        }
        builder.setThumbnail(member.getGuild().getIconUrl());
        builder.setFooter("Bot created by Marius", member.getGuild().getIconUrl());
        builder.setColor(Color.GREEN);

        textChannel.sendMessageEmbeds(builder.build()).queue();
        builder.clear();
    }
}
