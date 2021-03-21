package ru.tikhvin.city.android.note;

import java.util.List;

public interface NoteDataSource {
    List<Note> getNoteData();
    Note getItemAt(int idx);
    int getItemsCount();
}
