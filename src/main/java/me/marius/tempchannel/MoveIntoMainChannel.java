package me.marius.tempchannel;

import me.marius.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Clock;
import java.time.LocalDateTime;

public class MoveIntoMainChannel extends ListenerAdapter {

    private Main plugin;
    public MoveIntoMainChannel(Main plugin) { this.plugin = plugin; }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if(event.getMember().getUser().isBot()) return;

        VoiceChannel voiceChannel = (VoiceChannel) event.getChannelJoined();
        TextChannel textChannel = event.getGuild().getTextChannelById(plugin.TEMP_CHANNEL);
        Category category = voiceChannel.getParentCategory();

        if(voiceChannel.getIdLong() == plugin.JOIN_CHANNEL) {
            if (!plugin.tempchannels.containsKey(event.getMember())) {

                assert category != null;
                VoiceChannel tempChannel = category.createVoiceChannel("⏳ | " + event.getMember().getEffectiveName() + "´s Channel").complete();
                tempChannel.upsertPermissionOverride(event.getMember()).grant(Permission.MANAGE_CHANNEL).grant(Permission.VOICE_MOVE_OTHERS).grant(Permission.MANAGE_PERMISSIONS).queue((channel) -> {
                    channel.getGuild().moveVoiceMember(event.getMember(), tempChannel).queue();
                });

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("**⏳ | Temporäre Channel**")
                        .setDescription("**Dein privater Channel wurde erstellt " + event.getMember().getAsMention() +
                                " \nDu wurdest in diesen hineingemoved!**")
                        .setThumbnail(event.getMember().getUser().getAvatarUrl())
                        .setColor(Color.GREEN)
                        .setFooter("Bot created by Marius")
                        .setTimestamp(LocalDateTime.now(Clock.systemUTC()));

                textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
                embedBuilder.clear();
                //textChannel.sendMessage("Channel created for " + event.getMember().getAsMention()).queue();

                plugin.tempchannels.put(event.getMember(), tempChannel.getIdLong());
            } else {

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("**⏳ | Temporäre Channel**")
                        .setDescription("**Du hast bereits einen privaten Channel! " + event.getMember().getAsMention() +
                                " \nDu wurdest in diesen zurückgemoved!**")
                        .setThumbnail(event.getMember().getUser().getAvatarUrl())
                        .setColor(Color.ORANGE)
                        .setFooter("Bot created by Marius")
                        .setTimestamp(LocalDateTime.now(Clock.systemUTC()));

                textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
                embedBuilder.clear();
                //textChannel.sendMessage("Moved back into your channel!" + event.getMember().getAsMention()).queue();

                event.getMember().getGuild().moveVoiceMember(event.getMember(), event.getGuild().getVoiceChannelById(plugin.tempchannels.get(event.getMember()))).queue();

            }
        }
    }
}
