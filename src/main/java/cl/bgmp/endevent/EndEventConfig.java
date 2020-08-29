package cl.bgmp.endevent;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

public final class EndEventConfig implements Config {
  private FileConfiguration config;
  private File datafolder;
  private Set<EntityType> allowedProjectiles;
  private boolean allowBeds;
  private boolean allowCrystals;

  public EndEventConfig(FileConfiguration config, File datafolder) {
    this.config = config;
    this.datafolder = datafolder;
    this.allowedProjectiles =
        config.getStringList("projectiles.allowed").stream()
            .map(EntityType::valueOf)
            .collect(Collectors.toSet());
    this.allowBeds = config.getBoolean("beds");
    this.allowCrystals = config.getBoolean("crystals");
  }

  @Override
  public boolean bedsAreAllowed() {
    return allowBeds;
  }

  @Override
  public boolean crystalsAreAllowed() {
    return allowCrystals;
  }

  @Override
  public Set<EntityType> getAllowedProjectiles() {
    return allowedProjectiles;
  }
}
