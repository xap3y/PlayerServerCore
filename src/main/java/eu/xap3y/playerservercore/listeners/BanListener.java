package eu.xap3y.playerservercore.listeners;

import com.cryptomorin.xseries.XSound;
import com.samjakob.spigui.buttons.SGButtonListener;
import java.util.Date;

import eu.xap3y.playerservercore.PlayerServerCore;
import eu.xap3y.playerservercore.guis.MainGui;
import eu.xap3y.playerservercore.utils.ChatUtil;
import eu.xap3y.playerservercore.utils.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.BanList.Type;
import org.bukkit.configuration.Configuration;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.spigot.api.title.TitleApi;

public class BanListener implements SGButtonListener {
   private final Player target;

   public BanListener(Player target) {
      this.target = target;
   }

   @Override
   public void onClick(InventoryClickEvent event) {
      final Player player = (Player)event.getWhoClicked();
      Configuration config = PlayerServerCore.getInstance().getConfig();
      String owner = config.getString("Owner", "OwnerCZ");
      if (!target.isOp() && !target.getName().equals(owner) && !target.getUniqueId().toString().equals(owner)) {
         final Prompt reason = new ValidatingPrompt() {
            public @NotNull String getPromptText(@NotNull ConversationContext context) {
               XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
               TitleUtils.sendTitle(player, "&9Input ban reason", "&cMust be less than 40 characters", 10, 100000, 10);
               return ChatUtil.translate("&9PlayerServers> &7Input ban reason.");
            }

            protected boolean isInputValid(@NotNull ConversationContext context, String input) {
               return input.length() <= 40;
            }

            protected Prompt acceptValidatedInput(ConversationContext context, @NotNull String input) {
               player.resetTitle();
               long time = (Long)context.getSessionData("time");
               Date expiration = time == 0L ? null : new Date(System.currentTimeMillis() + time);
               Bukkit.getServer().getBanList(Type.NAME).addBan(BanListener.this.target.getName(), input, expiration, player.getName());
               BanListener.this.target.kickPlayer(ChatUtil.translate("Banned: " + input));
               TitleUtils.sendTitle(
                  player,
                  "&aSuccess!",
                  "&7Successfully banned &a" + BanListener.this.target.getName() + (expiration != null ? " &7for &a" + time + " minutes" : " permanently"),
                  10,
                  10,
                  10
               );
               //TitleApi.sendActionbar(player, "&7Banned for: &a" + input);

               try {
                  Thread.sleep(1500L);
               } catch (InterruptedException var7) {
                  throw new RuntimeException(var7);
               }

               MainGui.openGui(player);
               XSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(player);
               return Prompt.END_OF_CONVERSATION;
            }
         };
         Prompt duration = new NumericPrompt() {
            public @NotNull String getPromptText(@NotNull ConversationContext context) {
               XSound.BLOCK_NOTE_BLOCK_BASS.play(player);
               TitleUtils.sendTitle(player, "&9Input duration in minutes", "&cEnter 0 for permanent ban", 10, 100000, 10);
               return ChatUtil.translate("&9PlayerServers> &7Input duration.");
            }

            protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
               player.resetTitle();
               long duration = input.longValue();
               context.setSessionData("time", duration);
               return reason;
            }
         };
         ConversationFactory factory = new ConversationFactory(PlayerServerCore.getInstance());
         Conversation conversation = factory.withLocalEcho(false).withFirstPrompt(duration).buildConversation(player);
         conversation.begin();
         player.closeInventory();
      } else {
         ChatUtil.sendMessage(player, "&9Permissions> &7You can not perform this action on this player.");
      }
   }
}
