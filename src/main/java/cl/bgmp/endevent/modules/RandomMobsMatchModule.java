package cl.bgmp.endevent.modules;

import cl.bgmp.endevent.ChatConstant;
import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.match.Match;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RandomMobsMatchModule extends RandomMobsSummoner implements MatchModule, Listener {
  private Plugin plugin;
  private Match match;
  private BukkitRunnable randomSpawnsTask = getNewRandomSpawnTask();

  public RandomMobsMatchModule(Plugin plugin, Match match) {
    super(match.getEndWorld());
    this.plugin = plugin;
    this.match = match;
  }

  private BukkitRunnable getNewRandomSpawnTask() {
    return new BukkitRunnable() {
      @Override
      public void run() {
        Bukkit.broadcastMessage(ChatConstant.MISC_GENERATING_MOBS.getFormattedMessage(ChatColor.RED));
        generateRandomStuffAround(match.getPlayers());
      }
    };
  }

  @Override
  public void enable() {
    EndEvent.get().registerEvents(this);
    randomSpawnsTask.runTaskTimer(plugin, 0L, 400L); // Every 20 seconds
  }

  @Override
  public void disable() {
    randomSpawnsTask.cancel();
    EndEvent.get().unregisterEvents(this);
  }
}
