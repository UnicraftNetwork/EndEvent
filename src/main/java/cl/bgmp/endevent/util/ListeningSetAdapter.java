package cl.bgmp.endevent.util;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class ListeningSetAdapter<E> extends ForwardingSet<E> implements Listener {
  protected final Plugin plugin;
  protected final Set<E> set;
  protected boolean enabled;
  protected boolean lazyEnable = false;

  public ListeningSetAdapter(Plugin plugin) {
    this(plugin, Sets.newHashSet());
  }

  public ListeningSetAdapter(Plugin plugin, Set<E> set) {
    this.plugin = plugin;
    this.set = set;
  }

  protected void assertEnabled() {
    if (!this.enabled) {
      if (this.lazyEnable) {
        this.enable();
      } else {
        throw new IllegalStateException(
            "This " + this.getClass().getSimpleName() + " is not enabled");
      }
    }
  }

  /**
   * If the given element is valid, add it to the list and return true If the entry is not valid, no
   * change is made to the list and false is returned.
   */
  @Override
  public boolean add(@NotNull E element) {
    this.assertEnabled();
    if (isValid(element)) {
      this.set.add(element);
      return true;
    } else return false;
  }

  /** If the element is a valid new element */
  public abstract boolean isValid(E element);

  /** Add an element to the list for the given key and return true, or false if unable to add */
  public boolean force(E element) {
    this.assertEnabled();
    return this.set.add(element);
  }

  @Override
  public boolean addAll(@NotNull Collection<? extends E> elements) {
    if (set == null) return false;
    else {
      for (E element : elements) {
        add(element);
      }
      return true;
    }
  }

  /** Special case of putAll that does not check if each entry is valid */
  public void putAll(ListeningSetAdapter<? extends E> otherSet) {
    this.assertEnabled();
    this.addAll(otherSet);
  }

  /** Register to receive events. This must be called before adding any entries to the set. */
  public void enable() {
    if (!this.enabled) {
      this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.enabled = true;
    }
  }

  /**
   * Clear the set and stop listening for events. This list should not be used after this method is
   * called.
   */
  public void disable() {
    if (this.enabled) {
      this.clear();
      HandlerList.unregisterAll(this);
    }
  }

  /**
   * Return an immutable copy of this list that is safe to iterate over while the list is modified,
   * which tends to happen unexpectedly from events the list is listening to.
   */
  public ImmutableSet<E> copy() {
    return ImmutableSet.copyOf(set);
  }

  @Override
  protected Set<E> delegate() {
    return this.set;
  }
}
