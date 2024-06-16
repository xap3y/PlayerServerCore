package eu.xap3y.playerservercore.commands;

import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.guis.ConfigGui;
import eu.xap3y.playerservercore.guis.MainGui;
import eu.xap3y.playerservercore.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements CommandExecutor {
   private final PlayerServerCore instance;

   public CommandManager(PlayerServerCore instance) {
      this.instance = instance;
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (!(sender instanceof Player)) {
         ChatUtil.sendMessage(sender, "&cOnly players can execute this command.");
         return true;
      } else {
         Player player = (Player)sender;
         if (!player.hasPermission("servercore.manage") && !player.isOp()) {
            ChatUtil.sendMessage(player, "&rUnknown command. Type \"/help\" for help.");
            return true;
         } else {
            switch (label) {
               case "psc":
                  if (args.length == 0) {
                     ChatUtil.sendMessage(player, "&9PlayerServerCore> &7Default command.");
                     return true;
                  } else {
                     String var8 = args[0];
                     switch (var8) {
                        case "menu":
                           MainGui.openGui(player);
                           return true;
                        case "config":
                           ConfigGui.openGui(player, this.instance.getServer().getWorldContainer());
                           return true;
                        case "plugins":
                           return true;
                        default:
                           return true;
                     }
                  }
               case "menu":
                  MainGui.openGui(player);
                  return true;
               case "config":
                  ConfigGui.openGui(player, this.instance.getServer().getWorldContainer());
                  return true;
               default:
                  return true;
            }
         }
      }
   }
}
