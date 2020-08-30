package cl.bgmp.endevent.modules;

import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.events.MatchStartEvent;
import cl.bgmp.endevent.match.Match;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EnderDragonMatchModule implements MatchModule, Listener {
  private Match match;
  private EnderDragon enderDragon;

  public EnderDragonMatchModule(Match match) {
    this.match = match;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onMatchStart(MatchStartEvent event) {
    World endWorld = match.getEndWorld();
    this.enderDragon =
        endWorld.getEntities().stream()
            .filter(e -> e instanceof EnderDragon)
            .map(e -> (EnderDragon) e)
            .findFirst()
            .orElse(null);
    if (this.enderDragon == null) return;

    modifyEventDragon();
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onDragonDeath(EntityDeathEvent event) {
    Entity entity = event.getEntity();
    if (!(entity instanceof EnderDragon)) return;

    EnderDragon enderDragon = (EnderDragon) entity;
    Player killer = enderDragon.getKiller();

    if (killer != null) {
      this.match.end("The EndEvent has finished. Winner: " + killer.getDisplayName());
    } else {
      this.match.end("The EndEvent has finished. The Dragon died for unknown reasons.");
    }
  }

  @SuppressWarnings("deprecation")
  private void modifyEventDragon() {
    this.enderDragon.setCustomName(ChatColor.RED + "Event Dragon");
    this.enderDragon.setMaxHealth(enderDragon.getHealth() * 3);
    this.enderDragon.setHealth(enderDragon.getMaxHealth());

    if (enderDragon.getBossBar() == null) return;

    this.enderDragon.getBossBar().setStyle(BarStyle.SEGMENTED_20);
    this.enderDragon.getBossBar().setTitle(this.enderDragon.getCustomName());
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
