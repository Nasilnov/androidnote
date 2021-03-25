package ru.tikhvin.city.android.note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final NoteListFragment mFragment;
    private final LayoutInflater mInflater;
    private final NoteDataSource mNoteDataSource;

    private NoteListFragment.OnClickListener mOnClickListener;

    public ViewHolderAdapter(NoteListFragment fragment, NoteDataSource noteDataSource ) {
        mInflater = fragment.getLayoutInflater();
        mNoteDataSource = noteDataSource;
        mFragment = fragment;
    }

    public void setOnClickListener(NoteListFragment.OnClickListener onClickListener) {
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
        Note note = mNoteDataSource.getItemAt(position);
        holder.populate(mFragment, note);
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
        return mNoteDataSource.getItemsCount();
    }
}
