package eu.xap3y.playerservercore.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

public class FileUtils {
   public static void updateServerProperties(String key, String value) {
      Properties properties = new Properties();
      File propertiesFile = new File("server.properties");

      try {
         InputStream is = Files.newInputStream(propertiesFile.toPath());

         try {
            properties.load(is);
         } catch (Throwable var10) {
             try {
                 is.close();
             } catch (Throwable var8) {
                 var10.addSuppressed(var8);
             }

             throw var10;
         }

          is.close();

          if (properties.getProperty(key).equals(value)) {
            return;
         }

         properties.setProperty(key, value);
         OutputStream os = Files.newOutputStream(propertiesFile.toPath());

         try {
            properties.store(os, "Minecraft server properties");
         } catch (Throwable var9) {
            if (os != null) {
               try {
                  os.close();
               } catch (Throwable var7) {
                  var9.addSuppressed(var7);
               }
            }

            throw var9;
         }

         if (os != null) {
            os.close();
         }
      } catch (IOException var11) {
         Bukkit.getLogger().log(Level.SEVERE, "An error occurred while updating the server properties", (Throwable)var11);
      }
   }

   public static void copyFromRepository(File plugin) {
      try {
         Files.copy(plugin.toPath(), new File("plugins/" + plugin.getName()).toPath());
      } catch (IOException var2) {
         Bukkit.getLogger().log(Level.SEVERE, "An error occurred while copying the plugin", (Throwable)var2);
      }
   }

   public static Exception loadPlugin(File plugin) {
      Plugin loadedPlugin = null;

      try {
         loadedPlugin = Bukkit.getPluginManager().loadPlugin(plugin);
      } catch (UnknownDependencyException var3) {
         return var3;
      } catch (InvalidDescriptionException | InvalidPluginException var4) {
         var4.printStackTrace();
         return var4;
      }

       assert loadedPlugin != null;
       Bukkit.getPluginManager().enablePlugin(loadedPlugin);
      return null;
   }
}
