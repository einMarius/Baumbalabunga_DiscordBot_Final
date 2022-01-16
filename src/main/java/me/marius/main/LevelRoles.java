package me.marius.main;

import me.marius.mysql.MySQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LevelRoles {

    private Main plugin;

    public LevelRoles(Main plugin) {
        this.plugin = plugin;
    }

    private final String[] colours = new String[]{
            "ff0000", "ff6600", "fff700", "59ff00", "00ff5e", "00eeff", "003cff"
    };
    private final ExecutorService service = Executors.newCachedThreadPool();

    public void addRoles(Member member) {
        Random rand = new Random();
        int i = rand.nextInt(colours.length);
        String colour = colours[i];
        //RANK 5
        if (MySQL.getPunkte(member.getId()) >= 20000) {

            member.getGuild().removeRoleFromMember(member.getId(), member.getJDA().getRoleById(plugin.RANK_4)).queue();
            member.getGuild().addRoleToMember(member.getId(), member.getJDA().getRoleById(plugin.RANK_5)).queue();

            member.getUser().openPrivateChannel().queue(channel -> {

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("◽ **Levelup** ◽")
                        .setDescription("**Glückwunsch du bist jetzt Level 5**")
                        .addField(">>> Neue Rolle: ", "Rolle: `" + member.getJDA().getRoleById(plugin.RANK_5).getName() + "` ", false)
                        .addField(">>> Neue Berechtigung", "Berechtigung: `No new permission.`", false)
                        .setThumbnail(member.getUser().getAvatarUrl())
                        .setFooter("Bot created by Marius", member.getGuild().getIconUrl())
                        .setColor(Color.decode("0x" + colour));

                channel.sendMessageEmbeds(builder.build()).queue();
                builder.clear();
            });

            //RANK 4
        } else if (MySQL.getPunkte(member.getId()) >= 10000) {
            member.getGuild().removeRoleFromMember(member.getId(), member.getJDA().getRoleById(plugin.RANK_3)).queue();
            member.getGuild().addRoleToMember(member.getId(), member.getJDA().getRoleById(plugin.RANK_4)).queue();

            member.getUser().openPrivateChannel().queue(channel -> {

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("◽ **Levelup** ◽")
                        .setDescription("**Glückwunsch du bist jetzt Level 4**")
                        .addField(">>> Neue Rolle: ", "Rolle: `" + member.getJDA().getRoleById(plugin.RANK_4).getName() + "` ", false)
                        .addField(">>> Neue Berechtigung", "Berechtigung: `No new permission.`", false)
                        .setThumbnail(member.getUser().getAvatarUrl())
                        .setFooter("Bot created by Marius", member.getGuild().getIconUrl())
                        .setColor(Color.decode("0x" + colour));

                channel.sendMessageEmbeds(builder.build()).queue();
                builder.clear();
            });

            //RANK 3
        } else if (MySQL.getPunkte(member.getId()) >= 5000) {

            member.getGuild().removeRoleFromMember(member.getId(), member.getJDA().getRoleById(plugin.RANK_2)).queue();
            member.getGuild().addRoleToMember(member.getId(), member.getJDA().getRoleById(plugin.RANK_3)).queue();

            member.getUser().openPrivateChannel().queue(channel -> {

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("◽ **Levelup** ◽")
                        .setDescription("**Glückwunsch du bist jetzt Level 3**")
                        .addField(">>> Neue Rolle: ", "Rolle: `" + member.getJDA().getRoleById(plugin.RANK_3).getName() + "` ", false)
                        .addField(">>> Neue Berechtigung", "Berechtigung: `No new permission.`", false)
                        .setThumbnail(member.getUser().getAvatarUrl())
                        .setFooter("Bot created by Marius", member.getGuild().getIconUrl())
                        .setColor(Color.decode("0x" + colour));

                channel.sendMessageEmbeds(builder.build()).queue();
                builder.clear();
            });

            //RANK 2
        } else if (MySQL.getPunkte(member.getId()) >= 1000) {

            member.getGuild().removeRoleFromMember(member.getId(), member.getJDA().getRoleById(plugin.RANK_1)).queue();
            member.getGuild().addRoleToMember(member.getId(), member.getJDA().getRoleById(plugin.RANK_2)).queue();

            member.getUser().openPrivateChannel().queue(channel -> {

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("◽ **Levelup** ◽")
                        .setDescription("**Glückwunsch du bist jetzt Level 2**")
                        .addField(">>> Neue Rolle: ", "Rolle: `" + member.getJDA().getRoleById(plugin.RANK_2).getName() + "` ", false)
                        .addField(">>> Neue Berechtigung", "Berechtigung: `No new permission.`", false)
                        .setThumbnail(member.getUser().getAvatarUrl())
                        .setFooter("Bot created by Marius", member.getGuild().getIconUrl())
                        .setColor(Color.decode("0x" + colour));

                channel.sendMessageEmbeds(builder.build()).queue();
                builder.clear();
            });

            //RANK 1
        } else if (MySQL.getPunkte(member.getId()) >= 500) {

            member.getGuild().removeRoleFromMember(member.getId(), member.getJDA().getRoleById(plugin.UNRANKED)).queue();
            member.getGuild().addRoleToMember(member.getId(), member.getJDA().getRoleById(plugin.RANK_1)).queue();

            member.getUser().openPrivateChannel().queue(channel -> {

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("◽ **Levelup** ◽")
                        .setDescription("**Glückwunsch du bist jetzt Level 1**")
                        .addField(">>> Neue Rolle: ", "Rolle: `" + member.getJDA().getRoleById(plugin.RANK_1).getName() + "` ", false)
                        .addField(">>> Neue Berechtigung", "Berechtigung: `No new permission.`", false)
                        .setThumbnail(member.getUser().getAvatarUrl())
                        .setFooter("Bot created by Marius", member.getGuild().getIconUrl())
                        .setColor(Color.decode("0x" + colour));

                channel.sendMessageEmbeds(builder.build()).queue();
                builder.clear();
            });
        }
    }
}
