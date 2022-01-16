package me.marius.tempchannel;

import me.marius.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;

public class LeaveTempChannel extends ListenerAdapter {

    private Main plugin;
    public LeaveTempChannel(Main plugin) { this.plugin = plugin; }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {

        VoiceChannel voiceChannel = (VoiceChannel) event.getChannelLeft();
        TextChannel textChannel = event.getGuild().getTextChannelById(plugin.TEMP_CHANNEL);

        if(voiceChannel.getMembers().size() == 0) {
            if(plugin.tempchannels.containsValue(voiceChannel.getIdLong())){

                voiceChannel.delete().queue();

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("**⏳ | Temporäre Channel**")
                        .setDescription("**Der private Channel wurde gelöscht!" +
                                " \nJoine erneut in den Join-Channel, um erneut einen privaten Channel erstellen zu lassen!**")
                        .setThumbnail(event.getMember().getUser().getAvatarUrl())
                        .setColor(Color.RED)
                        .setFooter("Bot created by Marius")
                        .setTimestamp(LocalDateTime.now(Clock.systemUTC()));

                textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
                embedBuilder.clear();
                //textChannel.sendMessage("Your voicechannel was deleted! @" + getKeysByValue(plugin.tempchannels, voiceChannel.getIdLong())).queue();

                plugin.tempchannels.entrySet().removeIf(entry -> voiceChannel.getIdLong() == entry.getValue());

            }
        }

    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event){

        VoiceChannel voiceChannel = (VoiceChannel) event.getChannelLeft();
        TextChannel textChannel = event.getGuild().getTextChannelById(plugin.TEMP_CHANNEL);

        if(voiceChannel.getMembers().size() == 0){
            if(plugin.tempchannels.containsValue(voiceChannel.getIdLong())){

                voiceChannel.delete().queue();

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("**⏳ | Temporäre Channel**")
                        .setDescription("**Der private Channel wurde gelöscht!" +
                                " \nJoine erneut in den Join-Channel, um erneut einen privaten Channel erstellen zu lassen!**")
                        .setThumbnail(event.getMember().getUser().getAvatarUrl())
                        .setColor(Color.RED)
                        .setFooter("Bot created by Marius")
                        .setTimestamp(LocalDateTime.now(Clock.systemUTC()));

                textChannel.sendMessageEmbeds(embedBuilder.build()).queue();
                embedBuilder.clear();
                //textChannel.sendMessage("Your voicechannel was deleted! @" + getKeysByValue(plugin.tempchannels, voiceChannel.getIdLong())).queue();

                plugin.tempchannels.entrySet().removeIf(entry -> voiceChannel.getIdLong() == entry.getValue());

            }
        }

    }
}
