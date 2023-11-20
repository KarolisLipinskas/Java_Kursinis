package model;

public class Review extends Comment {
    private double ratingAverage;
    private int ratingSum;
    private int ratingCount;

    public Review(String text, int rating) {
        super(text);
        this.ratingSum = rating;
        this.ratingCount = 1;
        this.ratingAverage = rating;
    }

    public double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public void addRating(int rating) {
        this.ratingCount++;
        this.ratingSum += rating;
        this.ratingAverage = (double) this.ratingSum / this.ratingCount;
    }
}
