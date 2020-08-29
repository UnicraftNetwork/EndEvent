package cl.bgmp.endevent.modules;

import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.match.Match;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class BanEndCrystalsMatchModule implements MatchModule, Listener {
  private Match match;

  public BanEndCrystalsMatchModule(Match match) {
    this.match = match;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onCrystalPlace(EntitySpawnEvent event) {
    World.Environment environment = event.getEntity().getWorld().getEnvironment();
    if (environment != World.Environment.THE_END) return;

    if (event.getEntity() instanceof EnderCrystal
        && event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
      event.setCancelled(true);
    }
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
