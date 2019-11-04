package xuw220.cse298.lehigh.edu.androidunlock;

import java.util.Date;

class GameData {
    private int gameId;
    private int highScore;
    private String userName;
    private Date highScoreDate;

    public GameData(int gameId, int highScore, String userName, Date highScoreDate){
        this.gameId = gameId;
        this.highScore = highScore;
        this.userName = userName;
        this.highScoreDate = highScoreDate;
    }

    public int getGameId() {
        return gameId;
    }

    public String getUserName() {
        return userName;
    }

    public int getHighScore() {
        return highScore;
    }

    public Date getHighScoreDate() {
        return highScoreDate;
    }

}
