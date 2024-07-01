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
      String owner = this.configuration.getString("Owner", "OwnerCZ");
      if (player.getName().equals(owner) || player.getUniqueId().toString().equals(owner)) {
         player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9PlayerServers> &7Vítejte na svém serveru, " + player.getDisplayName() + "!"));
         player.sendMessage(
                 ChatColor.translateAlternateColorCodes('&', "&9PlayerServers> &7Pomocí příkazu &e/menu &7můžeš upravovat nastavení serveru.")
         );
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
                  player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&9PlayerServers> &7Server je plný!"));
               }
            }
         }
      }
   }
}
