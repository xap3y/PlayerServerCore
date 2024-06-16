package eu.xap3y.playerservercore.listeners;

import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.buttons.SGButtonListener;
import java.util.Arrays;

import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.guis.PluginInstallerGui;
import eu.xap3y.playerservercore.models.PluginModel;
import eu.xap3y.playerservercore.utils.ChatUtil;
import eu.xap3y.playerservercore.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class PluginClickListener implements SGButtonListener {
   private final PluginModel plugin;

   public PluginClickListener(PluginModel plugin) {
      this.plugin = plugin;
   }

   @Override
   public void onClick(InventoryClickEvent event) {
      Player player = (Player)event.getWhoClicked();
      if (this.plugin.isEnabled()) {
         Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(this.plugin.getName()));
         if (!this.plugin.getFile().delete()) {
            player.sendMessage(ChatUtil.translate("&9Error> &7Could not delete the plugin file."));
            return;
         }

         ChatUtil.sendMessage(
            player, "&9PlayerServers> &7Plugin &a" + this.plugin.getName() + "&7 disabled. Please restart the server to completely remove it."
         );
         PluginInstallerGui.openGui(player);
      } else {
         if (this.plugin.isFromRepository()) {
            FileUtils.copyFromRepository(this.plugin.getFile());
         }

         long enabledPlugins = Arrays.stream(PlayerServerCore.getInstance().getServer().getPluginManager().getPlugins()).filter(Plugin::isEnabled).count();
         if (enabledPlugins > (long)PlayerServerCore.getInstance().getConfig().getInt("MaxPlugins")) {
            ChatUtil.sendMessage(player, "&9PlayerServers> &7You have reached limit for max plugins!");
            return;
         }

         Exception ex = FileUtils.loadPlugin(this.plugin.getFile());
         if (ex != null) {
            ChatUtil.sendMessage(player, "&9Error> &7Failed to load plugin: &c" + ex.getMessage());
            return;
         }

         ChatUtil.sendMessage(player, "&9PlayerServers> &7Plugin loaded successfully. Some plugins may require a restart to work properly.");
         XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
         PluginInstallerGui.openGui(player);
      }
   }
}
