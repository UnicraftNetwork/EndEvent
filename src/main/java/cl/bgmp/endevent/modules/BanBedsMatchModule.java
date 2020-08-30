package cl.bgmp.endevent.modules;

import cl.bgmp.endevent.EndEvent;
import cl.bgmp.endevent.match.Match;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BanBedsMatchModule implements MatchModule, Listener {
  private Match match;
  private List<Material> beds =
      ImmutableList.<Material>builder()
          .add(Material.BLACK_BED)
          .add(Material.WHITE_BED)
          .add(Material.ORANGE_BED)
          .add(Material.LIGHT_BLUE_BED)
          .add(Material.YELLOW_BED)
          .add(Material.LIME_BED)
          .add(Material.PINK_BED)
          .add(Material.LIGHT_GRAY_BED)
          .add(Material.CYAN_BED)
          .add(Material.PURPLE_BED)
          .add(Material.BLUE_BED)
          .add(Material.BROWN_BED)
          .add(Material.GREEN_BED)
          .add(Material.RED_BED)
          .add(Material.LEGACY_BED)
          .add(Material.LEGACY_BED_BLOCK)
          .build();

  public BanBedsMatchModule(Match match) {
    this.match = match;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBedPlace(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    if (!match.getPlayers().contains(player)) return;

    Block block = event.getBlock();
    if (beds.contains(block.getType())) event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBedPlace(BlockMultiPlaceEvent event) {
    Player player = event.getPlayer();
    if (!match.getPlayers().contains(player)) return;

    Block block = event.getBlock();
    if (beds.contains(block.getType())) event.setCancelled(true);
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
