package cn.chengzhiya.mhdfshout;

import cn.chengzhiya.mhdfshout.Commands.AdminShout;
import cn.chengzhiya.mhdfshout.Commands.ShoutReload;
import cn.chengzhiya.mhdfshout.Listeners.*;
import cn.chengzhiya.mhdfshout.Tasks.ShoutDelayTime;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static cn.chengzhiya.mhdfpluginapi.Util.*;
import static cn.chengzhiya.mhdfpluginapi.YamlFileUtil.*;
import static cn.chengzhiya.mhdfshout.Util.*;

public final class main extends JavaPlugin {
    public static main main;

    @Override
    public void onEnable() {
        // Plugin startup logic
        main = this;

        File PluginHome = getDataFolder();
        File ConfigFile = new File(PluginHome, "config.yml");
        File LangFile = new File(PluginHome, "lang.yml");

        if (!PluginHome.exists()) {
            boolean Stats = PluginHome.mkdirs();
            if (!Stats) {
                ColorLog("&c[MHDF-PluginAPI]插件数据文件夹创建失败!");
            }
        }
        if (!ConfigFile.exists()) {
            SaveResource(this.getDataFolder().getPath(), "config.yml", "config.yml", true);
        }
        if (!LangFile.exists()) {
            SaveResource(this.getDataFolder().getPath(), "lang.yml", "lang.yml", true);
        }

        if (getConfig().getBoolean("BungeeCordMode")) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCordHook());
        } else {
            new ShoutDelayTime().runTaskTimerAsynchronously(this, 0L, 20L);
        }

        Bukkit.getPluginManager().registerEvents(new UseShout(), this);
        Bukkit.getPluginManager().registerEvents(new SendShoutMessage(), this);
        Bukkit.getPluginManager().registerEvents(new AntiChangeItem(), this);
        Bukkit.getPluginManager().registerEvents(new QuitCancel(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);

        for (String Commands : getConfig().getStringList("HornSettings.AdminShout.Commands")) {
            registerCommand(this, new AdminShout(), "管理喊话", Commands);
        }

        registerCommand(this, new ShoutReload(), "重载配置", "shoutreload");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        main = null;


    }
}
