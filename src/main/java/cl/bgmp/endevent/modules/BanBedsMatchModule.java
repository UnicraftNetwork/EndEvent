package cl.bgmp.endevent.modules;

import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.match.Match;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BanBedsMatchModule implements MatchModule, Listener {
  private Match match;

  public BanBedsMatchModule(Match match) {
    this.match = match;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBedPlace(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    if (!match.getPlayers().contains(player)) return;

    Block block = event.getBlock();
    if (block.getType().toString().contains("BED")) event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBedPlace(BlockMultiPlaceEvent event) {
    Player player = event.getPlayer();
    if (!match.getPlayers().contains(player)) return;

    Block block = event.getBlock();
    if (block.getType().toString().contains("BED")) event.setCancelled(true);
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
