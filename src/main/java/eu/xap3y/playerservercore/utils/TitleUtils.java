package eu.xap3y.playerservercore.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.tozymc.spigot.api.title.TitleApi;

public class TitleUtils {
   public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
      if (version.contains("v1_8_")) {
         TitleApi.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
      } else {
         player.sendTitle(ChatUtil.translate(title), ChatUtil.translate(subtitle), fadeIn, stay, fadeOut);
      }
   }
}
