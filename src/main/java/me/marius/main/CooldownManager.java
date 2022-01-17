package me.marius.main;

import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CooldownManager {

    private Map<Member, Integer> playerCoolDownMap = new HashMap<>();

    public CooldownManager() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Member member : playerCoolDownMap.keySet()){
                    if(playerCoolDownMap.get(member) == 1){
                        playerCoolDownMap.remove(member);
                        continue;
                    }

                    playerCoolDownMap.put(member, playerCoolDownMap.get(member)-1);
                }
            }
        }, 0, 1000);
    }

    public void addPlayerToMap(Member member, Integer time) {
        playerCoolDownMap.put(member, time);
    }

    public boolean isMemberInCooldown(Member member) {
        return  playerCoolDownMap.containsKey(member);
    }
}
