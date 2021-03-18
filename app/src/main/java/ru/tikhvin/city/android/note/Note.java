package ru.tikhvin.city.android.note;

public class Note {

    private int id;
    private String tittle;
    private String description;
    private String date;
    private String text;

    public Note() {
    }

    public Note(String tittle, String description, String date, String text) {
        this.tittle = tittle;
        this.description = description;
        this.date = date;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



}
