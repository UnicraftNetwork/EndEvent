package cl.bgmp.endevent.match;

import cl.bgmp.endevent.Config;
import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.events.MatchFinishEvent;
import cl.bgmp.endevent.events.MatchStartEvent;
import cl.bgmp.endevent.modules.BanBedsMatchModule;
import cl.bgmp.endevent.modules.BanEndCrystalsMatchModule;
import cl.bgmp.endevent.modules.BanWithersMatchModule;
import cl.bgmp.endevent.modules.EnderDragonMatchModule;
import cl.bgmp.endevent.modules.MatchModule;
import cl.bgmp.endevent.modules.ProjectilesMatchModule;
import cl.bgmp.endevent.modules.RandomMobsMatchModule;
import cl.bgmp.endevent.util.ListeningSetAdapter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/** Represents an EndEvent Match */
// TODO: Add observers
public class Match extends ListeningSetAdapter<Player> implements Listener {
  private UUID uuid = UUID.randomUUID();
  private MatchState state = MatchState.IDLE;
  private Set<MatchModule> modules = Sets.newHashSet();
  private World endWorld;
  private Instant start;
  private Instant end;

  public Match(Plugin plugin, World endWorld) {
    super(plugin);

    this.endWorld = endWorld;

    final Config config = EndEvent.get().getConfiguration();

    addModules(
        new ProjectilesMatchModule(this),
        new EnderDragonMatchModule(this),
        new RandomMobsMatchModule(this.plugin, this));
    if (!config.withersAreAllowed()) addModules(new BanWithersMatchModule(this));
    if (!config.bedsAreAllowed()) addModules(new BanBedsMatchModule(this));
    if (!config.crystalsAreAllowed()) addModules(new BanEndCrystalsMatchModule(this));
  }

  public World getEndWorld() {
    return endWorld;
  }

  @Override
  public boolean isValid(Player participant) {
    return participant.getWorld().getEnvironment()
        == World.Environment.THE_END; // FIXME: check for this.getEndWorld() instead
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    if (player.getWorld().getEnvironment() == World.Environment.THE_END)
      this.add(player); // FIXME: check for this.getEndWorld() instead
    else this.remove(player);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent event) {
    this.add(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerQuit(PlayerQuitEvent event) {
    this.remove(event.getPlayer());
  }

  public void startIn(@NotNull Duration duration) {
    this.state = MatchState.STARTING;
    new Countdown(
        duration,
        "EndEvent match starting in {0}.",
        "EndEvent match started!",
        "EndEvent start cancelled.") {
      @Override
      public void whenFinished() {
        enable();
        addAll(Bukkit.getOnlinePlayers());
        start();
      }
    }.startTimer();
  }

  public void start() {
    this.start = Instant.now();
    this.modules.forEach(MatchModule::enable);
    this.state = MatchState.PLAYING;

    Bukkit.getPluginManager().callEvent(new MatchStartEvent(this));
  }

  public void end(String endMessage) {
    Bukkit.getPluginManager().callEvent(new MatchFinishEvent(this));

    this.disable();
    this.end = Instant.now();
    this.modules.forEach(MatchModule::disable);
    this.state = MatchState.FINISHED;
    Bukkit.broadcastMessage(endMessage);
  }

  public MatchState getState() {
    return state;
  }

  public ImmutableSet<Player> getPlayers() {
    return this.copy();
  }

  public Duration getDuration() {
    return Duration.between(start, end);
  }

  private void addModules(MatchModule... matchModules) {
    this.modules.addAll(Arrays.asList(matchModules));
  }
}
