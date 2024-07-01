package eu.xap3y.playerservercore.listeners;

import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.buttons.SGButtonListener;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.guis.WhitelistGui;
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
import org.jetbrains.annotations.NotNull;

public class MaxPlayersListener implements SGButtonListener {
   @Override
   public void onClick(InventoryClickEvent event) {
      final Player player = (Player)event.getWhoClicked();
      String ownerFromCfg = PlayerServerCore.getInstance().getConfig().getString("Owner", "OwnerCZ");
      boolean isOwner = ownerFromCfg.equals(player.getName()) || ownerFromCfg.equals(player.getUniqueId().toString());

      if (!player.hasPermission("servercore.management.maxplayers") && !player.isOp() && !isOwner) {
         assert XSound.BLOCK_ANVIL_FALL.parseSound() != null;
         player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_FALL.parseSound(), 1f, 1f);
         return;
      }

      Prompt prompt = new NumericPrompt() {
         protected Prompt acceptValidatedInput(@NotNull ConversationContext context, Number input) {
            player.resetTitle();
            if (input.intValue() < 1) {
               TitleUtils.sendTitle(player, "&cZrušeno", "", 10, 60, 10);
               XSound.BLOCK_ANVIL_FALL.play(player);
               return Prompt.END_OF_CONVERSATION;
            }
            if (input.intValue() > PlayerServerCore.getInstance().getConfig().getInt("MaxPlayers")) {
               TitleUtils.sendTitle(
                  player, "&cChyba!", "Maximální počet hráčů nemůže být vyšší než &e" + PlayerServerCore.getInstance().getConfig().getInt("MaxPlayers") + "&r.", 10, 60, 10
               );
            } else {
               try {
                  ServerUtils.changeSlots(input.intValue(), true);
               } catch (ReflectiveOperationException var4) {
                  ChatUtil.sendMessage(player, "&9Error> &cAn error occurred while trying to change the max players.");
                  return Prompt.END_OF_CONVERSATION;
               }

               TitleUtils.sendTitle(player, "&aNastaveno!", "Maximální počet hráčů nastaven na &e" + input + "&r.", 10, 60, 10);
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
            }
             return Prompt.END_OF_CONVERSATION;
         }

         public @NotNull String getPromptText(@NotNull ConversationContext context) {
            XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
            TitleUtils.sendTitle(player, "&9Napiš nový počet", "", 10, 100000, 10);
            return ChatUtil.translate("&9PlayerServers> &7Napiš do chatu nový počet hráčů");
         }
      };
      ConversationFactory factory = new ConversationFactory(PlayerServerCore.getInstance());
      Conversation conversation = factory.withFirstPrompt(prompt).withLocalEcho(false).buildConversation(player);
      conversation.begin();
      player.closeInventory();
   }
}
