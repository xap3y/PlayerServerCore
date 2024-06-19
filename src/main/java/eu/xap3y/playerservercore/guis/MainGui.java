package eu.xap3y.playerservercore.guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.utils.ItemUtils;
import eu.xap3y.playerservercore.listeners.MaxPlayersListener;
import eu.xap3y.playerservercore.listeners.MotdListener;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.BanList.Type;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.motd;

public class MainGui extends Gui {
   public static void openGui(Player player) {
      XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
      int rows = 6;
      SGMenu menu = PlayerServerCore.getSpiGUI().create("Hlavní Menu", rows);
      menu.setAutomaticPaginationEnabled(false);
      ItemUtils.addBorder(menu, rows);

      String verze;
       try {
           verze = Bukkit.getServer().getVersion().split(" ")[0].split("-")[0];
       } catch (Exception e) {
           verze = Bukkit.getServer().getVersion().split(" ")[0];
       }
      menu.setButton(
         4,
         new SGButton(
            new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
               .name("&a&lInfo o serveru")
               .lore(
                       " ",
                      "&8&l|&r &7Jméno serveru: &f" + PlayerServerCore.getInstance().getConfig().getString("ServerUUID", "&cN/A"),
                      "&8&l|&r &7Verze serveru: &f" + verze,
                      /*"&8&l|&r &7Server version: &f" + Bukkit.getServer().getClass().getPackage().getName().replaceAll("org.bukkit.craftbukkit", ""),*/
                      "&8&l|&r &7Hráči: &f" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers(),
                      "&8&l|&r &7Whitelist: &f" + (Bukkit.hasWhitelist() ? "&azapnutu" : "&cvypnuto"),
                      String.format(
                         "&8&l|&r &7Bany: &f%d zabanovaných hráčů",
                         Bukkit.getBanList(Type.IP).getBanEntries().size() + Bukkit.getBanList(Type.NAME).getBanEntries().size()
                      ),
                      "&8&l|&r &7Majitel serveru: &f" + PlayerServerCore.getInstance().getConfig().getString("Owner", "&cN/A")
               )
               .skullOwner(player.getName())
               .build()
         )
      );
      /*menu.setButton(
         19,
         new SGButton(
               new ItemBuilder(XMaterial.STICKY_PISTON.parseItem()).name("&c&lPlugins Management").lore("&7Click to open the plugins management GUI.").build()
            )
            .withListener(listener -> PluginInstallerGui.openGui(player))
      );*/

       List<String> loreForBans = new ArrayList<>();
       loreForBans.add("&7Klikni pro správu banů.");
       if (!player.hasPermission("servercore.management.bans") && !player.isOp()) {
           loreForBans.add("");
           loreForBans.add("&cNa toto nemáš práva.");
       }

      menu.setButton(
         20,
         new SGButton(new ItemBuilder(XMaterial.GOLDEN_AXE.parseItem()).name("&e&lSpráva banů").lore(loreForBans).build())
            .withListener(listener -> {
                if (player.hasPermission("servercore.management.bans") || player.isOp())
                    BansManagementGui.openGui(player);
                else {
                    assert XSound.BLOCK_ANVIL_FALL.parseSound() != null;
                    player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_FALL.parseSound(), 1f, 1f);
                }
            })
      );

      List<String> loreForPlayers = new ArrayList<>();
       loreForPlayers.add("&7Klikni pro správu hráčů.");
      if (!player.hasPermission("servercore.management.players") && !player.isOp()) {
          loreForPlayers.add("");
          loreForPlayers.add("&cNa toto nemáš práva.");
      }

      menu.setButton(
         24,
         new SGButton(
               new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).name("&a&lSpráva hráčů").lore(loreForPlayers).build()
            )
            .withListener(listener -> {
                if (player.hasPermission("servercore.management.players") || player.isOp())
                    PlayerManagementGui.openGui(player);
                else {
                    assert XSound.BLOCK_ANVIL_FALL.parseSound() != null;
                    player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_FALL.parseSound(), 1f, 1f);
                }
            })
      );
      menu.setButton(
         29,
         new SGButton(
               new ItemBuilder(XMaterial.GRASS_BLOCK.parseItem())
                       .name("&b&lMožnosti světa")
                       .lore(
                         "&7Klikni pro správu světa.",
                         "",
                         "&7Jméno světa: &f" + player.getWorld().getName()
                      )
                       .flag(ItemFlag.HIDE_ATTRIBUTES)
                    .build()
            )
            .withListener(listener -> WorldOptionsGui.openGui(player))
      );


       List<String> loreForWhitelist = new ArrayList<>();
       loreForWhitelist.add("&7Klikni pro správu whitelistu");
       loreForWhitelist.add("&7Whitelist je nyní: &f" + (Bukkit.hasWhitelist() ? "&azapnutý" : "&cvypnutý"));
       if (!player.hasPermission("servercore.management.whitelist") && !player.isOp()) {
           loreForWhitelist.add("");
           loreForWhitelist.add("&cNa toto nemáš práva.");
       }

      menu.setButton(
         31,
         new SGButton(
               new ItemBuilder(Bukkit.hasWhitelist() ? XMaterial.IRON_DOOR.parseItem() : XMaterial.OAK_DOOR.parseItem())
                  .name("&a&lSpráva whitelistu")
                  .lore(loreForWhitelist)
                  .build()
            )
            .withListener(event -> {
                if (player.hasPermission("servercore.management.whitelist") || player.isOp())
                    WhitelistGui.openGui(player);
                else {
                    assert XSound.BLOCK_ANVIL_FALL.parseSound() != null;
                    player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_FALL.parseSound(), 1f, 1f);
                }
            })
      );

       List<String> loreForMaxPlayers;
         loreForMaxPlayers = new ArrayList<>();
       loreForMaxPlayers.add("&7Klikni pro nastavení maximálního počtu hráčů");
       loreForMaxPlayers.add("&7Maximálně hráčů nyní: &f" + Bukkit.getMaxPlayers());
       if (!player.hasPermission("servercore.management.maxplayers") && !player.isOp()) {
           loreForMaxPlayers.add("");
              loreForMaxPlayers.add("&cNa toto nemáš práva.");
       }

      menu.setButton(
         33,
         new SGButton(
               new ItemBuilder(XMaterial.ARMOR_STAND.parseItem())
                  .name("&a&lMaximální počet hráčů")
                  .lore(loreForMaxPlayers)
                  .build()
            )
            .withListener(new MaxPlayersListener())
      );
      menu.setButton(
         22,
         new SGButton(
               new ItemBuilder(XMaterial.PAPER.parseItem())
                  .name("&d&lNastavit MOTD")
                  .lore("&7Klikni pro úpravu MOTD.", "&7Momentální MOTD: &f" + LegacyComponentSerializer.legacyAmpersand().serialize(motd()))
                  .build()
            )
            .withListener(new MotdListener())
      );
      /*menu.setButton(
         34,
         new SGButton(new ItemBuilder(XMaterial.SLIME_BLOCK.parseItem()).name("&b&lManage Game Rules").lore("&7Click here to manage the game rules.").build())
            .withListener(event -> GameRuleGui.openGui(player))
      );*/

       menu.setButton(49, new SGButton(new ItemBuilder(XMaterial.BARRIER.parseItem()).name("&c&lZavřít").build()).withListener(
                event -> {
                     XSound.BLOCK_COPPER_DOOR_CLOSE.play(player);
                     player.closeInventory();
                }
       ));
      player.openInventory(menu.getInventory());
   }
}
