package me.marius.reactionroles;

import me.marius.commands.type.ServerCommand;
import me.marius.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class RoleSelectionCommand implements ServerCommand {

    private Main plugin;
    public RoleSelectionCommand(Main plugin) { this.plugin = plugin;}

    @Override
    public void performCommand(Member member, TextChannel textChannel, Message message) {

        String args[] = message.getContentDisplay().split(" ");

        if(member.hasPermission(Permission.MESSAGE_MANAGE)){
            if(textChannel.getIdLong() == plugin.ROLE_SELECTION || textChannel.getIdLong() == 844183074707210261L) {

                EmbedBuilder reactionrole = new EmbedBuilder()
                        .setTitle("**Baumbalabunga | Benachrichtigungen**")
                        .setDescription("**Reagiere mit dem jeweiligen Emoji, um bestimmte Benachrichtigungen zu erhalten.**")
                        .addField("**News-Role**", ">>> :one: `-` Ankündigung", false)
                        .addField("**Umfrage-Role**", ">>> :two: `-` Umfrage", false)
                        .setThumbnail(member.getGuild().getIconUrl())
                        .setFooter("Bot created by Marius")
                        .setColor(Color.decode("0x242323"));

                if(args.length == 1){
                    message.delete().submit()
                            .thenComposeAsync((v) -> textChannel.sendTyping().submit())
                            .thenComposeAsync((m) -> textChannel.sendMessageEmbeds(reactionrole.build()).submit())
                            .thenComposeAsync((v) -> v.addReaction("1⃣").and(v.addReaction("2⃣")).submit())
                            .whenCompleteAsync((s, error)-> {
                                reactionrole.clear();
                                if(error != null) {
                                    error.printStackTrace();
                                    System.out.println("[Baumbalabunga] Es gab einen Fehler beim Erstellen der Nachricht!");
                                }
                            });

                } else {
                    message.delete().queue();
                    textChannel.sendMessage("Benutze: #reactionrole").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                }
            } else {
                message.delete().queue();
                textChannel.sendMessage("Benutze für den Command nicht diesen Channel!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            }
        }

    }
}
