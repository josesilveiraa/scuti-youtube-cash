package com.scuti.youtube.mysql;

import com.scuti.youtube.*;
import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import java.sql.*;

public class ManagerAccounts
{
    private ScutiYoutubeCash plugin;
    
    public ManagerAccounts(final ScutiYoutubeCash plugin) {
        this.setPlugin(plugin);
    }
    
    public void addConta(final String s, final String s2) {
        new BukkitRunnable() {
            public void run() {
                try {
                    if (ScutiYoutubeCash.getInstance().getSQL().getConnection() == null || ScutiYoutubeCash.getInstance().getSQL().getConnection().isClosed()) {
                        ScutiYoutubeCash.getInstance().getSQL().open();
                    }
                    final PreparedStatement prepareStatement = ScutiYoutubeCash.getInstance().getSQL().getConnection().prepareStatement("INSERT INTO `youtubeaccounts`(`nome`, `canal`) VALUES (?,?)");
                    prepareStatement.setString(1, s);
                    prepareStatement.setString(2, s2);
                    prepareStatement.execute();
                    prepareStatement.close();
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously((Plugin)ScutiYoutubeCash.getInstance());
    }
    
    public final boolean existePlayer(final String s) {
        try {
            if (ScutiYoutubeCash.getInstance().getSQL().getConnection() == null || ScutiYoutubeCash.getInstance().getSQL().getConnection().isClosed()) {
                ScutiYoutubeCash.getInstance().getSQL().open();
            }
            final PreparedStatement prepareStatement = ScutiYoutubeCash.getInstance().getSQL().getConnection().prepareStatement("SELECT * FROM `youtubeaccounts` WHERE `nome` = ?");
            prepareStatement.setString(1, s);
            if (prepareStatement.executeQuery().next()) {
                return true;
            }
        }
        catch (SQLException ex) {}
        return false;
    }
    
    public void removerPlayer(final String s) {
        new BukkitRunnable() {
            public void run() {
                try {
                    if (ScutiYoutubeCash.getInstance().getSQL().getConnection() == null || ScutiYoutubeCash.getInstance().getSQL().getConnection().isClosed()) {
                        ScutiYoutubeCash.getInstance().getSQL().open();
                    }
                    final PreparedStatement prepareStatement = ScutiYoutubeCash.getInstance().getSQL().getConnection().prepareStatement("DELETE FROM youtubeaccounts WHERE `nome` = ?");
                    prepareStatement.setString(1, s);
                    prepareStatement.executeUpdate();
                    prepareStatement.close();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously((Plugin)ScutiYoutubeCash.getInstance());
    }
    
    public String getChannelName(final String s) {
        try {
            if (ScutiYoutubeCash.getInstance().getSQL().getConnection() == null || ScutiYoutubeCash.getInstance().getSQL().getConnection().isClosed()) {
                ScutiYoutubeCash.getInstance().getSQL().open();
            }
            final PreparedStatement prepareStatement = ScutiYoutubeCash.getInstance().getSQL().getConnection().prepareStatement("SELECT * FROM `youtubeaccounts` WHERE `nome` = ?");
            prepareStatement.setString(1, s);
            final ResultSet executeQuery = prepareStatement.executeQuery();
            if (executeQuery.next()) {
                return executeQuery.getString("canal");
            }
            prepareStatement.close();
        }
        catch (SQLException ex) {}
        return null;
    }
    
    public ScutiYoutubeCash getPlugin() {
        return this.plugin;
    }
    
    public void setPlugin(final ScutiYoutubeCash plugin) {
        this.plugin = plugin;
    }
}
