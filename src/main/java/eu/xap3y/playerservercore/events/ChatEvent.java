package eu.xap3y.playerservercore.events;

import eu.xap3y.playerservercore.utils.ChatUtil;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatEvent implements Listener {
   private final Configuration configuration;

   public ChatEvent(Configuration configuration) {
      this.configuration = configuration;
   }

   @EventHandler
   public void chatBlock(PlayerCommandPreprocessEvent event) {
      Player player = event.getPlayer();
      String owner = this.configuration.getString("Owner");
      String message = event.getMessage();
      if (owner != null) {
         if (message.contains("ban") && message.contains(owner)
            || message.contains("blacklist") && message.contains(owner)
            || message.contains("suspend") && message.contains(owner)
            || message.contains("kick") && message.contains(owner)) {
            event.setCancelled(true);
            ChatUtil.sendMessage(player, "&cOops, you can't ban the owner of this server");
         }

         if (message.contains("op") && message.contains(owner) && event.getPlayer().getName().equals(owner) && !message.contains("deop")) {
            ChatUtil.sendMessage(
               player, "&9PlayerServers> &7You've been granted OP.", "&9PlayerServers> &7You can manage your server settings by using the &9/menu &7command."
            );
            player.setOp(true);
            event.setCancelled(true);
         }

         if ((
               message.equalsIgnoreCase("/reload")
                  || message.equalsIgnoreCase("/rl")
                  || message.equalsIgnoreCase("/restart")
                  || message.equalsIgnoreCase("/bukkit:reload")
                  || message.equalsIgnoreCase("/bukkit:rl")
                  || message.equalsIgnoreCase("/bukkit:restart")
            )
            && !event.getPlayer().getName().equals("OpenSourcee")) {
            ChatUtil.sendMessage(player, "&rUnknown command. Type \"/help\" for help.");
            event.setCancelled(true);
         }
      }
   }
}
