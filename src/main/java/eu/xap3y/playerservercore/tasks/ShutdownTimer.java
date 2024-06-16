package eu.xap3y.playerservercore.tasks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class ShutdownTimer extends BukkitRunnable implements Listener {
    private long lastLeave;
    private final Configuration configuration;

    public ShutdownTimer(Configuration configuration) {
        this.configuration = configuration;
        this.lastLeave = System.currentTimeMillis();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        System.out.println(Bukkit.getOnlinePlayers().size());
        if (Bukkit.getOnlinePlayers().size() == 1) {
            this.lastLeave = System.currentTimeMillis();
        }
    }

    public void run() {
        if (System.currentTimeMillis() - this.lastLeave >= TimeUnit.MINUTES.toMillis(this.configuration.getInt("LastJoin"))
                && Bukkit.getOnlinePlayers().isEmpty()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
        }
    }
}