package ru.tikhvin.city.android.note;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

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
//        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_note_list, container, false);
//        ArrayList<Note> noteList = Repository.getAll(getContext());
//        firstNoteId = noteList.get(0).getId();
//        for (Note note: noteList) {
//            int noteId = note.getId();
//            TextView tv = (TextView) inflater.inflate(R.layout.note_item, view, false);
//            tv.setText(note.getTittle());
//            tv.setOnClickListener((v) -> {
//                setCurrentId(noteId);
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                    goToSeparateActivity(noteId);
//                }
//                else {
//                    showToTheRight(noteId);
//                }
//            });
//            view.addView(tv);
//        }
//
//        return view;
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_note_list, container, false);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration decorator = new DividerItemDecoration(requireActivity(),
                LinearLayoutManager.VERTICAL);
        decorator.setDrawable(getResources().getDrawable(R.drawable.decoration));
        recyclerView.addItemDecoration(decorator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(layoutManager);

        ViewHolderAdapter viewHolderAdapter = new ViewHolderAdapter(inflater,
                new Repository(getContext()));
        viewHolderAdapter.setOnClickListener((v, note_id) -> {
            setCurrentId(note_id);
            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT) {
                goToSeparateActivity(note_id);
            } else {
                showToTheRight(note_id);
            }
        });
        recyclerView.setAdapter(viewHolderAdapter);

        return recyclerView;


    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private static final AtomicInteger COUNTER = new AtomicInteger();
        public final TextView tittle;
        public final TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.note_item_tittle);
            date = itemView.findViewById(R.id.note_item_date);
        }

        public void populate(Note note) {
            date.setText(note.getDate());
            tittle.setText(note.getText());
        }
    }

    private interface OnClickListener {
        void onItemClick(View v, int position);
    }

    private static class ViewHolderAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final LayoutInflater mInflater;
        private final Repository mRepository;

        private OnClickListener mOnClickListener;

        public ViewHolderAdapter(LayoutInflater inflater, Repository repository) {
            mInflater = inflater;
            mRepository = repository;
        }

        public void setOnClickListener(OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = mInflater.inflate(R.layout.note_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
//            android.util.Log.e(NoteListFragment.class.getCanonicalName(),
//                    String.format(Locale.getDefault(), "created holder id=%d", viewHolder.id));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Note note = mRepository.getItemAt(position);
            holder.populate(note);
            holder.itemView.setOnClickListener((v) -> {
                if (mOnClickListener != null) {
                    mOnClickListener.onItemClick(v, note.getId());
                }
            });
//            android.util.Log.e(FruitListFragment.class.getCanonicalName(),
//                    String.format(Locale.getDefault(), "used holder id=%d", holder.id));
        }

        @Override
        public int getItemCount() {
            return mRepository.getItemsCount();
        }
    }




    private void goToSeparateActivity(int note_id) {
//        Toast toast = Toast.makeText(requireActivity(), String.format(Locale.getDefault(),"id=%d", note_id),Toast.LENGTH_SHORT);
//        toast.show();
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