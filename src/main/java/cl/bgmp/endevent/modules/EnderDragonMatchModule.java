package cl.bgmp.endevent.modules;

import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.events.MatchStartEvent;
import cl.bgmp.endevent.match.Match;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

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

  @SuppressWarnings("deprecation")
  private void modifyEventDragon() {
    this.enderDragon.setCustomName(ChatColor.RED + "Event Dragon");
    Bukkit.broadcastMessage("Vida antes: " + enderDragon.getHealth());
    this.enderDragon.setMaxHealth(enderDragon.getHealth() * 3);
    this.enderDragon.setHealth(enderDragon.getMaxHealth());
    Bukkit.broadcastMessage("Vida despues: " + enderDragon.getHealth());

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
