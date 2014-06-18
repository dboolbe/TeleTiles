package net.thesyndicate.games.utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by dboolbe on 6/16/14.
 */
public class HighScoreTable implements Serializable {

    ArrayList<HighScore> highScoreArrayEasy;
    ArrayList<HighScore> highScoreArrayMedium;
    ArrayList<HighScore> highScoreArrayHard;
    ArrayList<HighScore> highScoreArrayEasyBlank;
    ArrayList<HighScore> highScoreArrayMediumBlank;
    ArrayList<HighScore> highScoreArrayHardBlank;

    public HighScoreTable() {
        highScoreArrayEasy = new ArrayList<HighScore>();
        highScoreArrayMedium = new ArrayList<HighScore>();
        highScoreArrayHard = new ArrayList<HighScore>();
        highScoreArrayEasyBlank = new ArrayList<HighScore>();
        highScoreArrayMediumBlank = new ArrayList<HighScore>();
        highScoreArrayHardBlank = new ArrayList<HighScore>();
    }

    public boolean isEasyHighScore(HighScore score) {
        if (highScoreArrayEasy.size() < 5)
            return true;
        return (score.compareTo(highScoreArrayEasy.get(4)) < 0);
    }

    public void addEasyHighScore(HighScore score) {
        highScoreArrayEasy.add(score);
        Collections.sort(highScoreArrayEasy);

        if (highScoreArrayEasy.size() > 5)
            highScoreArrayEasy.remove(5);
    }

    public boolean isMediumHighScore(HighScore score) {
        if (highScoreArrayMedium.size() < 5)
            return true;
        return (score.compareTo(highScoreArrayMedium.get(4)) < 0);
    }

    public void addMediumHighScore(HighScore score) {
        highScoreArrayMedium.add(score);
        Collections.sort(highScoreArrayMedium);

        if (highScoreArrayMedium.size() > 5)
            highScoreArrayMedium.remove(5);
    }

    public boolean isHardHighScore(HighScore score) {
        if (highScoreArrayHard.size() < 5)
            return true;
        return (score.compareTo(highScoreArrayHard.get(4)) < 0);
    }

    public void addHardHighScore(HighScore score) {
        highScoreArrayHard.add(score);
        Collections.sort(highScoreArrayHard);

        if (highScoreArrayHard.size() > 5)
            highScoreArrayHard.remove(5);
    }

    public boolean isEasyBlankHighScore(HighScore score) {
        if (highScoreArrayEasyBlank.size() < 5)
            return true;
        return (score.compareTo(highScoreArrayEasyBlank.get(4)) < 0);
    }

    public void addEasyBlankHighScore(HighScore score) {
        highScoreArrayEasyBlank.add(score);
        Collections.sort(highScoreArrayEasyBlank);

        if (highScoreArrayEasyBlank.size() > 5)
            highScoreArrayEasyBlank.remove(5);
    }

    public boolean isMediumBlankHighScore(HighScore score) {
        if (highScoreArrayMediumBlank.size() < 5)
            return true;
        return (score.compareTo(highScoreArrayMediumBlank.get(4)) < 0);
    }

    public void addMediumBlankHighScore(HighScore score) {
        highScoreArrayMediumBlank.add(score);
        Collections.sort(highScoreArrayMediumBlank);

        if (highScoreArrayMediumBlank.size() > 5)
            highScoreArrayMediumBlank.remove(5);
    }

    public boolean isHardBlankHighScore(HighScore score) {
        if (highScoreArrayHardBlank.size() < 5)
            return true;
        return (score.compareTo(highScoreArrayHardBlank.get(4)) < 0);
    }

    public void addHardBlankHighScore(HighScore score) {
        highScoreArrayHardBlank.add(score);
        Collections.sort(highScoreArrayHardBlank);

        if (highScoreArrayHardBlank.size() > 5)
            highScoreArrayHardBlank.remove(5);
    }

    public ArrayList<HighScore> getHighScoreArrayEasy() {
        return highScoreArrayEasy;
    }

    public void setHighScoreArrayEasy(ArrayList<HighScore> highScoreArrayEasy) {
        this.highScoreArrayEasy = highScoreArrayEasy;
    }

    public ArrayList<HighScore> getHighScoreArrayMedium() {
        return highScoreArrayMedium;
    }

    public void setHighScoreArrayMedium(ArrayList<HighScore> highScoreArrayMedium) {
        this.highScoreArrayMedium = highScoreArrayMedium;
    }

    public ArrayList<HighScore> getHighScoreArrayHard() {
        return highScoreArrayHard;
    }

    public void setHighScoreArrayHard(ArrayList<HighScore> highScoreArrayHard) {
        this.highScoreArrayHard = highScoreArrayHard;
    }

    public ArrayList<HighScore> getHighScoreArrayEasyBlank() {
        return highScoreArrayEasyBlank;
    }

    public void setHighScoreArrayEasyBlank(ArrayList<HighScore> highScoreArrayEasyBlank) {
        this.highScoreArrayEasyBlank = highScoreArrayEasyBlank;
    }

    public ArrayList<HighScore> getHighScoreArrayMediumBlank() {
        return highScoreArrayMediumBlank;
    }

    public void setHighScoreArrayMediumBlank(ArrayList<HighScore> highScoreArrayMediumBlank) {
        this.highScoreArrayMediumBlank = highScoreArrayMediumBlank;
    }

    public ArrayList<HighScore> getHighScoreArrayHardBlank() {
        return highScoreArrayHardBlank;
    }

    public void setHighScoreArrayHardBlank(ArrayList<HighScore> highScoreArrayHardBlank) {
        this.highScoreArrayHardBlank = highScoreArrayHardBlank;
    }
}
