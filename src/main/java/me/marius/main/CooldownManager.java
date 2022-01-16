package me.marius.main;

import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {

    private Map<Member, Integer> playerCoolDownMap = new HashMap<>();

    public CooldownManager() {
        new Runnable() {
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
        };
    }

    public void addPlayerToMap(Member member, Integer time) {
        playerCoolDownMap.put(member, time);
    }

    public boolean isMemberInCooldown(Member member) {
        return  playerCoolDownMap.containsKey(member.getId());
    }
}
