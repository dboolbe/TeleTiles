package net.thesyndicate.games.utilities;

import java.io.Serializable;

/**
 * Created by dboolbe on 6/16/14.
 */
public class HighScore implements Serializable, Comparable<HighScore> {
    String username;
    Integer moves;
    Long elapsedTime;

    public HighScore(String username, int moves, long elapsedTime) {
        this.username = username;
        this.moves = moves;
        this.elapsedTime = elapsedTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    @Override
    public String toString() {
        return new StringBuffer(" Username : ")
                .append(username)
                .append(" Moves : ")
                .append(moves)
                .append(" ElapsedTime : ")
                .append(elapsedTime).toString();
    }

    @Override
    public int compareTo(HighScore highScore) {
        if (moves.compareTo(highScore.moves) == 0)
            return elapsedTime.compareTo(highScore.elapsedTime);
        return moves.compareTo(highScore.moves);
    }
}
