package com.bgp.seosumsan.DTO;

public class ReviewData {
    private String name;
    private int evaluation;
    private String review;

    public String getName(){return name;}
    public void setName(String name) { this.name = name;}

    public int getEvaluation() { return evaluation;}
    public void setEvaluation(int evaluation) { this.evaluation = evaluation;}

    public String getReview() {return review;}
    public void setReview(String review){this.review = review;}
}
