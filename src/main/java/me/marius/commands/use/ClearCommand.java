package me.marius.commands.use;

import me.marius.commands.type.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClearCommand implements ServerCommand {

    private int clearamount;

    @Override
    public void performCommand(Member member, TextChannel textChannel, Message message) {

        String args[] = message.getContentDisplay().split(" ");

        if(member.hasPermission(Permission.ADMINISTRATOR)) {
            if (args.length == 2) {

                try {
                    clearamount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    textChannel.sendMessage("Du musst eine Zahl angeben!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                    return;
                }
                if (clearamount < 100) {

                    OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);

                    List<Message> messages = textChannel.getHistory().retrievePast(Integer.parseInt(args[1]) + 1).complete();
                    messages.removeIf(message1 -> message1.getTimeCreated().isBefore(twoWeeksAgo));

                    EmbedBuilder info = new EmbedBuilder()
                            .setTitle("**Information**")
                            .setDescription(clearamount + " Nachrichten gelöscht.")
                            .setFooter("Nachrichten von " + member.getUser().getName() + " gelöscht.", member.getUser().getAvatarUrl())
                            .setColor(Color.RED);

                    textChannel.deleteMessages(messages).submit()
                            .thenComposeAsync((v) -> textChannel.sendTyping().submit())
                            .thenComposeAsync((m) -> textChannel.sendMessageEmbeds(info.build()).submit())
                            .thenComposeAsync((v) -> v.delete().submitAfter(5, TimeUnit.SECONDS))
                            .whenCompleteAsync((s, error) -> {
                                info.clear();
                                if (error != null) {
                                    error.printStackTrace();
                                    System.out.println("[AngelBot] Es gab einen Fehler beim Löschen der Nachrichten!");
                                }
                            });

                } else {
                    message.delete().queue();
                    textChannel.sendTyping().queue();
                    textChannel.sendMessage("Die Zahl muss zwischen 1 und 99 liegen!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                }
            } else {
                message.delete().queue();
                textChannel.sendTyping().queue();
                textChannel.sendMessage("Benutze #clear <Amount>!").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            }
        }
    }
}
