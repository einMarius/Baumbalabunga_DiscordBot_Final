package me.marius.music.commands;

import me.marius.commands.type.ServerCommand;
import me.marius.music.GuildMusicManager;
import me.marius.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.TimeUnit;

public class StopCommand implements ServerCommand {

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

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        textChannel.sendMessage("Der MusikBot wurde gestoppt und die Queue wurde geleert!").queue();

    }
}
