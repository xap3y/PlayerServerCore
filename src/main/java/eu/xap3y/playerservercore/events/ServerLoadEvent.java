package eu.xap3y.playerservercore.events;

import eu.xap3y.playerservercore.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class ServerLoadEvent implements Listener {
   private final Configuration config;

   public ServerLoadEvent(Configuration config) {
      this.config = config;
   }

   @EventHandler
   public void onServerLoad(PluginEnableEvent event) throws ReflectiveOperationException {
      int maxPlayers = this.config.getInt("MaxPlayers");
      if (Bukkit.getMaxPlayers() > maxPlayers) {
         ServerUtils.changeSlots(maxPlayers, true);
      }
   }
}
