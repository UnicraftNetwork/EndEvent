package cl.bgmp.endevent.commands;

import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.match.MatchManager;
import cl.bgmp.endevent.match.MatchState;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import java.time.Duration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndEventCommands {
  @Command(
      aliases = {"start"},
      desc = "Starts the upcoming EndEvent match.",
      max = 0)
  @CommandPermissions("endevent.start")
  public static void start(final CommandContext args, final CommandSender sender) {
    MatchManager matchManager = EndEvent.get().getMatchManager();

    if (matchManager.getCurrentMatch().getState() == MatchState.IDLE) {
      matchManager.getCurrentMatch().startIn(Duration.ofSeconds(30));
    } else {
      sender.sendMessage(ChatColor.RED + "Match cannot be started at this moment.");
    }
  }

  @Command(
      aliases = {"test"},
      desc = "test command",
      max = 0)
  @CommandPermissions("endevent.test")
  public static void test(final CommandContext args, final CommandSender sender) {
    MatchManager matchManager = EndEvent.get().getMatchManager();

    for (Player player : matchManager.getCurrentMatch().getPlayers()) {
      Bukkit.broadcastMessage("Is participating: " + player.getDisplayName());
    }
  }
}
