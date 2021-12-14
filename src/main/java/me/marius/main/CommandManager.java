package me.marius.main;

import me.marius.commands.type.ServerCommand;
import me.marius.commands.use.ClearCommand;
import me.marius.commands.use.MemeCommand;
import me.marius.commands.use.NewsCommand;
import me.marius.commands.use.UmfrageCommand;
import me.marius.reactionroles.RoleSelectionCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    private Main plugin;
    private JDA bot;
    public ConcurrentHashMap<String, ServerCommand> commands;

    public CommandManager(Main plugin, JDA bot) {

        this.bot = bot;
        this.plugin = plugin;
        this.commands = new ConcurrentHashMap<>();

        this.commands.put("plsmeme", new MemeCommand(plugin));
        this.commands.put("new", new NewsCommand(plugin));
        this.commands.put("umfrage", new UmfrageCommand(plugin));
        this.commands.put("roleselection", new RoleSelectionCommand(plugin));
        this.commands.put("clear", new ClearCommand());

        CommandListUpdateAction commands = bot.updateCommands();
        commands.addCommands(new CommandData("plsmeme", "Sendet ein random Meme von Reddit."));
        commands.queue();

    }

    public boolean perform(String command, Member member, TextChannel channel, Message message) {

        ServerCommand cmd;
        if ((cmd = this.commands.get(command.toLowerCase())) != null) {

            cmd.performCommand(member, channel, message);
            return true;
        }
        return false;
    }

}
