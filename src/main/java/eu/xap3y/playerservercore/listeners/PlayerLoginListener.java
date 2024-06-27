package eu.xap3y.playerservercore.listeners;

import eu.xap3y.playerservercore.PlayerServerCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    @EventHandler
    void onPlayerJoinEvent(PlayerLoginEvent e) {
        Player playerToLogin = e.getPlayer();
        boolean serverWhitelist = Bukkit.getServer().hasWhitelist();
        if (serverWhitelist && !playerToLogin.isWhitelisted()) {
            String owner = PlayerServerCore.getInstance().getConfig().getString("Owner", "OwnerCZ");
            if (playerToLogin.getName().equals(owner)) {
                //Bukkit.getServer().getWhitelistedPlayers().add(playerToLogin);
                playerToLogin.setWhitelisted(true);
                e.allow();
                e.setResult(PlayerLoginEvent.Result.ALLOWED);
            }
        }
    }
}
