package eu.xap3y.playerservercore.listeners;

import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.buttons.SGButtonListener;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.events.MotdHandler;
import eu.xap3y.playerservercore.utils.ChatUtil;
import eu.xap3y.playerservercore.utils.FileUtils;
import eu.xap3y.playerservercore.utils.TitleUtils;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class MotdListener implements SGButtonListener {
   @Override
   public void onClick(InventoryClickEvent event) {
      final Player player = (Player)event.getWhoClicked();
      Prompt prompt = new ValidatingPrompt() {
         public @NotNull String getPromptText(@NotNull ConversationContext context) {
            XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
            TitleUtils.sendTitle(player, "&9Input new MOTD", "&cMOTD must be less than 40 characters", 10, 100000, 10);
            return ChatUtil.translate("&9PlayerServers> &7What would you like to change the MOTD to?");
         }

         protected boolean isInputValid(@NotNull ConversationContext context, String input) {
            return input.length() <= 40;
         }

         protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            player.resetTitle();
            MotdHandler.setMotd(input);
            FileUtils.updateServerProperties("motd", input.replaceAll("&", "§"));
            TitleUtils.sendTitle(player, "&9Successfully changed MOTD", input, 10, 60, 10);
            XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
            return Prompt.END_OF_CONVERSATION;
         }
      };
      ConversationFactory factory = new ConversationFactory(PlayerServerCore.getInstance());
      Conversation conversation = factory.withFirstPrompt(prompt).withLocalEcho(false).buildConversation(player);
      conversation.begin();
      player.closeInventory();
   }
}
