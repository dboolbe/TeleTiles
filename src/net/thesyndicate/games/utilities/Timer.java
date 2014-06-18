package net.thesyndicate.games.utilities;

import javax.swing.*;

/**
 * Created by dboolbe on 6/16/14.
 */
public class Timer implements Runnable {

    long totalTime = 0L;
    long oldTime = System.currentTimeMillis();
    boolean running = true;

    JLabel timeLabel;

    public Timer(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }

    @Override
    public void run() {
        while (true)
            while (running) {
                long newTime = System.currentTimeMillis();
                totalTime += (newTime - oldTime);
                oldTime = newTime;

                timeLabel.setText("Time Elapsed " + convertToTimeString(totalTime));
            }
    }

    public static String convertToTimeString(long time) {
        long remainingTime = time;
        long milliseconds = time % 1000;
        remainingTime -= milliseconds;
        long seconds = (remainingTime / 1000) % 60;
        remainingTime -= seconds * 1000;
        long minutes = (remainingTime / (1000 * 60)) % 60;
        remainingTime -= minutes * 1000 * 60;
        long hours = (remainingTime / (1000 * 60 * 60)) % 60;
        // prints out the times as such 000:00:00.000
//        return String.format("%03d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
        // prints out the times as such 000:00:00
        return String.format("%03d:%02d:%02d", hours, minutes, seconds);
    }

    public void resetTime() {
        totalTime = 0L;
    }

    public void resumeTime() {
        running = true;
    }

    public void pauseTime() {
        running = false;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public String getTotalTimeString() {
        return convertToTimeString(totalTime);
    }
}
