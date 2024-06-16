package eu.xap3y.playerservercore.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.Server;

public class ServerUtils {
   private static Object minecraftServer;
   private static Field recentTps;

   public static double[] getRecentTps() {
      try {
         if (minecraftServer == null) {
            Server server = Bukkit.getServer();
            Field consoleField = server.getClass().getDeclaredField("console");
            consoleField.setAccessible(true);
            minecraftServer = consoleField.get(server);
         }

         if (recentTps == null) {
            recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
            recentTps.setAccessible(true);
         }

         return (double[])recentTps.get(minecraftServer);
      } catch (NoSuchFieldException | IllegalAccessException var2) {
         return new double[]{20.0, 20.0, 20.0};
      }
   }

   public static void changeSlots(Integer slots, boolean updateProperties) throws ReflectiveOperationException {
      Method serverGetHandle = Bukkit.getServer().getClass().getDeclaredMethod("getHandle");
      Object playerList = serverGetHandle.invoke(Bukkit.getServer());
      Field maxPlayersField = getMaxPlayersField(playerList);
      maxPlayersField.setInt(playerList, slots);
      if (updateProperties) {
         FileUtils.updateServerProperties("max-players", slots.toString());
      }
   }

   private static Field getMaxPlayersField(Object playerList) throws ReflectiveOperationException {
      Class<?> playerListClass = playerList.getClass().getSuperclass();

      try {
         Field field = playerListClass.getDeclaredField("maxPlayers");
         field.setAccessible(true);
         return field;
      } catch (NoSuchFieldException var7) {
         for (Field fieldx : playerListClass.getDeclaredFields()) {
            if (fieldx.getType() == int.class) {
               fieldx.setAccessible(true);
               if (fieldx.getInt(playerList) == Bukkit.getMaxPlayers()) {
                  return fieldx;
               }
            }
         }

         throw new NoSuchFieldException("Unable to find maxPlayers field in " + playerListClass.getName());
      }
   }
}
