package ru.tikhvin.city.android.note;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

public class NoteListFragment extends Fragment {
    private int mCurrentId = -1;
    private int firstNoteId = 1;
    private int mLastSelectedPosition = -1;
    private int mId = -1;
    private NoteDataSource mNoteDataSource;
    private ViewHolderAdapter mViewHolderAdapter;
    private RecyclerView mRecyclerView;

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
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_note_list, container, false);
        mRecyclerView.setHasFixedSize(true);

        DividerItemDecoration decorator = new DividerItemDecoration(requireActivity(),
                LinearLayoutManager.VERTICAL);
        decorator.setDrawable(getResources().getDrawable(R.drawable.decoration));
        mRecyclerView.addItemDecoration(decorator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mNoteDataSource = Repository.getInstance(getContext());
        mViewHolderAdapter = new ViewHolderAdapter(this,
                mNoteDataSource);
        mViewHolderAdapter.setOnClickListener((v, note_id) -> {
            setCurrentId(note_id);
            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT) {
                goToSeparateActivity(note_id);
            } else {
                showToTheRight(note_id);
            }
        });
        mRecyclerView.setAdapter(mViewHolderAdapter);

        return mRecyclerView;
    }

    protected interface OnClickListener {
        void onItemClick(View v, int position);
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
                mCurrentId = setFirstNoteId();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.note_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.note_list_menu_add) {
            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                showToTheRightEdit(-1);
            } else {
                goToSeparateActivityEdit(-1);
            }
        } else if (item.getItemId() == R.id.note_list_menu_clear) {
            mNoteDataSource.clear();
            mViewHolderAdapter.notifyDataSetChanged();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.note_list_item_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.note_list_item_menu_edit) {
            if (mId != -1) {
                if (getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_LANDSCAPE) {
                    showToTheRightEdit(mId);
                } else {
                    goToSeparateActivityEdit(mId);
                }
            }
        } else if (item.getItemId() == R.id.note_list_item_menu_delete) {
            if (mLastSelectedPosition != -1) {
                mNoteDataSource.remove(mLastSelectedPosition);
                mViewHolderAdapter.notifyItemRemoved(mLastSelectedPosition);
                if (getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_LANDSCAPE) {
                    mCurrentId = setFirstNoteId();
                    showToTheRight(mCurrentId);
                }
            }
        } else {
            mCurrentId = -1;
            return super.onContextItemSelected(item);
        }
        return true;
    }

    /* package */ void setLastSelectedPosition(int lastSelectedPosition) {
        mLastSelectedPosition = lastSelectedPosition;
    }

    /* package */ void setId(int Id) {
        mId = Id;
    }

    private void goToSeparateActivityEdit(int note_id) {
//        Toast toast = Toast.makeText(requireActivity(), String.format(Locale.getDefault(),"id=%d", note_id),Toast.LENGTH_SHORT);
//        toast.show();
        Intent intent = new Intent(getActivity(), NoteActivityEdit.class);
        intent.putExtra(NoteActivityEdit.KEY_NOTE_ID, note_id);
        startActivity(intent);
    }

    private void showToTheRightEdit(int noteId) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.notes_item_container, NoteEditorFragment.newInstance(noteId));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();

    }
    private int setFirstNoteId() {
        return Repository.getInstance(getContext()).getIdByPosition(0);
    }
}