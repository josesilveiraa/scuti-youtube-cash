package com.scuti.youtube;

import org.bukkit.plugin.java.*;
import com.scuti.youtube.mysql.*;
import com.scuti.youtube.commands.*;
import java.nio.file.*;
import com.google.common.base.*;
import com.google.common.io.*;
import org.bukkit.configuration.*;
import java.io.*;

public class ScutiYoutubeCash extends JavaPlugin
{
    private static ScutiYoutubeCash instance;
    private MySQL SQL;
    private ManagerVideo managerVideo;
    private ManagerAccounts managerAccounts;
    
    public void onEnable() {
        setInstance(this);
        this.saveCustomConfig();
        this.setSQL(new MySQL(this));
        this.getSQL().open();
        this.getSQL().createTable();
        this.setManagerVideo(new ManagerVideo(this));
        this.setManagerAccounts(new ManagerAccounts(this));
        new RequestCashCommand(this);
    }
    
    public void saveCustomConfig() {
        final File file = Paths.get(this.getDataFolder().getAbsolutePath(), "config.yml").toFile();
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        if (file.exists()) {
            return;
        }
        final ByteSource byteSource = new ByteSource() {
            public InputStream openStream() throws IOException {
                return ScutiYoutubeCash.this.getResource("config.yml");
            }
        };
        try {
            final String read = byteSource.asCharSource(Charsets.UTF_8).read();
            this.getConfig().loadFromString(read);
            Files.write((CharSequence)read, file, Charsets.UTF_8);
        }
        catch (IOException | InvalidConfigurationException ex) {
            System.out.println("[ScutiYoutubeCash] Erro ao escrever 'config.yml' usando a formata\u00e7\u00e3o UTF-8.");
        }
    }
    
    public static ScutiYoutubeCash getInstance() {
        return ScutiYoutubeCash.instance;
    }
    
    public static void setInstance(final ScutiYoutubeCash instance) {
        ScutiYoutubeCash.instance = instance;
    }
    
    public MySQL getSQL() {
        return this.SQL;
    }
    
    public void setSQL(final MySQL sql) {
        this.SQL = sql;
    }
    
    public ManagerVideo getManagerVideo() {
        return this.managerVideo;
    }
    
    public void setManagerVideo(final ManagerVideo managerVideo) {
        this.managerVideo = managerVideo;
    }
    
    public ManagerAccounts getManagerAccounts() {
        return this.managerAccounts;
    }
    
    public void setManagerAccounts(final ManagerAccounts managerAccounts) {
        this.managerAccounts = managerAccounts;
    }
}
