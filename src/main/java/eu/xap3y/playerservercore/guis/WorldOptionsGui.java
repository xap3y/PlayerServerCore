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
      SGMenu menu = PlayerServerCore.getSpiGUI().create("World management: " + player.getWorld().getName(), 3);
      menu.setAutomaticPaginationEnabled(false);
      menu.setButton(
         10,
         new SGButton(new ItemBuilder(XMaterial.WATER_BUCKET.parseItem()).name("&a&lWeather Options").lore("&7Click to open the weather options menu.").build())
            .withListener(event -> WorldOptionsGui.WeatherOptionsGui.openGui(player))
      );
      menu.setButton(
         12,
         new SGButton(new ItemBuilder(XMaterial.CLOCK.parseItem()).name("&a&lTime Options").lore("&7Click to open the time options menu.").build())
            .withListener(event -> WorldOptionsGui.TimeOptionsGui.openGui(player))
      );
      menu.setButton(
         14,
         new SGButton(
               new ItemBuilder(XMaterial.PAPER.parseItem())
                  .name("&a&lGame Rule Options")
                  .lore("&7Click to open the game rule options menu.", "&cThese options only apply to the main world.")
                  .build()
            )
            .withListener(event -> GameRuleGui.openGui(player))
      );
      menu.setButton(
         16,
         new SGButton(new ItemBuilder(XMaterial.ARROW.parseItem()).name("&c&lBack").lore("&7Click to go back to the main menu.").build())
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
         SGMenu menu = PlayerServerCore.getSpiGUI().create("Time Options", 3);
         menu.setAutomaticPaginationEnabled(false);
         menu.setButton(
            10,
            new SGButton(new ItemBuilder(XMaterial.CLOCK.parseItem()).name("&a&lDay").lore("&7Click to set the time to day.").build()).withListener(event -> {
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
               player.getWorld().setTime(1000L);
               player.closeInventory();
            })
         );
         menu.setButton(
            13,
            new SGButton(new ItemBuilder(XMaterial.CLOCK.parseItem()).name("&a&lNight").lore("&7Click to set the time to night.").build())
               .withListener(event -> {
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  player.getWorld().setTime(13000L);
                  player.closeInventory();
               })
         );
         menu.setButton(
            16,
            new SGButton(new ItemBuilder(XMaterial.ARROW.parseItem()).name("&c&lBack").lore("&7Click to go back to the main menu.").build())
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
         SGMenu menu = PlayerServerCore.getSpiGUI().create("Weather Options", 3);
         menu.setAutomaticPaginationEnabled(false);
         menu.setButton(
            10,
            new SGButton(new ItemBuilder(XMaterial.WATER_BUCKET.parseItem()).name("&a&lClear").lore("&7Click to clear the weather.").build())
               .withListener(event -> {
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  player.getWorld().setStorm(false);
                  player.getWorld().setThundering(false);
                  player.closeInventory();
               })
         );
         menu.setButton(
            12,
            new SGButton(new ItemBuilder(XMaterial.WATER_BUCKET.parseItem()).name("&a&lRain").lore("&7Click to make it rain.").build()).withListener(event -> {
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
               player.getWorld().setStorm(true);
               player.getWorld().setThundering(false);
               player.closeInventory();
            })
         );
         menu.setButton(
            14,
            new SGButton(new ItemBuilder(XMaterial.WATER_BUCKET.parseItem()).name("&a&lThunder").lore("&7Click to make it thunder.").build())
               .withListener(event -> {
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  player.getWorld().setStorm(true);
                  player.getWorld().setThundering(true);
                  player.closeInventory();
               })
         );
         menu.setButton(
            16,
            new SGButton(new ItemBuilder(XMaterial.ARROW.parseItem()).name("&c&lBack").lore("&7Click to go back to the main menu.").build())
               .withListener(event -> {
                  WorldOptionsGui.openGui(player);
                  XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
               })
         );
         player.openInventory(menu.getInventory());
      }
   }
}
