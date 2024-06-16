package eu.xap3y.playerservercore.guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import java.util.Arrays;
import java.util.List;

import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.listeners.GameRuleListener;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class GameRuleGui extends Gui {
   public static void openGui(Player player) {
      XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
      SGMenu menu = PlayerServerCore.getSpiGUI().create("Main Menu", 4);
      menu.setAutomaticPaginationEnabled(false);
      World defaultWorld = (World)PlayerServerCore.getInstance().getServer().getWorlds().get(0);
      List<String> gameRules = Arrays.asList(defaultWorld.getGameRules());

      for (String gameRule : gameRules) {
         menu.setButton(
            gameRules.indexOf(gameRule),
            new SGButton(
                  new ItemBuilder(XMaterial.PAPER.parseItem())
                     .name("&a&l" + gameRule)
                     .lore("&7Value: &f" + player.getWorld().getGameRuleValue(gameRule))
                     .build()
               )
               .withListener(new GameRuleListener(gameRule))
         );
      }

      menu.setButton(
         31,
         new SGButton(new ItemBuilder(XMaterial.ARROW.parseItem()).name("&c&lBack").lore("&7Click to go back to the main menu.").build())
            .withListener(event -> {
               MainGui.openGui(player);
               XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
            })
      );
      player.openInventory(menu.getInventory());
   }
}
