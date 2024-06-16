package eu.xap3y.playerservercore.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
   private final Configuration configuration;

   public JoinEvent(Configuration configuration) {
      this.configuration = configuration;
   }

   @EventHandler
   public void opOnJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      String owner = this.configuration.getString("Owner");
      if (player.getName().equals(owner) || player.getUniqueId().toString().equals(owner)) {
         if (!player.isOp()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9PlayerServers> &7Welcome to your server, " + player.getDisplayName() + "!"));
            player.sendMessage(
               ChatColor.translateAlternateColorCodes('&', "&9PlayerServers> &7You can manage your server settings by using the &9/menu &7command.")
            );
            player.setOp(true);
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void maxPlayersJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      String owner = this.configuration.getString("Owner");
      int online = Bukkit.getOnlinePlayers().size();
      if (player.getName().equals(owner) || player.getUniqueId().toString().equals(owner)) {
         if (!player.isOp()) {
            if (!player.hasPermission("playerserver.bypass.maxplayers")) {
               if (online >= Bukkit.getMaxPlayers() || online >= this.configuration.getInt("MaxPlayers")) {
                  player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&9PlayerServers> &7The server is full!"));
               }
            }
         }
      }
   }
}
