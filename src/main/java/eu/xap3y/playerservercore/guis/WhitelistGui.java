package eu.xap3y.playerservercore.guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.listeners.WhitelistListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WhitelistGui extends Gui {
   public static void openGui(Player player) {
      XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
      SGMenu menu = PlayerServerCore.getSpiGUI().create("Správa whitelistu", 5);
      menu.setAutomaticPaginationEnabled(true);
      boolean isEnabled = Bukkit.hasWhitelist();
      menu.setButton(
         6,
         new SGButton(
               new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).name("&a&lPřidat hráče do whitelistu").lore("&7Klikni pro přidání").build()
            )
            .withListener(new WhitelistListener())
      );
      menu.setButton(
         7,
         new SGButton(
               new ItemBuilder(isEnabled ? XMaterial.IRON_DOOR.parseMaterial() : XMaterial.OAK_DOOR.parseMaterial())
                  .name(isEnabled ? "&a&lVypnout Whitelist" : "&c&lZapnout Whitelist")
                  .build()
            )
            .withListener(event -> {
               Bukkit.setWhitelist(!isEnabled);
               openGui(player);
            })
      );
      menu.setButton(8, new SGButton(new ItemBuilder(XMaterial.ARROW.parseMaterial()).name("&cZpět").build()).withListener(event -> MainGui.openGui(player)));
      menu.stickSlot(8);
      Bukkit.getWhitelistedPlayers()
         .forEach(
            whitelistedPlayer -> menu.addButton(
                  new SGButton(
                        new ItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                           .skullOwner(whitelistedPlayer.getName())
                           .name("&a" + whitelistedPlayer.getName())
                           .lore("&7Klikni pro odebrání")
                           .build()
                     )
                     .withListener(event -> {
                        whitelistedPlayer.setWhitelisted(false);
                        openGui(player);
                     })
               )
         );
      player.openInventory(menu.getInventory());
   }
}
