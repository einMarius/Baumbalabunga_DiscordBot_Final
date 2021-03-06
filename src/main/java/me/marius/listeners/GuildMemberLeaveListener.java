package me.marius.listeners;

import me.marius.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GuildMemberLeaveListener extends ListenerAdapter {

    private Main plugin;

    public GuildMemberLeaveListener(Main plugin) {
        this.plugin = plugin;
    }

    private String emoji = "❔";
    private String emoji1 = "❓";
    String[] messages = {

            "Ohhhhh! %member% hat den Discord-Server verlassen.",
            "Oh nein! Wieso ist denn %member% gegangen?",
            "Auf Wiedersehen, %member%!",
            "Wir haben jemanden verloren, %member%!",
            "Woooooooosh. %member% ist weggeflogen!",
            "Ein wildes %member% ist verschwunden!",
            "%member% wurde von Wanningers Adlerblick erwischt!",
            "%member% wurde von Vannis Lostheit erwischt!.",
            "%member% wurde ge-Jommet!"

    };

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

        Random random = new Random();
        int number = random.nextInt(messages.length);

        EmbedBuilder builder = new EmbedBuilder()
                .setDescription(emoji + " " + messages[number].replace("%member%", event.getUser().getAsMention()) + " " + emoji1)
                .setColor(0xe3672d);

        event.getGuild().getTextChannelById(plugin.MOIN_TSCHOE_CHANNEL).sendMessageEmbeds(builder.build()).queue();
        builder.clear();

    }

}
