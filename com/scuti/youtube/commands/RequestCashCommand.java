package com.scuti.youtube.commands;

import com.scuti.youtube.*;
import java.lang.reflect.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import com.scuti.youtube.utils.*;
import org.bukkit.configuration.*;

public class RequestCashCommand extends Command
{
    private ScutiYoutubeCash plugin;
    
    public RequestCashCommand(final ScutiYoutubeCash plugin) {
        super("yt");
        this.setPlugin(plugin);
        this.register();
    }
    
    private void register() {
        try {
            final Field declaredField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            declaredField.setAccessible(true);
            ((CommandMap)declaredField.get(Bukkit.getServer())).register("scutiyoutubecash", (Command)this);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean execute(final CommandSender commandSender, final String s, final String[] array) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        final Player player = (Player)commandSender;
        if (!player.hasPermission("stonerequestcash.use")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((MemorySection)this.getPlugin().getConfig()).getString("Mensagens.Sem_Permissao")));
            return true;
        }
        if (array.length < 2) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUtilize /yt video <link do video>."));
            if (player.hasPermission("stonerequestcash.admin")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUtilize /yt cadastro <adicionar/remover> <player> <nome do canal>."));
            }
            return true;
        }
        if (array[0].equalsIgnoreCase("cadastro")) {
            if (!player.hasPermission("stonerequestcash.admin")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((MemorySection)this.getPlugin().getConfig()).getString("Mensagens.Sem_Permissao")));
                return true;
            }
            if (array.length < 3) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUtilize /yt cadastro <adicionar/remover> <player> <nome do canal>."));
                return true;
            }
            if (array[1].equalsIgnoreCase("remover")) {
                final String s2 = array[2];
                if (!this.getPlugin().getManagerAccounts().existePlayer(s2)) {
                    player.sendMessage("§cEste jogador n\u00e3o possui nenhum canal cadastrado em nosso sistema.");
                    return true;
                }
                this.getPlugin().getManagerAccounts().removerPlayer(s2);
                player.sendMessage("§aYAY! Cadastro de canal removido com sucesso.");
            }
            else if (array[1].equalsIgnoreCase("adicionar")) {
                if (array.length != 4) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUtilize /yt cadastro <adicionar/remover> <player> <nome do canal>."));
                    return true;
                }
                final String s3 = array[2];
                final String s4 = array[3];
                if (this.getPlugin().getManagerAccounts().existePlayer(s3)) {
                    player.sendMessage("§cEste player j\u00e1 foi cadastrado no sistema de youtubers.");
                    return true;
                }
                this.getPlugin().getManagerAccounts().addConta(s3, s4);
                player.sendMessage("§aYAY! Youtuber cadastrado com sucesso.");
            }
        }
        else if (array[0].equalsIgnoreCase("video")) {
            if (!this.getPlugin().getManagerAccounts().existePlayer(player.getName())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((MemorySection)this.getPlugin().getConfig()).getString("Mensagens.Canal_Nao_Cadastrado")));
                return true;
            }
            final String s5 = array[1];
            if (!s5.startsWith("https://www.youtube.com")) {
                player.sendMessage("§cLink do video informado \u00e9 inv\u00e1lido!");
                return true;
            }
            if (this.getPlugin().getManagerVideo().hasVideo(s5)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((MemorySection)this.getPlugin().getConfig()).getString("Mensagens.Video_Ja_Solicitado")));
                return true;
            }
            try {
                final YoutubeAPI.RespostaYoutube obterDadosVideo = YoutubeAPI.obterDadosVideo(s5);
                if (obterDadosVideo == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((MemorySection)this.getPlugin().getConfig()).getString("Mensagens.Video_Invalido")));
                    return true;
                }
                final String title = obterDadosVideo.items.get(0).snippet.title;
                if (!this.getPlugin().getManagerAccounts().getChannelName(player.getName()).startsWith(obterDadosVideo.items.get(0).snippet.channelTitle)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((MemorySection)this.getPlugin().getConfig()).getString("Mensagens.Video_Nao_Corresponde_Com_NomeCanal")));
                    return true;
                }
                if (!title.contains(((MemorySection)this.getPlugin().getConfig()).getString("RequestCash.Nome_Do_Servidor"))) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((MemorySection)this.getPlugin().getConfig()).getString("Mensagens.Video_Falta_Nome_Do_Servidor").replace("<nome>", ((MemorySection)this.getPlugin().getConfig()).getString("RequestCash.Nome_Do_Servidor"))));
                    return true;
                }
                final YoutubeAPI.Statistics statistics = obterDadosVideo.items.get(0).statistics;
                Long n = statistics.likeCount;
                if (statistics.likeCount < statistics.dislikeCount) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((MemorySection)this.getPlugin().getConfig()).getString("Mensagens.Like_Menor_Deslike")));
                    return true;
                }
                if (player.hasPermission("scutirequestcash.multiplicar") && ((MemorySection)this.getPlugin().getConfig()).getBoolean("Multiplicar.Ativo")) {
                    n *= (Long)((MemorySection)this.getPlugin().getConfig()).getInt("Multiplicar.Vezes");
                }
                if (n > ((MemorySection)this.getPlugin().getConfig()).getInt("RequestCash.MaxCash")) {
                    n = (long)((MemorySection)this.getPlugin().getConfig()).getInt("RequestCash.MaxCash");
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((MemorySection)this.getPlugin().getConfig()).getString("Mensagens.Cash_Entregue").replace("<value>", Long.toString(n))));
                player.sendTitle("§a+" + Long.toString(n), "§aCash");
                this.getPlugin().getManagerVideo().addVideo(s5, n, player.getName());
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), ((MemorySection)this.getPlugin().getConfig()).getString("RequestCash.Execute_Command").replace("<player>", player.getName()).replace("<value>", Long.toString(statistics.likeCount)));
            }
            catch (Exception ex) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ((MemorySection)this.getPlugin().getConfig()).getString("Mensagens.Video_Invalido")));
            }
        }
        return false;
    }
    
    public ScutiYoutubeCash getPlugin() {
        return this.plugin;
    }
    
    public void setPlugin(final ScutiYoutubeCash plugin) {
        this.plugin = plugin;
    }
}
