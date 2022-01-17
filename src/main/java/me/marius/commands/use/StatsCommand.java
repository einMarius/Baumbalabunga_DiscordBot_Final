package me.marius.commands.use;

import me.marius.commands.type.ServerCommand;
import me.marius.main.CommandManager;
import me.marius.main.Main;
import me.marius.mysql.MySQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StatsCommand extends ListenerAdapter implements ServerCommand {

    private Main plugin;

    public StatsCommand(Main plugin) {
        this.plugin = plugin;
    }

    private String[] colours = new String[]{
            "ff0000", "ff6600", "fff700", "59ff00", "00ff5e", "00eeff", "003cff"
    };

    @Override
    public void performCommand(Member member, TextChannel textChannel, Message message) {
        String args[] = message.getContentDisplay().split(" ");
        Random rand = new Random();
        int i = rand.nextInt(colours.length);
        String colour = colours[i];

        if(!(textChannel.getIdLong() == plugin.STATS_CHANNEL)) return;

        message.delete().queue();
        if (args.length == 1) {

            int rank = MySQL.getRank(member.getId());
            int punkte = MySQL.getPunkte(member.getId());
            int nachrichten = MySQL.getNachrichten(member.getId());
            int reaktionen = MySQL.getReaktionen(member.getId());
            int joinedchannels = MySQL.getJoinedChannels(member.getId());
            //int channeltime = MySQL.getChannelTime(event.getMember().getId());

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("◽ **Stats von " + member.getUser().getName() + "** ◽")
                    .setDescription("**Hier siehst du die Stats von `" + member.getUser().getName() + "`**")
                    .addField(">>> Der Rank", "Rank: `" + rank + "`", false)
                    .addField(">>> Die Punkte", "Punkte: `" + punkte + "`", false)
                    .addField(">>> Die gesendeten Nachrichten", "Nachrichten: `" + nachrichten + "`", false)
                    .addField(">>> Die hinzugefügten Reaktionen", "Reaktionen: `" + reaktionen + "`", false)
                    .addField(">>> Die Anzahl der gejointen Channels", "Joined: `" + joinedchannels + "`", false)
                    .addField(">>> Die Minutenzahl der Zeit im Channel", "Minuten: `-1`", false)
                    .setThumbnail(member.getUser().getAvatarUrl())
                    .setFooter("Bot created by Marius", member.getGuild().getIconUrl())
                    .setColor(Color.decode("0x" + colour));

            textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
            embedBuilder.clear();
        } else if (args.length >= 2) {
            Member targett = message.getMentionedMembers().get(0);

            int rank = MySQL.getRank(targett.getId());
            int punkte = MySQL.getPunkte(targett.getId());
            int nachrichten = MySQL.getNachrichten(targett.getId());
            int reaktionen = MySQL.getReaktionen(targett.getId());
            int joinedchannels = MySQL.getJoinedChannels(targett.getId());
            //int channeltime = MySQL.getChannelTime(event.getMember().getId());

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("◽ **Stats von " + targett.getUser().getName() + "** ◽")
                    .setDescription("**Hier siehst du die Stats von `" + targett.getUser().getName() + "`**")
                    .addField(">>> Der Rank", "Rank: `" + rank + "`", false)
                    .addField(">>> Die Punkte", "Punkte: `" + punkte + "`", false)
                    .addField(">>> Die gesendeten Nachrichten", "Nachrichten: `" + nachrichten + "`", false)
                    .addField(">>> Die hinzugefügten Reaktionen", "Reaktionen: `" + reaktionen + "`", false)
                    .addField(">>> Die Anzahl der gejointen Channels", "Joined: `" + joinedchannels + "`", false)
                    .addField(">>> Die Minutenzahl der Zeit im Channel", "Minuten: `-1`", false)
                    .setThumbnail(targett.getUser().getAvatarUrl())
                    .setFooter("Bot created by Marius", targett.getGuild().getIconUrl())
                    .setColor(Color.decode("0x" + colour));

            textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
            embedBuilder.clear();
        }

    }


    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        Member member = event.getMember();
        TextChannel textChannel = event.getTextChannel();
        String args[] = event.getCommandString().split(" ");

        if (!event.getName().equals("stats")) return;
        if (args.length == 1) {

            int rank = MySQL.getRank(event.getMember().getId());
            int punkte = MySQL.getPunkte(event.getMember().getId());
            int nachrichten = MySQL.getNachrichten(event.getMember().getId());
            int reaktionen = MySQL.getReaktionen(event.getMember().getId());
            int joinedchannels = MySQL.getJoinedChannels(event.getMember().getId());
            //int channeltime = MySQL.getChannelTime(event.getMember().getId());

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("◽ **Stats von " + event.getUser().getName() + "** ◽")
                    .setDescription("**Hier siehst du die Stats von `" + member.getUser().getName() + "`**")
                    .addField(">>> Der Rank", "Rank: `" + rank + "`", false)
                    .addField(">>> Die Punkte", "Punkte: `" + punkte + "`", false)
                    .addField(">>> Die gesendeten Nachrichten", "Nachrichten: `" + nachrichten + "`", false)
                    .addField(">>> Die hinzugefügten Reaktionen", "Reaktionen: `" + reaktionen + "`", false)
                    .addField(">>> Die Anzahl der gejointen Channels", "Joined: `" + joinedchannels + "`", false)
                    .addField(">>> Die Minutenzahl der Zeit im Channel", "Minuten: `-1`", false)
                    .setThumbnail(member.getUser().getAvatarUrl())
                    .setFooter("Bot created by Marius", member.getGuild().getIconUrl())
                    .setColor(Color.decode("0x" + colours));

            event.replyEmbeds(embedBuilder.build()).submit()
                    .thenRunAsync(() -> textChannel.sendMessageFormat("`Stats wurden geladen! Channel Time ist derzeit nicht verfügbar!` :white_check_mark:").queueAfter(5, TimeUnit.SECONDS));
            embedBuilder.clear();
        } else if (args.length == 2) {
            String targett = args[1];

            //Umändern auf targett!
            int rank = MySQL.getRank(event.getMember().getId());
            int punkte = MySQL.getPunkte(event.getMember().getId());
            int nachrichten = MySQL.getNachrichten(event.getMember().getId());
            int reaktionen = MySQL.getReaktionen(event.getMember().getId());
            int joinedchannels = MySQL.getJoinedChannels(event.getMember().getId());
            //int channeltime = MySQL.getChannelTime(event.getMember().getId());

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("◽ **Stats von \" + m.getUser().getName() + \"** ◽")
                    .setDescription("**Hier siehst du die Stats von `" + member.getUser().getName() + "`**")
                    .addField(">>> Der Rank", "Rank: `" + rank + "`", false)
                    .addField(">>> Die Punkte", "Punkte: `" + punkte + "`", false)
                    .addField(">>> Die gesendeten Nachrichten", "Nachrichten: `" + nachrichten + "`", false)
                    .addField(">>> Die hinzugefügten Reaktionen", "Reaktionen: `" + reaktionen + "`", false)
                    .addField(">>> Die Anzahl der gejointen Channels", "Joined: `" + joinedchannels + "`", false)
                    .addField(">>> Die Minutenzahl der Zeit im Channel", "Minuten: `-1`", false)
                    .setThumbnail(member.getUser().getAvatarUrl())
                    .setFooter("Bot created by Marius", member.getGuild().getIconUrl())
                    .setColor(Color.decode("0x" + colours));

            event.replyEmbeds(embedBuilder.build()).submit()
                    .thenRunAsync(() -> textChannel.sendMessageFormat("`Stats wurden geladen! Channel Time ist derzeit nicht verfügbar!` :white_check_mark:").queueAfter(5, TimeUnit.SECONDS));
            embedBuilder.clear();
        }
    }
}
