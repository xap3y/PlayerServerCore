package eu.xap3y.playerservercore.listeners;

import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.buttons.SGButtonListener;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.guis.MainGui;
import eu.xap3y.playerservercore.utils.ChatUtil;
import eu.xap3y.playerservercore.utils.TitleUtils;
import org.bukkit.configuration.Configuration;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class KickListener implements SGButtonListener {
   private final Player target;

   public KickListener(Player target) {
      this.target = target;
   }

   @Override
   public void onClick(InventoryClickEvent event) {
      final Player player = (Player)event.getWhoClicked();
      Configuration config = PlayerServerCore.getInstance().getConfig();
      String owner = config.getString("Owner", "OwnerCZ");
      if (!target.isOp() && !target.hasPermission("servercore.manage") && !target.getName().equals(owner) && !target.getUniqueId().toString().equals(owner)) {
         Prompt prompt = new ValidatingPrompt() {
            public @NotNull String getPromptText(@NotNull ConversationContext context) {
               XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
               TitleUtils.sendTitle(player, "&9Input kick reason", "&cMust be less than 40 characters", 10, 100000, 10);
               return ChatUtil.translate("&9PlayerServers> &7Input kick reason.");
            }

            protected boolean isInputValid(@NotNull ConversationContext context, String input) {
               return input.length() <= 40;
            }

            protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
               player.resetTitle();
               KickListener.this.target.kickPlayer(ChatUtil.translate(input));
               TitleUtils.sendTitle(
                  player,
                  "&aSuccess!",
                  "&7Successfully kicked &a" + KickListener.this.target.getName() + " &7for: &a" + input.substring(0, 20) + (input.length() > 20 ? "..." : ""),
                  10,
                  10,
                  10
               );
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);

               try {
                  Thread.sleep(1500L);
               } catch (InterruptedException var4) {
                  throw new RuntimeException(var4);
               }

               MainGui.openGui(player);
               return Prompt.END_OF_CONVERSATION;
            }
         };
         ConversationFactory factory = new ConversationFactory(PlayerServerCore.getInstance());
         Conversation conversation = factory.withFirstPrompt(prompt).withLocalEcho(false).buildConversation(player);
         conversation.begin();
         player.closeInventory();
      } else {
         ChatUtil.sendMessage(player, "&9Permissions> &7You can not perform this action on this player.");
      }
   }
}
