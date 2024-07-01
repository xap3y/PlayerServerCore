package eu.xap3y.playerservercore.guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.events.MoveHandler;
import eu.xap3y.playerservercore.listeners.BanListener;
import eu.xap3y.playerservercore.utils.ItemUtils;
import eu.xap3y.playerservercore.listeners.KickListener;
import eu.xap3y.playerservercore.utils.ChatUtil;
import eu.xap3y.playerservercore.utils.PlayerPingUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlayerActionsGui extends Gui {
   public static void openGui(Player player, Player target) {
      try {
         XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
         SGMenu menu = PlayerServerCore.getSpiGUI().create("Akce pro hráče: " + player.getName(), 5);
         menu.setAutomaticPaginationEnabled(false);
         ItemUtils.addBorder(menu, 5);
         int targetPing = PlayerPingUtils.getPlayerPing(player);
         menu.setButton(
            4,
            new SGButton(
                  new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
                     .name("&a&l" + target.getName())
                     .lore(
                        "&8&l|&r &7UUID: &f" + target.getUniqueId().toString().replaceAll("-", ""),
                        "&8&l|&r &7IP: &f" + Objects.requireNonNull(target.getAddress()).getAddress().getHostAddress(),
                        "&8&l|&r &7Ping: &f" + targetPing,
                        "&8&l|&r &7Zdraví: &f" + Math.round(target.getHealth()),
                        "&8&l|&r &7Jidlo: &f" + target.getFoodLevel(),
                        "&8&l|&r &7Herní režim: &f" + target.getGameMode().name(),
                        "&8&l|&r &7Lokace: &f" + target.getLocation().getBlockX() + ", " + target.getLocation().getBlockY() + ", " + target.getLocation().getBlockZ(),
                        "&8&l|&r &7Svět: &f" + target.getWorld().getName(),
                        "",
                        "&cKlikni pro teleportaci"
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
            new SGButton(new ItemBuilder(XMaterial.OAK_DOOR.parseItem()).name("&c&lKick").lore("&7Klikni pro vyhození hráče").build())
               .withListener(new KickListener(target))
         );
         menu.setButton(
            11,
            new SGButton(new ItemBuilder(XMaterial.BARRIER.parseItem()).name("&c&lBan").lore("&7Klikni pro zabanování hráče").build())
               .withListener(new BanListener(target))
         );
         menu.setButton(
            12,
            new SGButton(new ItemBuilder(XMaterial.GOLDEN_APPLE.parseItem()).name("&a&lUzdravit").lore("&7Klikni pro uzdravení").build())
               .withListener(event -> {
                  event.setCancelled(true);
                  target.setHealth(20.0);
                  target.setFoodLevel(20);
                  player.closeInventory();
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7Uzdravil jsi hráče &a" + target.getName() + "&7.");
               })
         );
         menu.setButton(
            13,
            new SGButton(new ItemBuilder(XMaterial.COOKED_BEEF.parseItem()).name("&a&lNakrmit").lore("&7Klikni pro nakrmení").build())
               .withListener(event -> {
                  event.setCancelled(true);
                  target.setFoodLevel(20);
                  player.closeInventory();
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7Nakrmil jsi hráče &a" + target.getName() + "&7.");
               })
         );
         menu.setButton(
            14, new SGButton(new ItemBuilder(XMaterial.BONE.parseItem()).name("&c&lZabít").lore("&7Klikni pro zabití").build()).withListener(event -> {
               event.setCancelled(true);
               target.setHealth(0.0);
               player.closeInventory();
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
               ChatUtil.sendMessage(player, "&9PlayerServers> &7Zabil jsi hráče &a" + target.getName() + "&7.");
            })
         );
         menu.setButton(
            15,
            new SGButton(
                  new ItemBuilder(XMaterial.ICE.parseItem())
                     .name("&b&lZmrazit")
                     .lore("&7Klikni pro zmražení", "&7Hráč se nebude moc hýbat!")
                     .build()
               )
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  MoveHandler.freezePlayer(target);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7Zamrazil jsi hráče &a" + target.getName() + "&7.");
               })
         );
         menu.setButton(
            16,
            new SGButton(
                  new ItemBuilder(XMaterial.LAVA_BUCKET.parseItem())
                     .name("&b&lOdmrazit")
                     .lore("&7Klikni pro odmražení")
                     .build()
               )
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  MoveHandler.unfreezePlayer(target);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7Odmrazil jsi hráče &a" + target.getName() + "&7.");
               })
         );
/*         menu.setButton(
            23, new SGButton(new ItemBuilder(XMaterial.DIAMOND.parseItem()).name("&a&lOP").lore("&7Click to OP this player.").build()).withListener(event -> {
               event.setCancelled(true);
               player.closeInventory();
               target.setOp(true);
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
               ChatUtil.sendMessage(player, "&9PlayerServers> &7Udělil jsi OP hráči &a" + target.getName() + "&7.");
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
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7Odebral jsi OP hráči &a" + target.getName() + "&7.");
               })
         );*/
         menu.setButton(
            19,
            new SGButton(new ItemBuilder(XMaterial.WHITE_WOOL.parseItem()).name("&a&lWhitelist").lore("&7Klikni pro přidání").build())
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  target.setWhitelisted(true);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7Přidal jsi hráče &a" + target.getName() + " &7na bílou listinu.");
               })
         );
         menu.setButton(
            20,
            new SGButton(new ItemBuilder(XMaterial.BLACK_WOOL.parseItem()).name("&a&lUnwhitelist").lore("&7Klikni pro odebrání").build())
               .withListener(event -> {
                  event.setCancelled(true);
                  player.closeInventory();
                  target.setWhitelisted(false);
                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                  ChatUtil.sendMessage(player, "&9PlayerServers> &7Odebral jsi hráče &a" + target.getName() + " &7z bílé listiny.");
               })
         );
          menu.setButton(
                  21,
                  new SGButton(new ItemBuilder(XMaterial.DIRT.parseItem()).name("&a&lSurvival").lore("&7Klikni pro změnu herního režimu").build())
                          .withListener(event -> {
                              event.setCancelled(true);
                              player.closeInventory();
                              target.setGameMode(GameMode.SURVIVAL);
                              XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                              ChatUtil.sendMessage(player, "&9PlayerServers> &7Nastavil jsi herní režim hráči &a" + target.getName() + " &7na survival.");
                          })
          );
          menu.setButton(
                  22,
                  new SGButton(
                          new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).name("&a&lAdventure").lore("&7Klikni pro změnu herního režimu").build()
                  )
                          .withListener(event -> {
                              event.setCancelled(true);
                              player.closeInventory();
                              target.setGameMode(GameMode.ADVENTURE);
                              XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                              ChatUtil.sendMessage(player, "&9PlayerServers> &7Nastavil jsi herní režim hráči &a" + target.getName() + " &7na adventure.");
                          })
          );
          menu.setButton(
                  23,
                  new SGButton(
                          new ItemBuilder(XMaterial.GRASS_BLOCK.parseItem()).name("&a&lSpectator").lore("&7Klikni pro změnu herního režimu").build()
                  )
                          .withListener(event -> {
                              event.setCancelled(true);
                              player.closeInventory();
                              target.setGameMode(GameMode.SPECTATOR);
                              XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                              ChatUtil.sendMessage(player, "&9PlayerServers> &7Nastavil jsi herní režim hráči &a" + target.getName() + " &7na spectator.");
                          })
          );
          if (player.isOp() || player.hasPermission("servercore.action.creative"))
              menu.setButton(
                      24,
                      new SGButton(
                              new ItemBuilder(XMaterial.DIAMOND_BLOCK.parseItem()).name("&a&lCreative").lore("&7Klikni pro změnu herního režimu").build()
                      )
                              .withListener(event -> {
                                  event.setCancelled(true);
                                  player.closeInventory();
                                  target.setGameMode(GameMode.CREATIVE);
                                  XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
                                  ChatUtil.sendMessage(player, "&9PlayerServers> &7Nastavil jsi herní režim hráči &a" + target.getName() + " &7na creative.");
                              })
              );

          menu.setButton(
                  36,
                  new SGButton(new ItemBuilder(XMaterial.ARROW.parseItem()).name("&c&lZpět").build())
                          .withListener(event -> {
                              event.setCancelled(true);
                              PlayerManagementGui.openGui(player);
                          })
          );
         player.openInventory(menu.getInventory());
      } catch (Throwable var5) {
          player.sendMessage(Component.text("§9PlayerServers> §cNastala chyba!"));
      }
   }

   /*private static int getPlayerPing(Player player) {
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
   }*/
}
