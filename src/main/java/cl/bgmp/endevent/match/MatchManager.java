package cl.bgmp.endevent.match;

import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.events.MatchFinishEvent;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Manager of all the matches, in charge of ensuring only one is playing at a time and of tracking
 * and registering their progress
 */
public class MatchManager implements Listener {
  private Plugin plugin;
  private Logger logger;
  private List<Match> matches = Lists.newArrayList();

  public MatchManager(Plugin plugin, Logger logger) {
    this.logger = logger;
    this.plugin = plugin;

    EndEvent.get().registerEvents(this);
    prepareNewMatch(); // Constructs and adds the first match to the manager
  }

  private void prepareNewMatch() {
    matches.add(new Match(plugin, Bukkit.getWorld("world_the_end"))); // FIXME: UnHard-code this
    logger.info("Match " + matches.size() + " awaiting command in state IDLE.");
  }

  public ImmutableList<Match> getMatches() {
    return ImmutableList.copyOf(matches);
  }

  public Match getCurrentMatch() {
    return matches.get(matches.size() - 1);
  }

  public ImmutableSet<Player> getCurrentMatchPlayers() {
    return getCurrentMatch().getPlayers();
  }

  @EventHandler
  public void onMatchFinish(MatchFinishEvent event) {
    Bukkit.broadcastMessage("Duración: " + event.getMatch().getDuration().toString());
    prepareNewMatch();
  }
}
