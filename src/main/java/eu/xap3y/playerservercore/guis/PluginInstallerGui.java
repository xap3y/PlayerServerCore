package eu.xap3y.playerservercore.guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.listeners.PluginClickListener;
import eu.xap3y.playerservercore.models.PluginModel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PluginInstallerGui extends Gui {
   public static void openGui(Player player) {
      XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
      SGMenu menu = PlayerServerCore.getSpiGUI().create("Plugins Manager", 5);
      menu.setButton(8, new SGButton(new ItemBuilder(XMaterial.ARROW.parseMaterial()).name("&cGo back").build()).withListener(event -> MainGui.openGui(player)));
      menu.stickSlot(8);
      menu.setAutomaticPaginationEnabled(true);
      List<Plugin> plugins = Arrays.stream(PlayerServerCore.getInstance().getServer().getPluginManager().getPlugins())
         .filter(Plugin::isEnabled)
         .collect(Collectors.toList());
      List<PluginModel> pluginModels = new ArrayList<>();
      File[] pluginRepository = new File("/plugins-repo").listFiles();
      File[] pluginsFolder = new File("plugins").listFiles();
       assert pluginsFolder != null;
       List<File> allPluginsRepo = new ArrayList<>(Arrays.asList(pluginsFolder));

       if (pluginRepository != null) {
          for (File file : pluginRepository) {
             if (allPluginsRepo.stream().noneMatch(f -> f.getName().equals(file.getName()))) {
                allPluginsRepo.add(file);
             }
          }
      }

      Iterator<File> var21 = allPluginsRepo.iterator();

      while (true) {
         String pluginDescription;
         String pluginVersion;
         File filex;
         String pluginName;
         while (true) {
            if (!var21.hasNext()) {
               for (PluginModel plugin : pluginModels) {
                  menu.addButton(
                     new SGButton(
                           new ItemBuilder(plugin.isEnabled() ? XMaterial.GREEN_TERRACOTTA.parseItem() : XMaterial.RED_TERRACOTTA.parseItem())
                              .name("&a&l" + plugin.getName())
                              .lore(
                                 String.format("&7Click here to %s this plugin.", plugin.isEnabled() ? "uninstall" : "install"),
                                 "",
                                 "&7Description: &f" + plugin.getDescription(),
                                 "&7Version: &f" + plugin.getVersion()
                              )
                              .skullOwner(player.getName())
                              .build()
                        )
                        .withListener(new PluginClickListener(plugin))
                  );
               }

               player.openInventory(menu.getInventory());
               return;
            }

            filex = (File)var21.next();
            if (filex.isFile()) {
               String fileName = filex.getName();
               if (fileName.endsWith(".jar")) {
                  try {
                     ZipFile zipFile = new ZipFile(filex);

                     label96: {
                        try {
                           ZipEntry entry = zipFile.getEntry("plugin.yml");
                           if (entry == null) {
                              break label96;
                           }

                           pluginName = "";
                           pluginDescription = "";
                           pluginVersion = "";
                           InputStream stream = zipFile.getInputStream(entry);
                           BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

                           String line;
                           while ((line = br.readLine()) != null) {
                              if (line.startsWith("name: ")) {
                                 pluginName = line.substring(6);
                              } else if (line.startsWith("description: ")) {
                                 pluginDescription = line.substring(13);
                              } else if (line.startsWith("version: ")) {
                                 pluginVersion = line.substring(9);
                              }
                           }
                        } catch (Throwable var19) {
                           try {
                              zipFile.close();
                           } catch (Throwable var18) {
                              var19.addSuppressed(var18);
                           }

                           throw var19;
                        }

                        zipFile.close();
                        break;
                     }

                     zipFile.close();
                  } catch (IOException var20) {
                     var20.printStackTrace();
                  }
               }
            }
         }

         String finalDescription = pluginDescription.isEmpty() ? "No description" : pluginDescription;
         String finalPluginName = pluginName;
         boolean installed = plugins.stream().anyMatch(plugin -> plugin.getName().equals(finalPluginName));
         if (!finalPluginName.equals(PlayerServerCore.getInstance().getName())) {
            pluginModels.add(new PluginModel(finalPluginName, pluginVersion, finalDescription, installed, filex));
         }
      }
   }
}
