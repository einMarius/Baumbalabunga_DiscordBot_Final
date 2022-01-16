package me.marius.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter {

    public final AudioPlayer player;
    public final BlockingDeque<AudioTrack> queue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingDeque<>();
    }

    public void addToQueue(AudioTrack audioTrack) {
        queue.addLast(audioTrack);
        startNextTrack(true);
    }

    public List<AudioTrack> drainQueue() {
        List<AudioTrack> drainedQueue = new ArrayList<>();
        queue.drainTo(drainedQueue);
        return drainedQueue;
    }

    public void playNow(AudioTrack audioTrack, boolean clearQueue) {
        if(clearQueue)
            queue.clear();

        queue.addFirst(audioTrack);
        startNextTrack(false);
    }

    public void skip() {
        startNextTrack(false);
    }


    private void startNextTrack(boolean noInterrupt) {
        AudioTrack next = queue.pollFirst();

        if(next != null) {
            if(!player.startTrack(next, noInterrupt))
                queue.addFirst(next);
        } else {
            player.stopTrack(); //NACHRICHT SENDEN!
        }
    }

    public void queue(AudioTrack track) {
        if (!this.player.startTrack(track, true)) {
            this.queue.offer(track);
        }
    }

    public void nextTrack() {
        this.player.startTrack(this.queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
}
