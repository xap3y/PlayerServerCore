package eu.xap3y.playerservercore.guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.buttons.SGButtonListener;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.listeners.FileClickListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ConfigGui extends Gui {
   public static void openGui(Player player, File directory) {
      XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
      SGMenu menu = PlayerServerCore.getSpiGUI().create("Configuration menu", 5);
      menu.setAutomaticPaginationEnabled(true);
      File parentDirectory = directory.getParentFile();
      File[] files = directory.listFiles();
      Locale currentLocale = Locale.getDefault();
      DateFormat df = DateFormat.getDateTimeInstance(2, 2, currentLocale);
       assert files != null;
       SGButton[] buttons = Arrays.stream(files)
         .sorted((f1, f2) -> Boolean.compare(f2.isDirectory(), f1.isDirectory()))
         .filter(
            file -> !PlayerServerCore.getInstance().getConfig().getStringList("DisabledAccess").contains(file.getName())
                  && !file.getName().equals("PSServerCore")
                  && !file.getName().equals("PlayerServerCore.jar")
         )
         .map(
            file -> {
               List<String> lore = new ArrayList<>();
               lore.add("&7Path: &f" + file.getPath());
               if (file.isFile()) {
                  lore.add("&7Size: &f" + file.length() / 1024L / 1024L + " MB");
                  lore.add("&7Last modified: &f" + df.format(file.lastModified()));
                  lore.add("");
                  lore.add("&7left click to &bedit&7 this file.");
                  lore.add("");
                  lore.add("&7Right click to &cdelete &7this file.");
               }

               return new SGButton(new ItemBuilder(getItemStack(file)).name(file.getName()).lore(lore).build())
                  .withListener((file.isDirectory() ? e -> openGui(player, file) : new FileClickListener(file)));
            }
         )
         .toArray(SGButton[]::new);
      menu.addButtons(buttons);
      if (parentDirectory != null) {
         menu.addButton(
            new SGButton(new ItemBuilder(XMaterial.ARROW.parseItem()).name("&cGo back").build()).withListener(e -> openGui(player, directory.getParentFile()))
         );
      }

      player.openInventory(menu.getInventory());
   }

   private static ItemStack getItemStack(File file) {
      return file.isDirectory()
         ? XMaterial.CHEST.parseItem()
         : (
            Arrays.asList(allowedExtensions).contains(file.getName().substring(file.getName().lastIndexOf(".") + 1))
               ? XMaterial.PAPER.parseItem()
               : XMaterial.BOOK.parseItem()
         );
   }
}
