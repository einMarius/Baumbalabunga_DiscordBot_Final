package me.marius.mysql;

import me.marius.main.Main;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionListener extends ListenerAdapter {

    private Main plugin;
    public ReactionListener(Main plugin) { this.plugin = plugin; }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.getMember().getUser().isBot()) return;

        if(!plugin.getCooldownManager().isMemberInCooldown(event.getMember())){
            //MEMBER NICHT IM COOLDOWN --> Punkte hinzufügen
            if (!MySQL.userIsExisting(event.getMember().getId())) {
                MySQL.createNewPlayer(event.getMember().getId(), event.getMember().getUser().getName(), 1, 0, 1, 0, 0);
                event.getMember().getGuild().addRoleToMember(event.getMember().getId(), event.getMember().getJDA().getRoleById(plugin.UNRANKED)).queue();
            } else {
                MySQL.setPunkte(event.getMember().getId(), event.getMember().getUser().getName(), 1, 0, 1, 0, 0);
                plugin.getLevelRoles().addRoles(event.getMember());
            }
            plugin.getCooldownManager().addPlayerToMap(event.getMember(), 5);

            //MEMBER IM COOLDOWN --> Keine PUnkte hinzufügen
        } else {
            if (!MySQL.userIsExisting(event.getMember().getId())) {
                MySQL.createNewPlayer(event.getMember().getId(), event.getMember().getUser().getName(), 0, 0, 1, 0, 0);
                event.getMember().getGuild().addRoleToMember(event.getMember().getId(), event.getMember().getJDA().getRoleById(plugin.UNRANKED)).queue();
            } else {
                MySQL.setPunkte(event.getMember().getId(), event.getMember().getUser().getName(), 0, 0, 1, 0, 0);
                plugin.getLevelRoles().addRoles(event.getMember());
            }
        }

    }

}
