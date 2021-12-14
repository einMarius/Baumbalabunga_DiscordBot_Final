package me.marius.reactionroles;

import me.marius.main.Main;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.jetbrains.annotations.NotNull;

public class RoleSelectionListener extends ListenerAdapter {

    private Main plugin;
    public RoleSelectionListener(Main plugin) { this.plugin = plugin; }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event){
        if(event.getUser().isBot()) return;

        if(event.getChannel().getIdLong() == plugin.ROLE_SELECTION || event.getChannel().getIdLong() == 844183074707210261L) {

            //NEWS_NOTFIY
            if(event.getReaction().getReactionEmote().getEmoji().equalsIgnoreCase("1⃣")) {
                AuditableRestAction<Void> action = event
                        .getGuild()
                        .addRoleToMember(event.getUserId(), event.getGuild().getRoleById(plugin.NEWS_NOTFIY));
                action.queue();
                return;
            }

            //UMFRAGE_NOTIFY
            if(event.getReaction().getReactionEmote().getEmoji().equalsIgnoreCase("2⃣")) {
                AuditableRestAction<Void> action = event
                        .getGuild()
                        .addRoleToMember(event.getUserId(), event.getGuild().getRoleById(plugin.UMFRAGE_NOTIFY));
                action.queue();
            }
        }
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event){

        if(event.getChannel().getIdLong() == plugin.ROLE_SELECTION || event.getChannel().getIdLong() == 844183074707210261L) {

            //NEWS_NOTFIY
            if(event.getReaction().getReactionEmote().getEmoji().equalsIgnoreCase("1⃣")) {
                AuditableRestAction<Void> action = event
                        .getGuild()
                        .removeRoleFromMember(event.getUserId(), event.getGuild().getRoleById(plugin.NEWS_NOTFIY));
                action.queue();
                return;
            }

            //UMFRAGE_NOTIFY
            if(event.getReaction().getReactionEmote().getEmoji().equalsIgnoreCase("2⃣")) {
                AuditableRestAction<Void> action = event
                        .getGuild()
                        .removeRoleFromMember(event.getUserId(), event.getGuild().getRoleById(plugin.UMFRAGE_NOTIFY));
                action.queue();
            }
        }
    }

}
