package me.marius.main;

import me.marius.listeners.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Main {

    private final String TOKEN = "TOKEN";

    /*
     *
     * TextChannel ID´s für den Discord
     *
     */
    public final long TEST_CHANNEL = 811935946064265229L;
    public final long MEME_CHANNEL = 673615226955890710L;

    private JDABuilder jdaBuilder;
    private CommandManager commandManager;

    public static void main(String[] args){

        new Main();

    }

    public Main() {

        jdaBuilder = JDABuilder
                .createDefault(TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.playing("mit Johannsees"))
                .setStatus(OnlineStatus.ONLINE);

        intializeListeners();

        JDA bot = null;
        try {
            bot = jdaBuilder.build();
            bot.awaitReady();
            System.out.println("[BaumbalaBunga] Der Bot sowie alle weiteren System wurden gestartet!");
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("[BaumbalaBunga] Es gab einen Fehler beim Starten des Discord-Bots!");
        }

        commandManager = new CommandManager(this, bot);

    }

    private void intializeListeners(){
        jdaBuilder
                .addEventListeners(new CommandListener(this));
    }

    private void configureMemoryUsage(JDABuilder jdaBuilder){

        jdaBuilder
                .disableCache(CacheFlag.ACTIVITY)
                .setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
                .setChunkingFilter(ChunkingFilter.NONE)
                .disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);

    }

    public CommandManager getCommandManager() { return commandManager; }
}
