package eu.xap3y.playerservercore.guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;

import java.util.HashSet;
import java.util.Set;

import eu.xap3y.playerservercore.PlayerServerCore;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.BanList.Type;
import org.bukkit.entity.Player;

public class BansManagementGui extends Gui {
   public static void openGui(Player player) {
      XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
      SGMenu menu = PlayerServerCore.getSpiGUI().create("Bans Management", 5);
      menu.setAutomaticPaginationEnabled(true);
      menu.setButton(8, new SGButton(new ItemBuilder(XMaterial.ARROW.parseMaterial()).name("&cGo back").build()).withListener(event -> MainGui.openGui(player)));
      menu.stickSlot(8);

      Set<BanEntry> nameBans = PlayerServerCore.getInstance().getServer().getBanList(Type.NAME).getBanEntries();
      Set<BanEntry> ipBans = PlayerServerCore.getInstance().getServer().getBanList(Type.IP).getBanEntries();
        Set<BanEntry> bannedPlayers = new HashSet<>(nameBans);
        bannedPlayers.addAll(ipBans);

      for (BanEntry banEntry : bannedPlayers) {
         menu.addButton(
            new SGButton(
                  new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                     .name("&a&l" + banEntry.getTarget())
                     .lore(
                        "&7Reason: &f" + banEntry.getReason(),
                        "&7Expires: &f" + (banEntry.getExpiration() == null ? "Never" : banEntry.getExpiration()),
                        "&7Source: &f" + banEntry.getSource(),
                        "",
                        "&cClick to unban"
                     )
                     .skullOwner(banEntry.getTarget())
                     .build()
               )
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  Bukkit.getBanList(Type.NAME).pardon(banEntry.getTarget());
                  Bukkit.getBanList(Type.IP).pardon(banEntry.getTarget());
                  openGui(player);
               })
         );
      }

      player.openInventory(menu.getInventory());
   }
}
