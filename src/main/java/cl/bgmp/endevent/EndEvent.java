package cl.bgmp.endevent;

import cl.bgmp.endevent.commands.EndEventCommands;
import cl.bgmp.endevent.match.MatchManager;
import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import com.sk89q.minecraft.util.commands.CommandsManager;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class EndEvent extends JavaPlugin {
  private static EndEvent endEvent;
  private MatchManager matchManager;
  private CommandsManager commands;
  private CommandsManagerRegistration commandRegistry;
  private Config config;

  public static EndEvent get() {
    return endEvent;
  }

  public MatchManager getMatchManager() {
    return matchManager;
  }

  public Config getConfiguration() {
    return config;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      Command command,
      @NotNull String label,
      @NotNull String[] args) {
    try {
      this.commands.execute(command.getName(), args, sender, sender);
    } catch (CommandPermissionsException exception) {
      sender.sendMessage("You do not have permission");
    } catch (MissingNestedCommandException exception) {
      sender.sendMessage(exception.getUsage());
    } catch (CommandUsageException exception) {
      sender.sendMessage(ChatColor.RED + exception.getMessage());
      sender.sendMessage(ChatColor.RED + exception.getUsage());
    } catch (WrappedCommandException exception) {
      if (exception.getCause() instanceof NumberFormatException) {
        sender.sendMessage("Expected a number, received string instead.");
      } else {
        sender.sendMessage("An unknown error has occurred.");
        exception.printStackTrace();
      }
    } catch (CommandException exception) {
      sender.sendMessage(ChatColor.RED + exception.getMessage());
    }
    return true;
  }

  @Override
  public void onEnable() {
    endEvent = this;

    getConfig().options().copyDefaults(true);
    saveConfig();
    reloadConfig();
    if (config == null) {
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    commands = new BukkitCommandsManager();
    commandRegistry = new CommandsManagerRegistration(this, this.commands);

    registerCommands();

    matchManager = new MatchManager(this, getLogger());
  }

  private void registerCommands() {
    commandRegistry.register(EndEventCommands.class);
  }

  public void registerEvents(Listener... listeners) {
    PluginManager pluginManager = Bukkit.getPluginManager();
    Arrays.stream(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
  }

  public void unregisterEvents(Listener... listeners) {
    for (Listener listener : listeners) {
      HandlerList.unregisterAll(listener);
    }
  }

  @Override
  public void reloadConfig() {
    super.reloadConfig();

    final boolean startup = config == null;
    try {
      config = new EndEventConfig(getConfig(), getDataFolder());
    } catch (RuntimeException e) {
      e.printStackTrace();
      getLogger().severe("Could not load config file.");
      return;
    }

    if (!startup) getLogger().fine("Configuration reloaded.");
  }
}
