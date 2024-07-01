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
         ChatUtil.sendMessage(sender, "&cJenom hráči mohou použít tento příkaz.");
      } else {
         Player player = (Player)sender;
         String cfgName = this.instance.getConfig().getString("Owner", "OwnerCZ");
         String name = player.getName();
         if (!player.hasPermission("servercore.manage") && !player.isOp() && !cfgName.equals(name)) {
            ChatUtil.sendMessage(player, "&9PlayerServerCore> &cNa toto nemáš oprávnění.");
         } else {
             switch (label) {
                 case "psc" -> {
                     if (args.length == 0) {
                         ChatUtil.sendMessage(player, "&9PlayerServerCore> &7Pro otevření menu napiš &e/menu&7.");
                     } else {
                         String var8 = args[0];
                         if (var8.equals("menu")) {
                             MainGui.openGui(player);
/*                             case "config" -> {
                                 ConfigGui.openGui(player, this.instance.getServer().getWorldContainer());
                             }*/
                         }
                     }
                 }
                 case "menu" -> {
                     MainGui.openGui(player);
                 }
                 /*case "config" -> {
                     ConfigGui.openGui(player, this.instance.getServer().getWorldContainer());
                 }*/
                 default -> {}
             }
         }
      }
       return true;
   }
}
