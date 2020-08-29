package cl.bgmp.endevent.match;

import cl.bgmp.endevent.EndEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a countdown which will broadcast its elapsed progress in spans of 1 minute, precising
 * to spans of 10 seconds only when under 60 seconds, and of 1 when under 10
 */
public abstract class Countdown implements Finishable {
  private Instant start;
  private Instant end;
  private String timerMessage;
  private String endMessage;
  private String cancelMessage;
  private BukkitRunnable broadcastTask;
  private Duration duration;
  private boolean includeZero = false;

  public Countdown(
      @NotNull Duration duration,
      @NotNull String timerMessage,
      @NotNull String endMessage,
      @NotNull String cancelMessage) {
    this.duration = duration;
    this.timerMessage = timerMessage;
    this.endMessage = endMessage;
    this.cancelMessage = cancelMessage;
  }

  public Countdown(
      @NotNull Duration duration,
      boolean includeZero,
      @NotNull String timerMessage,
      @NotNull String endMessage,
      @NotNull String cancelMessage) {
    this.duration = duration;
    this.includeZero = includeZero;
    this.timerMessage = timerMessage;
    this.endMessage = endMessage;
    this.cancelMessage = cancelMessage;
  }

  public void startTimer() {
    start = Instant.now();
    end = start.plus(duration);

    this.broadcastTask = buildBroadcastTask();
    broadcastTask.runTaskTimer(EndEvent.get(), 0L, 20L);
  }

  public void cancel() {
    broadcastTask.cancel();
    Bukkit.broadcastMessage(cancelMessage);
  }

  public void end() {
    end = Instant.now();
  }

  // FIXME: Probably improve this mess
  private BukkitRunnable buildBroadcastTask() {
    return new BukkitRunnable() {
      boolean first = true;
      Duration remaining;
      final long[] oneToFive = {1, 2, 3, 4, 5};

      @Override
      public void run() {
        remaining = Duration.between(Instant.now(), end);
        if (remaining.isZero() || remaining.isNegative()) {
          Bukkit.broadcastMessage(endMessage);
          whenFinished();
          this.cancel();
        } else {
          long seconds = remaining.getSeconds();
          long toBroadcast = -1;

          if (remaining.toMinutes() > 0 && seconds % 60 == 0) {
            toBroadcast = remaining.toMinutes();
          } else {
            if (seconds % 10 == 0
                || Arrays.stream(oneToFive)
                    .asDoubleStream()
                    .anyMatch(withinOneAndFive -> withinOneAndFive == seconds)) {
              toBroadcast = seconds;
            }
          }

          if (first) {
            first = false;
            toBroadcast = seconds + 1;
          }

          if (toBroadcast != -1)
            Bukkit.broadcastMessage(
                timerMessage.replace(
                    "{0}", toBroadcast + (toBroadcast == 1 ? " second" : " seconds")));
        }
      }
    };
  }

  @Override
  public abstract void whenFinished();
}
