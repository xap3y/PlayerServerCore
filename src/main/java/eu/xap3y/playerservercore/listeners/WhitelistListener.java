package eu.xap3y.playerservercore.listeners;

import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.buttons.SGButtonListener;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.guis.WhitelistGui;
import eu.xap3y.playerservercore.utils.ChatUtil;
import eu.xap3y.playerservercore.utils.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class WhitelistListener implements SGButtonListener {
   @Override
   public void onClick(InventoryClickEvent event) {
      final Player player = (Player)event.getWhoClicked();
      Prompt prompt = new ValidatingPrompt() {
         public @NotNull String getPromptText(@NotNull ConversationContext context) {
            XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
            TitleUtils.sendTitle(player, "&9Input the name of the player", "&cMust be less than 16 characters", 10, 100000, 10);
            return ChatUtil.translate("&9PlayerServers> &7Input the name of the player.");
         }

         protected boolean isInputValid(@NotNull ConversationContext context, String input) {
            return input.length() <= 16;
         }

         protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            player.resetTitle();
            OfflinePlayer target = Bukkit.getOfflinePlayer(input);
            target.setWhitelisted(true);
            TitleUtils.sendTitle(player, "&aSuccess!", "&7You have whitelisted &a" + target.getName(), 10, 10, 10);
            XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);

            try {
               Thread.sleep(1500L);
            } catch (InterruptedException var5) {
               throw new RuntimeException(var5);
            }

            WhitelistGui.openGui(player);
            return Prompt.END_OF_CONVERSATION;
         }
      };
      ConversationFactory factory = new ConversationFactory(PlayerServerCore.getInstance());
      Conversation conversation = factory.withFirstPrompt(prompt).withLocalEcho(false).buildConversation(player);
      conversation.begin();
      player.closeInventory();
   }
}
