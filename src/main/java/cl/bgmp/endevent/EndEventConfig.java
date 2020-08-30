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
  private boolean allowWithers;
  private boolean allowBeds;
  private boolean allowCrystals;

  public EndEventConfig(FileConfiguration config, File datafolder) {
    this.config = config;
    this.datafolder = datafolder;
    this.allowWithers = config.getBoolean("withers");
    this.allowedProjectiles =
        config.getStringList("allowed-projectiles").stream()
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
  public boolean withersAreAllowed() {
    return allowWithers;
  }

  @Override
  public Set<EntityType> getAllowedProjectiles() {
    return allowedProjectiles;
  }
}
