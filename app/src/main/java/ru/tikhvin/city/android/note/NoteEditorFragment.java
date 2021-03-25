package ru.tikhvin.city.android.note;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteEditorFragment extends Fragment {

    private int mNoteId;
    private static final String ARG_ITEM_ID = "NoteEditorFragment.item_id";

    public NoteEditorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param noteId Parameter 1.
     * @return A new instance of fragment NoteEditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteEditorFragment newInstance(int noteId) {
        NoteEditorFragment fragment = new NoteEditorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNoteId = getArguments().getInt(ARG_ITEM_ID, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_editor, container, false);
        final Repository repository = Repository.getInstance(getContext());
        Note note = new Note();
        if (mNoteId != -1) {
            note = repository.getOne(mNoteId);
        }
        final TextInputEditText tittle = view.findViewById(R.id.note_edit_tittle);
        final TextInputEditText  date = view.findViewById(R.id.note_edit_date);
        final TextInputEditText  text = view.findViewById(R.id.note_edit_text);

        if (mNoteId != -1) {
            tittle.setText(note.getTittle());
            date.setText(note.getDate());
            text.setText(note.getText());
        }

        final MaterialButton btnSave = view.findViewById(R.id.btn_save);

        Note finalNote = note;
        btnSave.setOnClickListener((v) -> {
            finalNote.setTittle( tittle.getText().toString());
            finalNote.setDate( date.getText().toString());
            finalNote.setText( text.getText().toString());
            if (mNoteId != -1) {
                repository.editNote(mNoteId, finalNote);
            }
            else {
                finalNote.setId(repository.getItemsCount() + 1);
                repository.add(finalNote);
            }
            Intent intent = new Intent (getActivity(), MainActivity.class); //создаем интент
            startActivity(intent);
//            getFragmentManager().popBackStack();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}