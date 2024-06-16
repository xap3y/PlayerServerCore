package eu.xap3y.playerservercore.events;

import eu.xap3y.playerservercore.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdHandler implements Listener {
   private static String motd = Bukkit.getMotd();

   @EventHandler
   public void onServerListPing(ServerListPingEvent event) {
      event.setMotd(ChatUtil.translate(motd));
   }

   public static void setMotd(String motd) {
      MotdHandler.motd = motd;
   }

   public static String getMotd() {
      return motd;
   }
}
