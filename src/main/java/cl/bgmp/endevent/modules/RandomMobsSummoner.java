package cl.bgmp.endevent.modules;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public abstract class RandomMobsSummoner {
  // SLIMES
  private Set<EntityType> SLIME_TYPES =
      ImmutableSet.<EntityType>builder().add(EntityType.SLIME, EntityType.MAGMA_CUBE).build();

  // MISC
  private Set<EntityType> MISC_TYPES =
      ImmutableSet.<EntityType>builder()
          .add(EntityType.PRIMED_TNT)
          .add(EntityType.FALLING_BLOCK)
          .add(EntityType.BOAT)
          .add(EntityType.MINECART)
          .add(EntityType.MINECART_CHEST)
          .add(EntityType.MINECART_COMMAND)
          .add(EntityType.MINECART_FURNACE)
          .add(EntityType.MINECART_HOPPER)
          .add(EntityType.MINECART_MOB_SPAWNER)
          .add(EntityType.MINECART_TNT)
          .build();

  // PROJECTILES
  private Set<EntityType> PROJECTILE_TYPES =
      ImmutableSet.<EntityType>builder()
          .add(EntityType.EGG)
          .add(EntityType.ARROW)
          .add(EntityType.SNOWBALL)
          .add(EntityType.FIREBALL)
          .add(EntityType.SMALL_FIREBALL)
          .add(EntityType.ENDER_PEARL)
          .add(EntityType.SPLASH_POTION)
          .add(EntityType.WITHER_SKULL)
          .add(EntityType.FIREWORK)
          .add(EntityType.SPECTRAL_ARROW)
          .add(EntityType.SHULKER_BULLET)
          .add(EntityType.DRAGON_FIREBALL)
          .add(EntityType.THROWN_EXP_BOTTLE)
          .build();

  // MOBS
  private Set<EntityType> MOB_TYPES =
      ImmutableSet.<EntityType>builder()
          .add(EntityType.ELDER_GUARDIAN)
          .add(EntityType.WITHER_SKELETON)
          .add(EntityType.STRAY)
          .add(EntityType.HUSK)
          .add(EntityType.ZOMBIE_VILLAGER)
          .add(EntityType.SKELETON_HORSE)
          .add(EntityType.ZOMBIE_HORSE)
          .add(EntityType.DONKEY)
          .add(EntityType.MULE)
          .add(EntityType.EVOKER)
          .add(EntityType.VEX)
          .add(EntityType.VINDICATOR)
          .add(EntityType.ILLUSIONER)
          .add(EntityType.CREEPER)
          .add(EntityType.SKELETON)
          .add(EntityType.SPIDER)
          .add(EntityType.GIANT)
          .add(EntityType.ZOMBIE)
          .add(EntityType.GHAST)
          .add(EntityType.ZOMBIFIED_PIGLIN)
          .add(EntityType.ENDERMAN)
          .add(EntityType.CAVE_SPIDER)
          .add(EntityType.SILVERFISH)
          .add(EntityType.BLAZE)
          .add(EntityType.BAT)
          .add(EntityType.WITCH)
          .add(EntityType.BAT)
          .add(EntityType.ENDERMITE)
          .add(EntityType.GUARDIAN)
          .add(EntityType.SHULKER)
          .add(EntityType.PIG)
          .add(EntityType.SHEEP)
          .add(EntityType.COW)
          .add(EntityType.CHICKEN)
          .add(EntityType.SQUID)
          .add(EntityType.WOLF)
          .add(EntityType.MUSHROOM_COW)
          .add(EntityType.SNOWMAN)
          .add(EntityType.OCELOT)
          .add(EntityType.IRON_GOLEM)
          .add(EntityType.HORSE)
          .add(EntityType.RABBIT)
          .add(EntityType.POLAR_BEAR)
          .add(EntityType.LLAMA)
          .add(EntityType.PARROT)
          .add(EntityType.VILLAGER)
          .add(EntityType.TURTLE)
          .add(EntityType.PHANTOM)
          .add(EntityType.COD)
          .add(EntityType.SALMON)
          .add(EntityType.PUFFERFISH)
          .add(EntityType.TROPICAL_FISH)
          .add(EntityType.DROWNED)
          .add(EntityType.DOLPHIN)
          .add(EntityType.CAT)
          .add(EntityType.PANDA)
          .add(EntityType.PILLAGER)
          .add(EntityType.TRADER_LLAMA)
          .add(EntityType.WANDERING_TRADER)
          .add(EntityType.FOX)
          .add(EntityType.BEE)
          .add(EntityType.HOGLIN)
          .add(EntityType.PIGLIN)
          .add(EntityType.STRIDER)
          .add(EntityType.ZOGLIN)
          .build();

  private Map<SpawnType, Set<EntityType>> entitySetsMap =
      ImmutableMap.<SpawnType, Set<EntityType>>builder()
          .put(SpawnType.SLIME, SLIME_TYPES)
          .put(SpawnType.MISC, MISC_TYPES)
          .put(SpawnType.PROJECTILE, PROJECTILE_TYPES)
          .put(SpawnType.MOB, MOB_TYPES)
          .build();
  private World world;

  public RandomMobsSummoner(World world) {
    this.world = world;
  }

  protected void generateRandomStuffAround(ImmutableSet<Player> players) {
    Random random = new Random();

    Iterator<Player> iterator = players.stream().iterator();
    while (iterator.hasNext()) {
      Player player = iterator.next();
      int numberOfEntities = random.nextInt(5);

      for (int j = 0; numberOfEntities > j; j++) {
        SpawnType randomSpawnType = getRandomEnum(SpawnType.class);

        int randomXOffset = random.nextInt(8) - 8;
        int randomYOffset = random.nextInt(5);
        int randomZOffset = random.nextInt(8) - 8;

        Location playerLocation = player.getLocation();
        Location around =
            new Location(
                world,
                playerLocation.getX() + randomXOffset,
                playerLocation.getY() + randomYOffset,
                playerLocation.getZ() + randomZOffset);
        Set<EntityType> entityTypesSet = entitySetsMap.get(randomSpawnType);

        around.getWorld().spawnEntity(around, getRandomElement(entityTypesSet));
        switch (randomSpawnType) {
          case MOB:
            break;
          case MISC:
            break;
          case SLIME:
            break;
          case PROJECTILE:
            break;
          default:
            break;
        }
      }
    }
  }

  private static <T extends Enum<?>> T getRandomEnum(Class<T> clazz) {
    int i = new Random().nextInt(clazz.getEnumConstants().length);
    return clazz.getEnumConstants()[i];
  }

  public static <T> T getRandomElement(Collection<T> coll) {
    int num = (int) (Math.random() * coll.size());
    for (T t : coll) if (--num < 0) return t;
    throw new AssertionError();
  }

  public enum SpawnType {
    SLIME,
    MISC,
    PROJECTILE,
    MOB
  }
}
