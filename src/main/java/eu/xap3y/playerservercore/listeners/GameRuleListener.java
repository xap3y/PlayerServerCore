package eu.xap3y.playerservercore.listeners;

import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.buttons.SGButtonListener;
import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.guis.GameRuleGui;
import eu.xap3y.playerservercore.utils.ChatUtil;
import eu.xap3y.playerservercore.utils.TitleUtils;
import org.bukkit.World;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class GameRuleListener implements SGButtonListener {
   final String gameRule;

   public GameRuleListener(String gameRule) {
      this.gameRule = gameRule;
   }

   @Override
   public void onClick(InventoryClickEvent event) {
      final Player player = (Player)event.getWhoClicked();
      Prompt prompt = new StringPrompt() {
         public @NotNull String getPromptText(@NotNull ConversationContext context) {
            XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
            TitleUtils.sendTitle(
               player, "&9Input new value for &a" + GameRuleListener.this.gameRule, "&7Value is usually true or false or a number.", 10, 100000, 10
            );
            return ChatUtil.translate(
               "&9PlayerServers> &7Please enter a new value for &a"
                  + GameRuleListener.this.gameRule
                  + "&7. Be careful, make sure you know what you're doing. Value is usually true or false or a number."
            );
         }

         public Prompt acceptInput(@NotNull ConversationContext context, String input) {
            player.resetTitle();
            World world = (World) PlayerServerCore.getInstance().getServer().getWorlds().get(0);
            world.setGameRuleValue(GameRuleListener.this.gameRule, input);
            TitleUtils.sendTitle(player, "&aSuccess!", "Game rule " + GameRuleListener.this.gameRule + " changed to &e" + input + "&r.", 10, 20, 10);

            try {
               Thread.sleep(1500L);
            } catch (InterruptedException var5) {
               throw new RuntimeException(var5);
            }

            GameRuleGui.openGui(player);
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
