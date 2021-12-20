package me.marius.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.marius.commands.type.ServerCommand;
import me.marius.music.GuildMusicManager;
import me.marius.music.PlayerManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueCommand implements ServerCommand {


    @Override
    public void performCommand(Member member, TextChannel textChannel, Message message) {

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(member.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if (queue.isEmpty()) {
            message.delete().queue();
            textChannel.sendTyping().queue();
            textChannel.sendMessage("Es befindet sich derzeit kein Song in der Queue!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            return;
        }

        final int trackCount = Math.min(queue.size(), 20);
        final List<AudioTrack> trackList = new ArrayList<>(queue);
        final MessageAction messageAction = textChannel.sendMessage("**Aktuelle Queue:**\n");

        for (int i = 0; i <  trackCount; i++) {
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();

            messageAction.append('#')
                    .append(String.valueOf(i + 1))
                    .append(" `")
                    .append(String.valueOf(info.title))
                    .append(" von ")
                    .append(info.author)
                    .append("` [`")
                    .append(formatTime(track.getDuration()))
                    .append("`]\n");
        }

        if (trackList.size() > trackCount) {
            messageAction.append("Und `")
                    .append(String.valueOf(trackList.size() - trackCount))
                    .append("` mehr :) ...");
        }

        messageAction.queue();

    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
