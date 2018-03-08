package io.github.syst3ms.blockyhockey.game;

public class Timer {
    private int minutes, seconds, tenths;

    public Timer(boolean overtime) {
        this(overtime ? 3 : 5, 0, 0);
    }

    public Timer(int minutes, int seconds, int tenths) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.tenths = tenths;
    }

    public boolean advanceTime() {
        tenths--;
        if (tenths == -1) {
            tenths = 9;
            seconds--;
        }
        if (seconds == -1) {
            seconds = 59;
            minutes--;
        }
        return minutes + seconds + tenths == 0;
    }

    public String getTimeString() {
        if (minutes == 0) {
            return String.format("%d.%d", seconds, tenths);
        } else {
            return String.format("%d:%02d", minutes, seconds);
        }
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setTenths(int tenths) {
        this.tenths = tenths;
    }
}
