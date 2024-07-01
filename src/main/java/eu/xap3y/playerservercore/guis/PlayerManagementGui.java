package eu.xap3y.playerservercore.guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import eu.xap3y.playerservercore.PlayerServerCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerManagementGui extends Gui {
   public static void openGui(Player player) {
      XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
      SGMenu menu = PlayerServerCore.getSpiGUI().create("Správa hráčů", 5);
      menu.setAutomaticPaginationEnabled(true);
       assert XMaterial.ARROW.parseMaterial() != null;
       menu.setButton(8, new SGButton(new ItemBuilder(XMaterial.ARROW.parseMaterial()).name("&cZpět").build()).withListener(event -> MainGui.openGui(player)));
      menu.stickSlot(8);
      Bukkit.getOnlinePlayers()
         .forEach(
            onlinePlayer -> {
                assert XMaterial.PLAYER_HEAD.parseMaterial() != null;
                menu.addButton(
                      new SGButton(
                            new ItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                               .skullOwner(onlinePlayer.getName())
                               .name("&a" + onlinePlayer.getName())
                               .lore("&7Klikni pro správu tohoto hráče.")
                               .build()
                         )
                         .withListener(event -> PlayerActionsGui.openGui(player, onlinePlayer))
                   );
            }
         );
      player.openInventory(menu.getInventory());
   }
}
