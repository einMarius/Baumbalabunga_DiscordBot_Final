package me.marius.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class GuildMemberJoinListener extends ListenerAdapter {

    private String emoji = "🎉";
    String[] messages = {

            "Yaaay! %member% hat den Discord-Server betreten.",
            "Oh nein! Was will denn %member% hier?",
            "Herzlich Willkommen, %member%.",
            "Wir haben dich erwartet, %member%.",
            "Woooooooosh. %member% ist gelandet!",
            "Ein wildes %member% ist aufgetaucht!",
            "Wanninger hat ein wildes %member% gespottet!",
            "Baumbalabunga Gaming wurde von %member% gejoosiet!"

    };

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e){

        Random rand = new Random();
        int number = rand.nextInt(messages.length);

        EmbedBuilder builder = new EmbedBuilder()
                .setDescription(emoji + " " + messages[number].replace("%member%", e.getMember().getAsMention()) + " " + emoji)
                .setColor(0x66d8ff);

        e.getGuild().getDefaultChannel().sendMessageEmbeds(builder.build()).queue();
        builder.clear();

        //if(!MySQL.userIsExisting(e.getMember().getId())) {
        //    e.getMember().getGuild().addRoleToMember(e.getMember(), e.getJDA().getRoleById(plugin.UNRANKED)).queue();
        //}
    }

}