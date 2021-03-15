package dev.vatuu.backbone.events.base;

public interface Cancellable {
	boolean isCancelled();
	void cancel();
}
