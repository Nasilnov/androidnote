package ru.tikhvin.city.android.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

public class NoteActivity extends AppCompatActivity {
    public static  final String KEY_NOTE_ID = "NoteActivity.note.id";
    private TextView mId;
    private TextView mTittle;
    private TextView mText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            int noteId = getIntent().getIntExtra(KEY_NOTE_ID, -1);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.notes_item_container,
                    NoteFragment.newInstance(noteId));
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }

//
//        Intent intent = getIntent();
//        int idNote = intent.getIntExtra(KEY_NOTE_ID, 0);
//
//
//        Note note = Repository.getOne(idNote, this);
//
//        mId = findViewById(R.id.note_id);
//        mId.setText(String.valueOf(note.getId()));
//        mTittle = findViewById(R.id.note_tittle);
//        mTittle.setText(note.getTittle());
//        mText = findViewById(R.id.note_text);
//        mText.setText(note.getText());
    }
}