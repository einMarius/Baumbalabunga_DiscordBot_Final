package me.marius.main;

import me.marius.listeners.CommandListener;
import me.marius.listeners.GuildMemberJoinListener;
import me.marius.listeners.GuildMemberLeaveListener;
import me.marius.mysql.JoinMoveQuitChannelListener;
import me.marius.mysql.MessageReceivedListener;
import me.marius.mysql.MySQL;
import me.marius.mysql.ReactionListener;
import me.marius.reactionroles.RoleSelectionListener;
import me.marius.tempchannel.JoinMainChannel;
import me.marius.tempchannel.LeaveTempChannel;
import me.marius.tempchannel.MoveIntoMainChannel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.commons.collections4.map.HashedMap;

import javax.security.auth.login.LoginException;
import java.util.Map;

public class Main {

    private final String TOKEN = ""; //MYSQL PASSWORT ÄNDERN!

    /*
     *
     * Guild ID für den Discord
     *
     */
    public final long GUILD_ID = 525729848413519886L;

    /*
     *
     * TextChannel ID´s für den Discord
     *
     */
    public final long TEST_CHANNEL = 811935946064265229L; //VON TESTDISCORD!
    public final long MEME_CHANNEL = 673615226955890710L;
    public final long NEWS_CHANNEL = 735904029606936598L;
    public final long ROLE_SELECTION = 816270150859882496L;
    public final long UMFRAGE_CHANNEL = 811948730142949436L;
    public final long TEMP_CHANNEL = 921756035058892831L;
    public final long MOIN_TSCHOE_CHANNEL = 812007747087368202L;
    public final long ZITATE_CHANNEL = 799597621224275988L;
    public final long STATS_CHANNEL = 825103970270707743L;

    /*
     *
     * Rollen ID´s für den Discord
     *
     */
    final public long UNRANKED = 824983261197500440L;
    final public long RANK_1 = 824983198039015444L;
    final public long RANK_2 = 824983050458759198L;
    final public long RANK_3 = 824983002970325002L;
    final public long RANK_4 = 824982992170516500L;
    final public long RANK_5 = 824982938717782046L;

    public final long NEWS_NOTFIY = 816243134090444812L;
    public final long UMFRAGE_NOTIFY = 816385581738885182L;

    /*
     *
     * VoiceChannel ID´s für den Discord
     *
     */
    public final long JOIN_CHANNEL = 921753075985809428L;

    public Map<Member, Long> tempchannels;

    private JDABuilder jdaBuilder;
    private CommandManager commandManager;
    private MySQL mySQL;
    private CooldownManager cooldownManager;
    private LevelRoles levelRoles;

    public static void main(String[] args){

        new Main();

    }

    public Main() {

        jdaBuilder = JDABuilder
                .createDefault(TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_VOICE_STATES)
                .setActivity(Activity.playing("mit Johannsees"))
                .setStatus(OnlineStatus.ONLINE);

        intializeListeners();
        configureMemoryUsage(jdaBuilder);

        JDA bot = null;
        try {
            bot = jdaBuilder.build();
            bot.awaitReady();
            System.out.println("[BaumbalaBunga] Der Bot sowie alle weiteren Systeme wurden gestartet!");
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("[BaumbalaBunga] Es gab einen Fehler beim Starten des Discord-Bots!");
        }

        tempchannels = new HashedMap<>();
        commandManager = new CommandManager(this, bot);
        mySQL = new MySQL();
        cooldownManager = new CooldownManager();
        levelRoles = new LevelRoles(this);

        MySQL.connect();

    }

    private void intializeListeners(){
        jdaBuilder
                .addEventListeners(new CommandListener(this))
                .addEventListeners(new GuildMemberJoinListener(this))
                .addEventListeners(new GuildMemberLeaveListener(this))
                .addEventListeners(new RoleSelectionListener(this))
                .addEventListeners(new MoveIntoMainChannel(this))
                .addEventListeners(new JoinMainChannel(this))
                .addEventListeners(new LeaveTempChannel(this))
                .addEventListeners(new MessageReceivedListener(this))
                .addEventListeners(new JoinMoveQuitChannelListener(this))
                .addEventListeners(new ReactionListener(this));
    }

    private void configureMemoryUsage(JDABuilder jdaBuilder){

        jdaBuilder
                .disableCache(CacheFlag.ACTIVITY)
                .enableCache(CacheFlag.VOICE_STATE)
                .setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
                .setChunkingFilter(ChunkingFilter.NONE)
                .disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);

    }

    public CommandManager getCommandManager() { return commandManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
    public LevelRoles getLevelRoles() { return levelRoles; }
}
