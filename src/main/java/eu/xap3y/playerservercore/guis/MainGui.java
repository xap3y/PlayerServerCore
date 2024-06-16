package eu.xap3y.playerservercore.guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.events.MotdHandler;
import eu.xap3y.playerservercore.listeners.ItemUtils;
import eu.xap3y.playerservercore.listeners.MaxPlayersListener;
import eu.xap3y.playerservercore.listeners.MotdListener;
import org.bukkit.Bukkit;
import org.bukkit.BanList.Type;
import org.bukkit.entity.Player;

public class MainGui extends Gui {
   public static void openGui(Player player) {
      XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
      int rows = 6;
      SGMenu menu = PlayerServerCore.getSpiGUI().create("Main Menu", rows);
      menu.setAutomaticPaginationEnabled(false);
      ItemUtils.addBorder(menu, rows);
      menu.setButton(
         4,
         new SGButton(
            new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
               .name("&a&lServer info")
               .lore(
                  "&7Server name: &f" + PlayerServerCore.getInstance().getConfig().getString("ServerUUID"),
                  "&7Server type: &f" + Bukkit.getServer().getVersion().split(" ")[0],
                  "&7Server version: &f" + Bukkit.getServer().getClass().getPackage().getName().replaceAll("org.bukkit.craftbukkit", ""),
                  "&7Online: &f" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + " players",
                  "&7Whitelist: &f" + Bukkit.hasWhitelist(),
                  String.format(
                     "&7Banlist: &f%d banned, %d IP banned",
                     Bukkit.getBanList(Type.NAME).getBanEntries().size(),
                     Bukkit.getBanList(Type.IP).getBanEntries().size()
                  ),
                  "&7Server owner: &f" + PlayerServerCore.getInstance().getConfig().getString("Owner")
               )
               .skullOwner(player.getName())
               .build()
         )
      );
      menu.setButton(
         19,
         new SGButton(
               new ItemBuilder(XMaterial.STICKY_PISTON.parseItem()).name("&c&lPlugins Management").lore("&7Click to open the plugins management GUI.").build()
            )
            .withListener(listener -> PluginInstallerGui.openGui(player))
      );
      menu.setButton(
         21,
         new SGButton(new ItemBuilder(XMaterial.GOLDEN_AXE.parseItem()).name("&e&lBans Management").lore("&7Click to open the bans management GUI.").build())
            .withListener(listener -> BansManagementGui.openGui(player))
      );
      menu.setButton(
         23,
         new SGButton(
               new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).name("&a&lPlayer Management").lore("&7Click to open the player management GUI.").build()
            )
            .withListener(listener -> PlayerManagementGui.openGui(player))
      );
      menu.setButton(
         25,
         new SGButton(
               new ItemBuilder(XMaterial.DIAMOND_SWORD.parseItem())
                  .name("&b&lWorld options")
                  .lore(
                     "&7Click to open the world options GUI.",
                     "&7These options only apply to the current world.",
                     "",
                     "&7World name: &f" + player.getWorld().getName()
                  )
                  .build()
            )
            .withListener(listener -> WorldOptionsGui.openGui(player))
      );
      menu.setButton(
         28,
         new SGButton(
               new ItemBuilder(Bukkit.hasWhitelist() ? XMaterial.IRON_DOOR.parseItem() : XMaterial.OAK_DOOR.parseItem())
                  .name("&a&lWhitelist Management")
                  .lore("&7Click here to manage whitelist.", "&7Whitelist is now: &f" + Bukkit.hasWhitelist())
                  .build()
            )
            .withListener(event -> WhitelistGui.openGui(player))
      );
      menu.setButton(
         30,
         new SGButton(
               new ItemBuilder(XMaterial.ARMOR_STAND.parseItem())
                  .name("&a&lSet max players")
                  .lore("&7Click here to set the max players.", "&7Current max players: &f" + Bukkit.getMaxPlayers())
                  .build()
            )
            .withListener(new MaxPlayersListener())
      );
      menu.setButton(
         32,
         new SGButton(
               new ItemBuilder(XMaterial.PAPER.parseItem())
                  .name("&d&lSet MOTD")
                  .lore("&7Click here to set the MOTD.", "&7Current MOTD: &f" + MotdHandler.getMotd())
                  .build()
            )
            .withListener(new MotdListener())
      );
      menu.setButton(
         34,
         new SGButton(new ItemBuilder(XMaterial.SLIME_BLOCK.parseItem()).name("&b&lManage Game Rules").lore("&7Click here to manage the game rules.").build())
            .withListener(event -> GameRuleGui.openGui(player))
      );
      player.openInventory(menu.getInventory());
   }
}
