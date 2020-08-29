package cl.bgmp.endevent.modules;

import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.match.Match;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectilesMatchModule implements MatchModule, Listener {
  private Match match;

  public ProjectilesMatchModule(Match match) {
    this.match = match;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onProjectileLaunch(ProjectileLaunchEvent event) {
    World.Environment environment = event.getEntity().getWorld().getEnvironment();
    if (environment != World.Environment.THE_END) return;

    event.setCancelled(!canThrow(event.getEntityType()));
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEntityShootBow(EntityShootBowEvent event) {
    LivingEntity shooter = event.getEntity();
    if (!(shooter instanceof Player)) return;
    if (!match.getPlayers().contains(shooter)) return;

    event.setCancelled(!canThrow(event.getEntityType()));
  }

  private boolean canThrow(EntityType projectileType) {
    return EndEvent.get().getConfiguration().getAllowedProjectiles().contains(projectileType);
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
