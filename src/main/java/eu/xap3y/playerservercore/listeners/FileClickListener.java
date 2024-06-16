package eu.xap3y.playerservercore.listeners;

import com.cryptomorin.xseries.XSound;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.samjakob.spigui.buttons.SGButtonListener;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.guis.Gui;
import eu.xap3y.playerservercore.utils.ChatUtil;
import eu.xap3y.playerservercore.utils.TitleUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class FileClickListener implements SGButtonListener {
   private final File file;

   public FileClickListener(File file) {
      this.file = file;
   }

   @Override
   public void onClick(InventoryClickEvent event) {
      switch (event.getClick()) {
         case LEFT:
            this.onLeftClick(event);
            break;
         case RIGHT:
            this.onRightClick(event);
            break;
         case SHIFT_RIGHT:
            this.onShiftClick(event);
      }
   }

   private void onLeftClick(InventoryClickEvent event) {
      Player player = (Player)event.getWhoClicked();
      String panel = PlayerServerCore.getInstance().getConfig().getString("Panel");
      String extension = this.file.getName().substring(this.file.getName().lastIndexOf(".") + 1);
      if (!Arrays.asList(Gui.allowedExtensions).contains(extension)) {
         ChatUtil.sendMessage(player, "&9Error> &7This file type is not supported.");
      } else {
         if (panel != null && !panel.isEmpty()) {
            ComponentBuilder messageBuilder = new ComponentBuilder(ChatUtil.translate("&9PlayerServers> &7Click "))
               .append("here")
               .color(ChatColor.GREEN)
               .underlined(true)
               .event(new ClickEvent(Action.OPEN_URL, panel + "/files/edit#" + this.file.getPath().substring(1)))
               .append(" to open the file editor.")
               .reset()
               .color(ChatColor.GRAY);
            player.spigot().sendMessage(messageBuilder.create());
         } else {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            try {
               out.writeUTF("files");
               out.writeUTF(Objects.requireNonNull(PlayerServerCore.getInstance().getConfig().getString("ServerUUID")));
               out.writeUTF(this.file.getPath());
            } catch (Exception var7) {
               var7.printStackTrace();
            }

            player.sendPluginMessage(PlayerServerCore.getInstance(), "BungeeCord", out.toByteArray());
         }

         player.closeInventory();
      }
   }

   private void onRightClick(InventoryClickEvent event) {
      Player player = (Player)event.getWhoClicked();
      String extension = this.file.getName().substring(this.file.getName().lastIndexOf(".") + 1);
      if (!Arrays.asList(Gui.allowedExtensions).contains(extension)) {
         ChatUtil.sendMessage(player, "&9Error> &7This file type can not be deleted.");
      } else {
         if (this.file.delete()) {
            player.resetTitle();
            TitleUtils.sendTitle(player, "&aSuccess!", "&7File &a" + this.file.getName() + " &7has been deleted successfully.", 10, 10, 10);
            XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
            ChatUtil.sendMessage(player, "&9Success> &7File deleted successfully.");
         } else {
            ChatUtil.sendMessage(player, "&9Error> &7Failed to delete file.");
         }

         player.closeInventory();
      }
   }

   private void onShiftClick(InventoryClickEvent event) {
      final Player player = (Player)event.getWhoClicked();
      String extension = this.file.getName().substring(this.file.getName().lastIndexOf(".") + 1);
      if (!Arrays.asList(Gui.allowedExtensions).contains(extension)) {
         ChatUtil.sendMessage(player, "&9Error> &7This file type can not be modified.");
      } else {
         Prompt prompt = new ValidatingPrompt() {
            public @NotNull String getPromptText(@NotNull ConversationContext context) {
               XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
               TitleUtils.sendTitle(player, "&9Input the new file name", "&cMust be less than 30 characters", 10, 100000, 10);
               return ChatUtil.translate("&9PlayerServers> &7Input the new file name.");
            }

            protected boolean isInputValid(@NotNull ConversationContext context, String input) {
               return input.length() <= 40;
            }

            protected Prompt acceptValidatedInput(@NotNull ConversationContext context, String input) {
               if (!input.contains(".")) {
                  player.resetTitle();
                  TitleUtils.sendTitle(player, "&cFile must contain an extension!", "&7Failure, file name must contain an extension.", 10, 60, 10);
                  return Prompt.END_OF_CONVERSATION;
               } else {
                  String extension = input.substring(input.lastIndexOf(".") + 1);
                  if (!Arrays.asList(Gui.allowedExtensions).contains(extension)) {
                     player.resetTitle();
                     TitleUtils.sendTitle(player, "&cInvalid file extension!", "&7Failure, this file extension is not allowed.", 10, 60, 10);
                     return Prompt.END_OF_CONVERSATION;
                  } else {
                     File newFile = new File(FileClickListener.this.file.getParentFile(), input);
                     if (!FileClickListener.this.file.renameTo(newFile)) {
                        ChatUtil.sendMessage(player, "&9Error> &7Failed to rename file.");
                        return Prompt.END_OF_CONVERSATION;
                     } else {
                        player.resetTitle();
                        TitleUtils.sendTitle(player, "&aSuccess!", "&7File renamed to &a" + input, 10, 10, 10);
                        XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);

                        try {
                           Thread.sleep(1500L);
                        } catch (InterruptedException var6) {
                           throw new RuntimeException(var6);
                        }

                        return Prompt.END_OF_CONVERSATION;
                     }
                  }
               }
            }
         };
         ConversationFactory factory = new ConversationFactory(PlayerServerCore.getInstance());
         Conversation conversation = factory.withFirstPrompt(prompt).withLocalEcho(false).buildConversation(player);
         conversation.begin();
         player.closeInventory();
      }
   }
}
