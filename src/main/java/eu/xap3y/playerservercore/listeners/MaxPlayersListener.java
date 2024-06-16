package eu.xap3y.playerservercore.listeners;

import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.buttons.SGButtonListener;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.utils.ChatUtil;
import eu.xap3y.playerservercore.utils.ServerUtils;
import eu.xap3y.playerservercore.utils.TitleUtils;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MaxPlayersListener implements SGButtonListener {
   @Override
   public void onClick(InventoryClickEvent event) {
      final Player player = (Player)event.getWhoClicked();
      Prompt prompt = new NumericPrompt() {
         protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            player.resetTitle();
            if (input.intValue() > PlayerServerCore.getInstance().getConfig().getInt("MaxPlayers")) {
               TitleUtils.sendTitle(
                  player, "&cFailure!", "Max players cannot be higher then &e" + PlayerServerCore.getInstance().getConfig().getInt("MaxPlayers") + "&r.", 10, 60, 10
               );
            } else {
               try {
                  ServerUtils.changeSlots(input.intValue(), true);
               } catch (ReflectiveOperationException var4) {
                  ChatUtil.sendMessage(player, "&9Error> &cAn error occurred while trying to change the max players.");
                  return Prompt.END_OF_CONVERSATION;
               }

               TitleUtils.sendTitle(player, "&aSuccess!", "Max players changed to &e" + input + "&r.", 10, 60, 10);
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
            }
             return Prompt.END_OF_CONVERSATION;
         }

         public String getPromptText(ConversationContext context) {
            XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
            TitleUtils.sendTitle(player, "&9Input max players", "&7How many players should be able to join?", 10, 100000, 10);
            return ChatUtil.translate("&9PlayerServers> &7How many players should be able to join?");
         }
      };
      ConversationFactory factory = new ConversationFactory(PlayerServerCore.getInstance());
      Conversation conversation = factory.withFirstPrompt(prompt).withLocalEcho(false).buildConversation(player);
      conversation.begin();
      player.closeInventory();
   }
}
