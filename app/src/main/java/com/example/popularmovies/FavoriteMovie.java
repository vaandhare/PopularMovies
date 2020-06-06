package com.example.popularmovies;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FavMovies")
public class FavoriteMovie {

    @PrimaryKey
    private int id;
    private String title;
    private String plot;
    private String releaseDate;
    private String averageVotes;
    private String posterPath;

    public FavoriteMovie(int id, String title, String plot, String releaseDate, String averageVotes, String posterPath) {
        this.id = id;
        this.title = title;
        this.plot = plot;
        this.releaseDate = releaseDate;
        this.averageVotes = averageVotes;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAverageVotes() {
        return averageVotes;
    }

    public void setAverageVotes(String averageVotes) {
        this.averageVotes = averageVotes;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
