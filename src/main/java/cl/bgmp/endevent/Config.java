package cl.bgmp.endevent;

import java.util.Set;
import org.bukkit.entity.EntityType;

public interface Config {
  boolean bedsAreAllowed();

  boolean crystalsAreAllowed();

  Set<EntityType> getAllowedProjectiles();
}
