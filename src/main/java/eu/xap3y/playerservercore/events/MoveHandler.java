package eu.xap3y.playerservercore.events;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveHandler implements Listener {
   private static final List<Player> frozenPlayers = new ArrayList<>();

   @EventHandler
   public void onPlayerMove(PlayerMoveEvent event) {
      Player player = event.getPlayer();
      if (getFrozenPlayers().contains(player)) {
         event.setCancelled(true);
      }
   }

   public static void freezePlayer(Player player) {
      frozenPlayers.add(player);
   }

   public static void unfreezePlayer(Player player) {
      frozenPlayers.remove(player);
   }

   public static List<Player> getFrozenPlayers() {
      return frozenPlayers;
   }
}
