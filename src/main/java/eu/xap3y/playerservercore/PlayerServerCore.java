package eu.xap3y.playerservercore;

import com.samjakob.spigui.SpiGUI;
import eu.xap3y.playerservercore.commands.CommandManager;
import eu.xap3y.playerservercore.events.*;
import eu.xap3y.playerservercore.listeners.MessageChannelListener;
import eu.xap3y.playerservercore.listeners.PlayerLoginListener;
import eu.xap3y.playerservercore.tasks.ShutdownTimer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class PlayerServerCore extends JavaPlugin {

    private static PlayerServerCore instance;
    public static SpiGUI spiGUI;

    public void onEnable() {
        instance = this;
        this.setConfiguration();
        Configuration configuration = this.getConfig();
        spiGUI = new SpiGUI(this);
        ShutdownTimer shutdownTimer = new ShutdownTimer(configuration);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageChannelListener());
        Bukkit.getPluginManager().registerEvents(new ServerLoadEvent(configuration), this);
        Bukkit.getPluginManager().registerEvents(new JoinEvent(configuration), this);
        Bukkit.getPluginManager().registerEvents(new ChatEvent(configuration), this);
        Bukkit.getPluginManager().registerEvents(new MoveHandler(), this);
        Bukkit.getPluginManager().registerEvents(shutdownTimer, this);
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), this);
        this.getCommand("psc").setExecutor(new CommandManager(this));
        this.getCommand("menu").setExecutor(new CommandManager(this));
        this.getCommand("config").setExecutor(new CommandManager(this));
        shutdownTimer.runTaskTimer(this, 0L, 20L);
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord");
    }

    public static PlayerServerCore getInstance() {
        return instance;
    }

    private void setConfiguration() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        try {
            File file = new File(this.getDataFolder(), "config.yml");
            if (!file.exists()) {
                this.saveResource("config.yml", false);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    public static SpiGUI getSpiGUI() {
        return spiGUI;
    }
}
