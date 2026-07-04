package io.github.isquyet.mindyourbruises.client.render;

public final class DamageOverlayTextureRefresher {
	private static Runnable refreshAction = () -> {
	};

	private DamageOverlayTextureRefresher() {
	}

	public static void setRefreshAction(Runnable refreshAction) {
		DamageOverlayTextureRefresher.refreshAction = refreshAction == null ? () -> {
		} : refreshAction;
	}

	public static void refresh() {
		refreshAction.run();
	}
}
