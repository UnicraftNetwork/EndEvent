package cl.bgmp.endevent.modules;

import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.match.Match;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class BanWithersMatchModule implements MatchModule, Listener {
  private Match match;

  public BanWithersMatchModule(Match match) {
    this.match = match;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onWitherSpawn(EntitySpawnEvent event) {
    if (event.getEntity() instanceof Wither) event.setCancelled(true);
  }

  @Override
  public void enable() {
    EndEvent.get().registerEvents(this);
  }

  @Override
  public void disable() {
    EndEvent.get().unregisterEvents(this);
  }
}
