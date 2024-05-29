package com.example.quizapp.models;

import java.util.List;

public class ApiResponse {
    private int response_code;
    private List<Result> results;

    public ApiResponse(int response_code, List<Result> results) {
        this.response_code = response_code;
        this.results = results;
    }

    public int getResponseCode() {
        return response_code;
    }

    public void setResponseCode(int response_code) {
        this.response_code = response_code;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public static class Result {

        private String type;
        private String difficulty;
        private String category;
        private String question;
        private String correct_answer;
        private List<String> incorrect_answers;

        public Result(String type, String difficulty, String category, String question, String correct_answer, List<String> incorrect_answers) {
            this.type = type;
            this.difficulty = difficulty;
            this.category = category;
            this.question = question;
            this.correct_answer = correct_answer;
            this.incorrect_answers = incorrect_answers;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getCorrectAnswer() {
            return correct_answer;
        }

        public void setCorrectAnswer(String correct_answer) {
            this.correct_answer = correct_answer;
        }

        public List<String> getIncorrectAnswers() {
            return incorrect_answers;
        }

        public void setIncorrectAnswers(List<String> incorrect_answers) {
            this.incorrect_answers = incorrect_answers;
        }
    }
}
