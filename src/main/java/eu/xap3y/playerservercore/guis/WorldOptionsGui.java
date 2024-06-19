package eu.xap3y.playerservercore.guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import eu.xap3y.playerservercore.PlayerServerCore;
import org.bukkit.entity.Player;

public class WorldOptionsGui extends Gui {
   public static void openGui(Player player) {
      XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
      SGMenu menu = PlayerServerCore.getSpiGUI().create("Správa světa: " + player.getWorld().getName(), 3);
      menu.setAutomaticPaginationEnabled(false);
      menu.setButton(
         11,
         new SGButton(new ItemBuilder(XMaterial.WATER_BUCKET.parseItem()).name("&a&lMožnosti počasí").build())
            .withListener(event -> WorldOptionsGui.WeatherOptionsGui.openGui(player))
      );
      menu.setButton(
         13,
         new SGButton(new ItemBuilder(XMaterial.CLOCK.parseItem()).name("&a&lMožnosti času").build())
            .withListener(event -> WorldOptionsGui.TimeOptionsGui.openGui(player))
      );
      /*menu.setButton(
         14,
         new SGButton(
               new ItemBuilder(XMaterial.PAPER.parseItem())
                  .name("&a&lGame Rule Options")
                  .lore("&7Click to open the game rule options menu.", "&cThese options only apply to the main world.")
                  .build()
            )
            .withListener(event -> GameRuleGui.openGui(player))
      );*/
      menu.setButton(
         15,
         new SGButton(new ItemBuilder(XMaterial.ARROW.parseItem()).name("&c&lZpět").build())
            .withListener(event -> {
               MainGui.openGui(player);
               XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
            })
      );
      player.openInventory(menu.getInventory());
   }

   private static class TimeOptionsGui {
      public static void openGui(Player player) {
         XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
         SGMenu menu = PlayerServerCore.getSpiGUI().create("Možnosti času", 3);
         menu.setAutomaticPaginationEnabled(false);
         menu.setButton(
            10,
            new SGButton(new ItemBuilder(XMaterial.CLOCK.parseItem()).name("&a&lDen").lore("&7Click to set the time to day.").build()).withListener(event -> {
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
               player.getWorld().setTime(1000L);
               player.closeInventory();
            })
         );
         menu.setButton(
            13,
            new SGButton(new ItemBuilder(XMaterial.CLOCK.parseItem()).name("&a&lNoc").lore("&7Click to set the time to night.").build())
               .withListener(event -> {
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  player.getWorld().setTime(13000L);
                  player.closeInventory();
               })
         );
         menu.setButton(
            16,
            new SGButton(new ItemBuilder(XMaterial.ARROW.parseItem()).name("&c&lZpět").build())
               .withListener(event -> {
                  WorldOptionsGui.openGui(player);
                  XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
               })
         );
         player.openInventory(menu.getInventory());
      }
   }

   private static class WeatherOptionsGui {
      public static void openGui(Player player) {
         XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
         SGMenu menu = PlayerServerCore.getSpiGUI().create("Možnosti počasí", 3);
         menu.setAutomaticPaginationEnabled(false);
         menu.setButton(
            10,
            new SGButton(new ItemBuilder(XMaterial.WATER_BUCKET.parseItem()).name("&a&lSlunce").lore("&7Klikni pro slunečné počasí").build())
               .withListener(event -> {
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  player.getWorld().setStorm(false);
                  player.getWorld().setThundering(false);
                  player.closeInventory();
               })
         );
         menu.setButton(
            12,
            new SGButton(new ItemBuilder(XMaterial.WATER_BUCKET.parseItem()).name("&a&lDéšt").lore("&7Klikni a začne pršet").build()).withListener(event -> {
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
               player.getWorld().setStorm(true);
               player.getWorld().setThundering(false);
               player.closeInventory();
            })
         );
         menu.setButton(
            14,
            new SGButton(new ItemBuilder(XMaterial.WATER_BUCKET.parseItem()).name("&a&lBouřka").lore("&7Klikni a začne bouřka").build())
               .withListener(event -> {
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  player.getWorld().setStorm(true);
                  player.getWorld().setThundering(true);
                  player.closeInventory();
               })
         );
         menu.setButton(
            16,
            new SGButton(new ItemBuilder(XMaterial.ARROW.parseItem()).name("&c&lZpět").build())
               .withListener(event -> {
                  WorldOptionsGui.openGui(player);
                  XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
               })
         );
         player.openInventory(menu.getInventory());
      }
   }
}
