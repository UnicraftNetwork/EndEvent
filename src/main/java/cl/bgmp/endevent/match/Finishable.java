package cl.bgmp.endevent.match;

/**
 * Represents a functional implementation of being able to perform a method body at any given point.
 */
@FunctionalInterface
public interface Finishable {
  void whenFinished();
}
