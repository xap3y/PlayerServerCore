package eu.xap3y.playerservercore.events;

import net.kyori.adventure.text.Component;

import static org.bukkit.Bukkit.motd;

public class MotdHandler {

   /*@EventHandler
   public void onServerListPing(ServerListPingEvent event) {
      event.setMotd(ChatUtil.translate(motd));
   }*/

   public static void setMotd(String motdToSet) {
      /*MotdHandler.motd = motd;*/
      motd(Component.text(motdToSet.replace("&", "§")));
   }
}
