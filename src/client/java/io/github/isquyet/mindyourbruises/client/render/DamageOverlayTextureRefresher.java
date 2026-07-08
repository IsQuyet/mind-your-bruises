package io.github.isquyet.mindyourbruises.client.render;

public final class DamageOverlayTextureRefresher {
	private static final Runnable NO_OP_REFRESH_ACTION = () -> {
	};

	private static Runnable refreshAction = NO_OP_REFRESH_ACTION;

	private DamageOverlayTextureRefresher() {
	}

	public static void setRefreshAction(Runnable refreshAction) {
		DamageOverlayTextureRefresher.refreshAction = refreshAction == null ? NO_OP_REFRESH_ACTION : refreshAction;
	}

	public static void refresh() {
		refreshAction.run();
	}
}
