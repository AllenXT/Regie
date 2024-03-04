package org.courseRegistration.util;

public enum Grade {
    A("A", 90, 100),
    B("B", 80, 89),
    C("C", 70, 79),
    D("D", 60, 69),
    F("F", 0, 59);

    private final String description;
    private final int minScore;
    private final int maxScore;

    // constructor
    Grade(String description, int minScore, int maxScore) {
        this.description = description;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public String getDescription() {
        return description;
    }

    public int getMinScore() {
        return minScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public static Grade getGradeByScore(int score) {
        for (Grade grade : Grade.values()) {
            if (score >= grade.getMinScore() && score <= grade.getMaxScore()) {
                return grade;
            }
        }
        return F; // default is F
    }
}
