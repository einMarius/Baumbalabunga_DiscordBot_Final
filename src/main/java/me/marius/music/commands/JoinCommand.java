package me.marius.music.commands;

import me.marius.commands.type.ServerCommand;
import me.marius.main.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("ConstantConditions")
public class JoinCommand implements ServerCommand {

    private Main plugin;
    private JDA bot;

    public JoinCommand(Main plugin, JDA bot) { this.plugin = plugin; this.bot = bot; }

    @Override
    public void performCommand(Member member, TextChannel textChannel, Message message) {

        String args[] = message.getContentDisplay().split(" ");

        final Member self = member.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(args.length == 1) {

            if (selfVoiceState.inAudioChannel()) {
                textChannel.sendTyping().queue();
                textChannel.sendMessage("Der Bot befindet sich bereits in einem Channel!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                return;
            }

            final GuildVoiceState memberVoiceState = member.getVoiceState();

            if (!memberVoiceState.inAudioChannel()) {
                textChannel.sendTyping().queue();
                textChannel.sendMessage("Du befindest dich in keinem VoiceChannel!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                return;
            }

            final AudioManager audioManager = member.getGuild().getAudioManager();
            final VoiceChannel memberVoiceChannel = (VoiceChannel) memberVoiceState.getChannel();

            audioManager.openAudioConnection(memberVoiceChannel);
            textChannel.sendMessageFormat("Connecting to `\uD83D\uDD0A %s`", memberVoiceChannel.getName()).queue();

        }
    }
}
