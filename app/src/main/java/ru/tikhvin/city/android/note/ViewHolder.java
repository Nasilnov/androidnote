package ru.tikhvin.city.android.note;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.atomic.AtomicInteger;

public class ViewHolder extends RecyclerView.ViewHolder {
    private static final AtomicInteger COUNTER = new AtomicInteger();
    public final TextView tittle;
    public final TextView date;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tittle = itemView.findViewById(R.id.note_item_tittle);
        date = itemView.findViewById(R.id.note_item_date);
    }

    public void populate(NoteListFragment fragment, Note note) {
        date.setText(note.getDate());
        tittle.setText(note.getText());
        itemView.setOnLongClickListener((v) -> {
            fragment.setId(note.getId());
            fragment.setLastSelectedPosition(getLayoutPosition());
            return false;
        });
        fragment.registerForContextMenu(itemView);
    }

    public void clear(Fragment fragment) {
        itemView.setOnLongClickListener(null);
        fragment.unregisterForContextMenu(itemView);
    }
}
