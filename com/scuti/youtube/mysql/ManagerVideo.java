package com.scuti.youtube.mysql;

import com.scuti.youtube.*;
import org.bukkit.scheduler.*;
import java.sql.*;
import org.bukkit.plugin.*;

public class ManagerVideo
{
    private ScutiYoutubeCash plugin;
    
    public ManagerVideo(final ScutiYoutubeCash plugin) {
        this.setPlugin(plugin);
    }
    
    public void addVideo(final String s, final long n, final String s2) {
        new BukkitRunnable() {
            public void run() {
                try {
                    if (ScutiYoutubeCash.getInstance().getSQL().getConnection() == null || ScutiYoutubeCash.getInstance().getSQL().getConnection().isClosed()) {
                        ScutiYoutubeCash.getInstance().getSQL().open();
                    }
                    final PreparedStatement prepareStatement = ScutiYoutubeCash.getInstance().getSQL().getConnection().prepareStatement("INSERT INTO `youtubevideos`(`video`, `cash`, `reivindicado`) VALUES (?,?,?)");
                    prepareStatement.setString(1, s);
                    prepareStatement.setLong(2, n);
                    prepareStatement.setString(3, s2);
                    prepareStatement.execute();
                    prepareStatement.close();
                }
                catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously((Plugin)ScutiYoutubeCash.getInstance());
    }
    
    public final boolean hasVideo(final String s) {
        try {
            if (ScutiYoutubeCash.getInstance().getSQL().getConnection() == null || ScutiYoutubeCash.getInstance().getSQL().getConnection().isClosed()) {
                ScutiYoutubeCash.getInstance().getSQL().open();
            }
            final PreparedStatement prepareStatement = ScutiYoutubeCash.getInstance().getSQL().getConnection().prepareStatement("SELECT * FROM `youtubevideos` WHERE `video` = ?");
            prepareStatement.setString(1, s);
            if (prepareStatement.executeQuery().next()) {
                return true;
            }
        }
        catch (SQLException ex) {}
        return false;
    }
    
    public ScutiYoutubeCash getPlugin() {
        return this.plugin;
    }
    
    public void setPlugin(final ScutiYoutubeCash plugin) {
        this.plugin = plugin;
    }
}
