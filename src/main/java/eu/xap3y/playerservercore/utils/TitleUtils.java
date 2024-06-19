package eu.xap3y.playerservercore.utils;

import org.bukkit.entity.Player;

public class TitleUtils {
   public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      player.sendTitle(ChatUtil.translate(title), ChatUtil.translate(subtitle), fadeIn, stay, fadeOut);
   }
}
