package eu.xap3y.playerservercore.guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.events.MoveHandler;
import eu.xap3y.playerservercore.listeners.BanListener;
import eu.xap3y.playerservercore.listeners.ItemUtils;
import eu.xap3y.playerservercore.listeners.KickListener;
import eu.xap3y.playerservercore.utils.ChatUtil;
import eu.xap3y.playerservercore.utils.PlayerPingUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlayerActionsGui extends Gui {
   public static void openGui(Player player, Player target) {
      try {
         XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
         int rows = 5;
         SGMenu menu = PlayerServerCore.getSpiGUI().create("Manage: " + player.getName(), 5);
         menu.setAutomaticPaginationEnabled(false);
         ItemUtils.addBorder(menu, 5);
         int targetPing = PlayerPingUtils.getPlayerPing(player);
         menu.setButton(
            4,
            new SGButton(
                  new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                     .name("&a&l" + target.getName())
                     .lore(
                        "&7UUID: &f" + target.getUniqueId(),
                        "&7IP: &f" + Objects.requireNonNull(target.getAddress()).getAddress().getHostAddress(),
                        "&7Ping: &f" + targetPing,
                        "&7Health: &f" + Math.round(target.getHealth()),
                        "&7Food: &f" + target.getFoodLevel(),
                        "&7Gamemode: &f" + target.getGameMode().name(),
                        "&7Location: &f" + target.getLocation().getBlockX() + ", " + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ(),
                        "&7World: &f" + target.getWorld().getName(),
                        "",
                        "&cClick to teleport"
                     )
                     .skullOwner(target.getName())
                     .build()
               )
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  player.teleport(target);
               })
         );
         menu.setButton(
            10,
            new SGButton(new ItemBuilder(XMaterial.OAK_DOOR.parseItem()).name("&c&lKick").lore("&7Click to kick this player.").build())
               .withListener(new KickListener(target))
         );
         menu.setButton(
            11,
            new SGButton(new ItemBuilder(XMaterial.BARRIER.parseItem()).name("&c&lBan").lore("&7Click to ban this player.").build())
               .withListener(new BanListener(target))
         );
         menu.setButton(
            12,
            new SGButton(new ItemBuilder(XMaterial.GOLDEN_APPLE.parseItem()).name("&a&lHeal").lore("&7Click to heal this player.").build())
               .withListener(event -> {
                  event.setCancelled(true);
                  target.setHealth(20.0);
                  target.setFoodLevel(20);
                  player.closeInventory();
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7You have healed &a" + target.getName() + "&7.");
               })
         );
         menu.setButton(
            13,
            new SGButton(new ItemBuilder(XMaterial.COOKED_BEEF.parseItem()).name("&a&lFeed").lore("&7Click to feed this player.").build())
               .withListener(event -> {
                  event.setCancelled(true);
                  target.setFoodLevel(20);
                  player.closeInventory();
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7You have fed &a" + target.getName() + "&7.");
               })
         );
         menu.setButton(
            14, new SGButton(new ItemBuilder(XMaterial.BONE.parseItem()).name("&c&lKill").lore("&7Click to kill this player.").build()).withListener(event -> {
               event.setCancelled(true);
               target.setHealth(0.0);
               player.closeInventory();
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
               ChatUtil.sendMessage(player, "&9PlayerServers> &7You have killed &a" + target.getName() + "&7.");
            })
         );
         menu.setButton(
            15,
            new SGButton(
                  new ItemBuilder(XMaterial.ICE.parseItem())
                     .name("&b&lFreeze")
                     .lore("&7Click to freeze this player.", "&7This will prevent them from moving.")
                     .build()
               )
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  MoveHandler.freezePlayer(target);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7You have frozen &a" + target.getName() + "&7.");
               })
         );
         menu.setButton(
            16,
            new SGButton(
                  new ItemBuilder(XMaterial.LAVA_BUCKET.parseItem())
                     .name("&b&lUnfreeze")
                     .lore("&7Click to unfreeze this player.", "&7This will allow them to move again.")
                     .build()
               )
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  MoveHandler.unfreezePlayer(target);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7You have unfrozen &a" + target.getName() + "&7.");
               })
         );
         menu.setButton(
            19,
            new SGButton(
                  new ItemBuilder(XMaterial.DIAMOND_BLOCK.parseItem()).name("&a&lCreative").lore("&7Click to set this player's gamemode to creative.").build()
               )
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  target.setGameMode(GameMode.CREATIVE);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7You have set &a" + target.getName() + "'s &7gamemode to creative.");
               })
         );
         menu.setButton(
            20,
            new SGButton(new ItemBuilder(XMaterial.DIRT.parseItem()).name("&a&lSurvival").lore("&7Click to set this player's gamemode to survival.").build())
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  target.setGameMode(GameMode.SURVIVAL);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7You have set &a" + target.getName() + "'s &7gamemode to survival.");
               })
         );
         menu.setButton(
            21,
            new SGButton(
                  new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).name("&a&lAdventure").lore("&7Click to set this player's gamemode to adventure.").build()
               )
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  target.setGameMode(GameMode.ADVENTURE);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7You have set &a" + target.getName() + "'s &7gamemode to adventure.");
               })
         );
         menu.setButton(
            22,
            new SGButton(
                  new ItemBuilder(XMaterial.GRASS_BLOCK.parseItem()).name("&a&lSpectator").lore("&7Click to set this player's gamemode to spectator.").build()
               )
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  target.setGameMode(GameMode.SPECTATOR);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7You have set &a" + target.getName() + "'s &7gamemode to spectator.");
               })
         );
         menu.setButton(
            23, new SGButton(new ItemBuilder(XMaterial.DIAMOND.parseItem()).name("&a&lOP").lore("&7Click to OP this player.").build()).withListener(event -> {
               event.setCancelled(true);
               player.closeInventory();
               target.setOp(true);
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
               ChatUtil.sendMessage(player, "&9PlayerServers> &7You have OP'd &a" + target.getName() + "&7.");
            })
         );
         menu.setButton(
            24,
            new SGButton(new ItemBuilder(XMaterial.IRON_INGOT.parseItem()).name("&a&lDeOP").lore("&7Click to DeOP this player.").build())
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  target.setOp(false);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7You have DeOP'd &a" + target.getName() + "&7.");
               })
         );
         menu.setButton(
            25,
            new SGButton(new ItemBuilder(XMaterial.WHITE_WOOL.parseItem()).name("&a&lWhitelist").lore("&7Click to whitelist this player.").build())
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  target.setWhitelisted(true);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7You have whitelisted &a" + target.getName() + "&7.");
               })
         );
         menu.setButton(
            28,
            new SGButton(new ItemBuilder(XMaterial.BLACK_WOOL.parseItem()).name("&a&lUnwhitelist").lore("&7Click to unwhitelist this player.").build())
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  target.setWhitelisted(false);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7You have unwhitelisted &a" + target.getName() + "&7.");
               })
         );
         player.openInventory(menu.getInventory());
      } catch (Throwable var5) {
          try {
              throw var5;
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }
   }

   private static int getPlayerPing(Player player) {
      try {
         String packageName = Bukkit.getServer().getClass().getPackage().getName();
         String serverVersion = packageName.substring(packageName.lastIndexOf(".") + 1);
         int ping = -1;
         Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
         Object craftPlayer = craftPlayerClass.cast(player);
         Object entityPlayer = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);
         return (Integer)entityPlayer.getClass().getMethod("ping").invoke(entityPlayer);
      } catch (Exception var7) {
         var7.printStackTrace();
         return -1;
      }
   }
}
