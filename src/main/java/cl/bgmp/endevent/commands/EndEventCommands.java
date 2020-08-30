package cl.bgmp.endevent.commands;

import cl.bgmp.endevent.ChatConstant;
import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.match.Match;
import cl.bgmp.endevent.match.MatchManager;
import cl.bgmp.endevent.match.MatchState;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import java.time.Duration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class EndEventCommands {
  @Command(
      aliases = {"start"},
      desc = "Inicia la partida actual.",
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
      aliases = {"observers", "obs"},
      desc = "Te añade a los observadores de la partida actual.",
      max = 0)
  @CommandPermissions("endevent.obs")
  public static void observers(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + ChatConstant.NO_CONSOLE.getMessage());
      return;
    }

    Player player = (Player) sender;
    Match match = EndEvent.get().getMatchManager().getCurrentMatch();

    if (match.isObserving(player)) {
      player.sendMessage(ChatConstant.ALREADY_SPECTATING.getFormattedMessage(ChatColor.RED));
    } else {
      match.addObserver(player);
      player.sendMessage(ChatConstant.NOW_SPECTATING.getFormattedMessage(ChatColor.GREEN));
    }
  }

  @Command(
      aliases = {"join"},
      desc = "Te añade a los participantes del evento.",
      max = 0)
  @CommandPermissions("endevent.join")
  public static void join(final CommandContext args, final CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + ChatConstant.NO_CONSOLE.getMessage());
      return;
    }

    Player player = (Player) sender;
    Match match = EndEvent.get().getMatchManager().getCurrentMatch();

    if (!match.isObserving(player)) {
      player.sendMessage(ChatConstant.ALREADY_NOT_SPECTATING.getFormattedMessage(ChatColor.RED));
    } else {
      match.removeObserver(player);
      player.sendMessage(ChatConstant.NOW_NOT_SPECTATING.getFormattedMessage(ChatColor.GREEN));
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
