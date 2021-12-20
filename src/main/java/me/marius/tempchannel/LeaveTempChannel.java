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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
                        .setDescription("**Dein privater Channel wurde gelöscht " + getKeysByValue(plugin.tempchannels, voiceChannel.getIdLong()) +
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
                        .setDescription("**Dein privater Channel wurde gelöscht " + getKeysByValue(plugin.tempchannels, voiceChannel.getIdLong()) +
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

    private <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

}
