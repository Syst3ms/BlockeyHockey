package io.github.syst3ms.blockyhockey.game.timer;

public abstract class Timer {
	protected int minutes, seconds, tenths;

	public abstract boolean advanceTime();

	public abstract String getTimeString();
}
