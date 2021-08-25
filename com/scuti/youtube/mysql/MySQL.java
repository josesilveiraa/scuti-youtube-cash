package com.scuti.youtube.mysql;

import com.scuti.youtube.*;
import java.sql.*;
import org.bukkit.configuration.*;

public class MySQL
{
    private ScutiYoutubeCash plugin;
    private Connection con;
    
    public MySQL(final ScutiYoutubeCash plugin) {
        this.setPlugin(plugin);
    }
    
    public void open() {
        final String string = "jdbc:mysql://" + ((MemorySection)this.getPlugin().getConfig()).getString("MySQL.Host") + ":" + ((MemorySection)this.getPlugin().getConfig()).getString("MySQL.Porta") + "/" + ((MemorySection)this.getPlugin().getConfig()).getString("MySQL.Database");
        final String string2 = new StringBuilder().append(((MemorySection)this.getPlugin().getConfig()).getString("MySQL.Usuario")).toString();
        final String string3 = new StringBuilder().append(((MemorySection)this.getPlugin().getConfig()).getString("MySQL.Senha")).toString();
        try {
            this.setConnection(DriverManager.getConnection(string, string2, string3));
        }
        catch (SQLException ex) {}
    }
    
    protected void close() {
        if (this.getConnection() != null) {
            try {
                this.getConnection().close();
            }
            catch (SQLException ex) {}
        }
    }
    
    public void createTable() {
        if (this.getConnection() != null) {
            try {
                final PreparedStatement prepareStatement = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `youtubevideos` (`video` VARCHAR(100) NULL, `cash` VARCHAR(48) NULL, `reivindicado` VARCHAR(20) NULL);");
                prepareStatement.execute();
                final PreparedStatement prepareStatement2 = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `youtubecooldown` (`nome` VARCHAR(48) NULL, `tempo` VARCHAR(48) NULL);");
                prepareStatement2.execute();
                final PreparedStatement prepareStatement3 = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `youtubeaccounts` (`nome` VARCHAR(48) NULL, `canal` VARCHAR(48) NULL);");
                prepareStatement3.execute();
                prepareStatement.close();
                prepareStatement2.close();
                prepareStatement3.close();
            }
            catch (SQLException ex) {
                System.out.println("[ScutiYoutubeCash] erro ao tentar criar tabela no MySQL.");
            }
        }
    }
    
    public ScutiYoutubeCash getPlugin() {
        return this.plugin;
    }
    
    public void setPlugin(final ScutiYoutubeCash plugin) {
        this.plugin = plugin;
    }
    
    public Connection getConnection() {
        return this.con;
    }
    
    public void setConnection(final Connection con) {
        this.con = con;
    }
}
