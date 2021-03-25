package ru.tikhvin.city.android.note;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class NoteFragment extends Fragment {

    public static final String ARG_NOTE_ID = "NoteActivity.note.id";

    private int mNoteId;

    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param noteId note id.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(int noteId) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNoteId = getArguments().getInt(ARG_NOTE_ID, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if ( mNoteId == -1 ) {
            mNoteId = Repository.getInstance(getContext()).getItemAt(0).getId();
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        Note note = Repository.getInstance(getContext()).getOne(mNoteId);
        TextView id = view.findViewById(R.id.note_id);
        id.setText(String.valueOf(note.getId()));
        TextView tittle = view.findViewById(R.id.note_tittle);
        tittle.setText(note.getTittle());
        TextView  date = view.findViewById(R.id.note_date);
        date.setText(note.getDate());
        TextView text = view.findViewById(R.id.note_text);
        text.setText(note.getText());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}