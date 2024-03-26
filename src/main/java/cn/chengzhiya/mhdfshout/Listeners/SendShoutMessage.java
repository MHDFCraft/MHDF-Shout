package cn.chengzhiya.mhdfshout.Listeners;

import cn.chengzhiya.mhdfpluginapi.Util;
import cn.chengzhiya.mhdfshout.main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

import static cn.chengzhiya.mhdfshout.Util.*;

public final class SendShoutMessage implements Listener {
    @EventHandler
    public void OnEvent(AsyncPlayerChatEvent event) {
        if (getShoutHashMap().get(event.getPlayer().getName()) != null) {
            event.setCancelled(true);
            String MythicMobsID = getShoutHashMap().get(event.getPlayer().getName());
            String Message = ChatColor.stripColor(event.getMessage());
            Player player = event.getPlayer();
            if (!Message.contains(Objects.requireNonNull(main.main.getConfig().getString("InputSettings.ExitMessage")))) {
                if (main.main.getConfig().getInt("HornSettings." + MythicMobsID + ".MaxLength") != -1 && !event.getPlayer().hasPermission("MHDFShout.Bypass.Length")) {
                    if (Message.length() > main.main.getConfig().getInt("HornSettings." + MythicMobsID + ".MaxLength")) {
                        player.sendMessage(i18n("OutLength").replaceAll("\\{Length\\}", String.valueOf(main.main.getConfig().getInt("HornSettings." + MythicMobsID + ".MaxLength"))));
                        getShoutHashMap().remove(player.getName());
                        return;
                    }
                }
                if (!event.getPlayer().hasPermission("MHDFShout.Bypass.BlackWord")) {
                    for (String BlackWord : main.main.getConfig().getStringList("ShoutSettings.BlackWordList")) {
                        if (Message.contains(BlackWord)) {
                            player.sendMessage(i18n("BlackWord"));
                            getShoutHashMap().remove(player.getName());
                            return;
                        }
                    }
                }
                if (main.main.getConfig().getBoolean("HornSettings." + MythicMobsID + ".TakeItem")) {
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                }
                if (main.main.getConfig().getBoolean("HornSettings." + MythicMobsID + ".Color")) {
                    Message = Util.ChatColor(Message);
                }
                if (ShoutList.isEmpty()) {
                    player.sendMessage(i18n("Done"));
                }else {
                    player.sendMessage(i18n("DoneInQueue").replaceAll("\\{Size\\}", String.valueOf(ShoutList.size())));
                }
                if (!MythicMobsID.equals("AdminShout")) {
                    SendShout(player,
                            main.main.getConfig().getString("HornSettings." + MythicMobsID + ".BossBarColor"),
                            Util.ChatColor(PlaceholderAPI.setPlaceholders(player, Objects.requireNonNull(main.main.getConfig().getString("HornSettings." + MythicMobsID + ".NullBossBarMessage")))),
                            Util.ChatColor(PlaceholderAPI.setPlaceholders(player, Objects.requireNonNull(main.main.getConfig().getString("HornSettings." + MythicMobsID + ".MessageFormat")))).replaceAll("\\{Message\\}", Message),
                            main.main.getConfig().getString("HornSettings." + MythicMobsID + ".Sound"),
                            main.main.getConfig().getInt("HornSettings." + MythicMobsID + ".ShowTime")
                    );
                } else {
                    SendAdminShout(player,
                            main.main.getConfig().getString("HornSettings." + MythicMobsID + ".BossBarColor"),
                            Util.ChatColor(PlaceholderAPI.setPlaceholders(player, Objects.requireNonNull(main.main.getConfig().getString("HornSettings." + MythicMobsID + ".NullBossBarMessage")))),
                            Util.ChatColor(PlaceholderAPI.setPlaceholders(player, Objects.requireNonNull(main.main.getConfig().getString("HornSettings." + MythicMobsID + ".MessageFormat")))).replaceAll("\\{Message\\}", Message),
                            main.main.getConfig().getString("HornSettings." + MythicMobsID + ".Sound"),
                            main.main.getConfig().getInt("HornSettings." + MythicMobsID + ".ShowTime")
                    );
                }
            } else {
                player.sendMessage(i18n("Exit"));
            }
            getShoutHashMap().remove(player.getName());
        }
    }
}
