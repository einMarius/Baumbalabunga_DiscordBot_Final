package me.marius.main;

import me.marius.commands.type.ServerCommand;
import me.marius.commands.use.*;
import me.marius.reactionroles.RoleSelectionCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

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
        this.commands.put("zitat", new ZitatCommand(plugin));
        this.commands.put("stats", new StatsCommand(plugin));
        this.commands.put("top", new TopCommand(plugin));

        //Music
        /*this.commands.put("join", new JoinCommand(plugin, bot));
        this.commands.put("play", new PlayCommand());
        this.commands.put("queue", new QueueCommand());
        this.commands.put("stop", new StopCommand());
        this.commands.put("skip", new SkipCommand());
        this.commands.put("nowplaying", new NowPlayingCommand());*/

        /*bot.upsertCommand("stats", "Zeigt dir die Stats von dir")
                .addSubcommands(new SubcommandData("User", "User des Discord Servers"))
                .addOption(OptionType.USER, "User", "Zeigt die Stats eines Users").queue();*/

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
