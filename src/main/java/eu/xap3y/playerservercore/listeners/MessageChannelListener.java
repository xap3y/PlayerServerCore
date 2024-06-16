package eu.xap3y.playerservercore.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import eu.xap3y.playerservercore.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageChannelListener implements PluginMessageListener {
   public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
      if (channel.equalsIgnoreCase("BungeeCord")) {
         ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
         String subChannel = in.readUTF();
         if (subChannel.equalsIgnoreCase("files")) {
            String token = in.readUTF();
            ComponentBuilder messageBuilder = new ComponentBuilder(ChatUtil.translate("&9PlayerServers> &7Click "))
               .append("here")
               .color(ChatColor.GREEN)
               .underlined(true)
               .event(new ClickEvent(Action.OPEN_URL, token))
               .append(" to open the file editor.")
               .reset()
               .color(ChatColor.GRAY);
            player.spigot().sendMessage(messageBuilder.create());
         }
      }
   }
}
