package ru.tikhvin.city.android.note;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteListFragment extends Fragment {
    private int mCurrentId = -1;
    private int firstNoteId = 1;

    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_note_list, container, false);
        ArrayList<Note> noteList = Repository.getAll(getContext());
        firstNoteId = noteList.get(0).getId();
        for (Note note: noteList) {
            int noteId = note.getId();
            TextView tv = new TextView(getContext());
            tv.setText(note.getTittle());
            tv.setTextSize(30);
            tv.setOnClickListener((v) -> {
                setCurrentId(noteId);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    goToSeparateActivity(noteId);
                }
                else {
                    showToTheRight(noteId);
                }
            });
            view.addView(tv);
        }

        return view;
    }

    private void goToSeparateActivity(int note_id) {
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        intent.putExtra(NoteActivity.KEY_NOTE_ID, note_id);
        startActivity(intent);
    }


    private void setCurrentId(int noteId) {
        mCurrentId = noteId;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            if (bundle != null) {
                mCurrentId = bundle.getInt(NoteFragment.ARG_NOTE_ID, -1);
                showToTheRight(mCurrentId);
            } else {
                mCurrentId = firstNoteId;
                showToTheRight(mCurrentId);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putInt(NoteFragment.ARG_NOTE_ID, mCurrentId);
    }

    private void showToTheRight(int noteId) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.notes_item_container, NoteFragment.newInstance(noteId));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }


}