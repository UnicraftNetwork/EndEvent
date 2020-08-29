package cl.bgmp.endevent.events;

import cl.bgmp.endevent.match.Match;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MatchFinishEvent extends Event {
  private static final HandlerList handlerList = new HandlerList();
  private Match match;

  public MatchFinishEvent(Match match) {
    this.match = match;
  }

  public Match getMatch() {
    return match;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }
}
