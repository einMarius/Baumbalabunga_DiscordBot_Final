package me.marius.music.commands;

import me.marius.commands.type.ServerCommand;
import me.marius.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class PlayCommand implements ServerCommand {


    @SuppressWarnings("ConstantConditions")
    @Override
    public void performCommand(Member member, TextChannel textChannel, Message message) {

        final Member self = member.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        String args[] = message.getContentDisplay().split(" ");
        if (args.length >= 1) {

            if (!selfVoiceState.inAudioChannel()) {
                textChannel.sendTyping().queue();
                textChannel.sendMessage("Der Bot befindet sich in keinem Channel! Benutze #join").complete().delete().queueAfter(5, TimeUnit.SECONDS);
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


            String link = String.join(" ", message.getContentDisplay().split(" "));

            if (!isUrl(link)) {
                link = "ytsearch:" + link;
            }

            PlayerManager.getInstance().loadAndPlay(textChannel, link);

        } else {
            textChannel.sendTyping().queue();
            textChannel.sendMessage("Benutze #play <link>!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            return;
        }
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        }catch (URISyntaxException e) {
            return false;
        }
    }
}
