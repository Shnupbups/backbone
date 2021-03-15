package dev.vatuu.backbone.events.base;

public interface Cancellable {
	boolean isCancelled();
	void setCancelled(boolean cancelled);
	default void cancel() {
		setCancelled(true);
	}
}
