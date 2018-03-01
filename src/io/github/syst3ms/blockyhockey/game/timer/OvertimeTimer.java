package io.github.syst3ms.blockyhockey.game.timer;

import java.util.Arrays;

public class OvertimeTimer extends Timer {
	public static final int[] OVERTIME_PERIOD_LIMIT = {2, 0, 0};

	@Override
	public boolean advanceTime() {
		tenths++;
		if (tenths == 10) {
			tenths = 0;
			seconds++;
		}
		if (seconds == 60) {
			seconds = 0;
			minutes++;
		}
		return Arrays.equals(new int[]{minutes, seconds, tenths}, OVERTIME_PERIOD_LIMIT);
	}

	@Override
	public String getTimeString() {
		if (minutes < 2) {
			return String.format("%02d:%02d", minutes, seconds);
		} else {
			return String.format("%02d:%02d.%d", minutes, seconds, tenths);
		}
	}
}
