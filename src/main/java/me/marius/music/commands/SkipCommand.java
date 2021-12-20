package me.marius.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.marius.commands.type.ServerCommand;
import me.marius.music.GuildMusicManager;
import me.marius.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.TimeUnit;

public class SkipCommand implements ServerCommand {


    @Override
    public void performCommand(Member member, TextChannel textChannel, Message message) {

        final Member self = member.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inAudioChannel()) {
            textChannel.sendMessage("Der Bot befindet sich in keinem Channel! Benutze #join").queue();
            return;
        }

        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inAudioChannel()) {
            textChannel.sendTyping().queue();
            textChannel.sendMessage("Du befindest dich in keinem VoiceChannel!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            textChannel.sendTyping().queue();
            textChannel.sendMessage("Du musst dich in dem Channel des Bots befinden!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(member.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            textChannel.sendMessage("Derzeit wird kein Track gespielt!").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
        textChannel.sendMessage("Der aktuelle Track wurde geskipped!").queue();

    }
}
