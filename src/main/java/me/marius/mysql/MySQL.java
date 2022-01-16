package me.marius.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySQL {

    private static Connection connection;
    private static String host = "localhost";
    private static int port = 3306;
    private static String database = "discordbot";
    private static String username = "root";
    private static String password = "";

    private static final ExecutorService service = Executors.newCachedThreadPool();

    public static boolean connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?allowMultiQueries=true", username, password);
            System.out.println("[MySQL] Die Verbindung zur MySQL-Datenbank wurde hergestellt!");
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("[MySQL] Es gab einen Fehler beim Aufbauen der Verbindung!");
        }
        return false;
    }

    public static boolean isConnected() {
        return (connection != null);
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void createTables() {
        if (!isConnected())
            if (!connect())
                return;

        service.execute(() -> {
            try {
                PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS ranking (UserID VARCHAR(100), Username VARCHAR(100), Punkte INT, Nachrichten INT, Reaktionen INT, ChannelTime INT, JoinedChannel INT);");
                ps.executeUpdate();
                System.out.println("[MySQL] Der Table wurde erfolgreich erstellt!");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("[MySQL] Es gab einen Fehler beim Erstellen des Tables!");
            }

        });
    }

    public static void createNewPlayer(String userID, String userName, int punkte, int messages, int reactions, int channeltime, int joinedchannel) {
        if (!isConnected())
            if (!connect())
                return;

            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO ranking (UserID,Username,Punkte,Nachrichten,Reaktionen,ChannelTime,JoinedChannel) VALUES (?,?,?,?,?,?,?)")) {
                ps.setString(1, userID);
                ps.setString(2, userName);
                ps.setInt(3, punkte);
                ps.setInt(4, messages);
                ps.setInt(5, reactions);
                ps.setInt(6, channeltime);
                ps.setInt(7, joinedchannel);
                ps.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("[MySQL] Es gab einen Fehler beim Erstellen eines Spielers!");
            }
    }

    public static boolean userIsExisting(String userID) {
        if (!isConnected())
            if (!connect())
                return false;

        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM ranking WHERE UserID = ?")) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("UserID").equals(userID)) {
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("[MySQL] Es gab einen Fehler beim userIsExisting-Check!");
        }

        return false;
    }

    public static void setPunkte(String userID, String username, int punkte, int nachrichten, int reaktionen, int channeltime, int joinedChannels) {
        if (!isConnected())
            if (!connect())
                return;

            try (PreparedStatement ps = connection.prepareStatement("UPDATE ranking SET Username = ?, Punkte = ?, Nachrichten = ?, Reaktionen = ?, ChannelTime = ?, JoinedChannel = ? WHERE UserID = ?")) {
                ps.setString(1, username);
                ps.setInt(2, getPunkte(userID) + punkte);
                ps.setInt(3, getNachrichten(userID) + nachrichten);
                ps.setInt(4, getReaktionen(userID) + reaktionen);
                ps.setInt(5, getChannelTime(userID) + channeltime);
                ps.setInt(6, getJoinedChannels(userID) + joinedChannels);
                ps.setString(7, userID);
                ps.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("[MySQL] Es gab einen Fehler beim Setzen der Punkte!");
            }

    }

    public static int getReaktionen(String userID) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT Reaktionen FROM ranking WHERE UserID = ?");
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("Reaktionen");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public static int getNachrichten(String userID) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT Nachrichten FROM ranking WHERE UserID = ?");
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("Nachrichten");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public static int getPunkte(String userID) {

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT Punkte FROM ranking WHERE UserID = ?");
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                return rs.getInt("Punkte");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<String> getRanking() {
        if (!isConnected())
            if (!connect())
                return null;

        ArrayList<String> ranking = new ArrayList<>();
        ranking.clear();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ranking ORDER BY Punkte DESC LIMIT 10");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ranking.add(rs.getString("Username"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return ranking;
    }

    public static ArrayList<Integer> getPunkteForTop() {
        if (!isConnected())
            if (!connect())
                return null;

        ArrayList<Integer> punkte = new ArrayList<>();
        punkte.clear();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ranking ORDER BY Punkte DESC LIMIT 10");
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                punkte.add(rs.getInt("Punkte"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return punkte;
    }

    public static int getRank(String UserID) {
        if (!isConnected())
            if (!connect())
                return -1;

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) From ranking WHERE Punkte >= (SELECT Punkte FROM ranking WHERE UserID = ?)");
            ps.setString(1, UserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("COUNT(*)");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public static int getJoinedChannels(String UserID) {
        if (!isConnected())
            if (!connect())
                return -1;

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT JoinedChannel FROM ranking WHERE UserID = ?");
            ps.setString(1, UserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                return rs.getInt("JoinedChannel");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public static int getChannelTime(String UserID) {
        if (!isConnected())
            if (!connect())
                return -1;

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT ChannelTime FROM ranking WHERE UserID = ?");
            ps.setString(1, UserID);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                return rs.getInt("ChannelTime");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return 0;
    }

}
