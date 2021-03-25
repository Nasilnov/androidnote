package ru.tikhvin.city.android.note;

import androidx.annotation.NonNull;

import java.util.List;

public interface NoteDataSource {
    List<Note> getNoteData();
    Note getItemAt(int idx);
    int getItemsCount();

    void add(@NonNull Note note);
    void remove(int position);
    void clear();
}
