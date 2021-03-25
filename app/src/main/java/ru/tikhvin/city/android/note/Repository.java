package ru.tikhvin.city.android.note;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Repository implements NoteDataSource {
    private volatile static Repository sInstance;

    private ArrayList<Note> mData = new ArrayList<>();
    private Context mContext;

    public static Repository getInstance(Context context) {
        Repository instance = sInstance;
        if (instance == null) {
            synchronized (Repository.class) {
                if (sInstance == null) {
                    instance = new Repository(context);
                    sInstance = instance;
                }
            }
        }
        return instance;
    }


    private Repository(Context context) {
        mContext = context;
        mData = getAll();
    }

    public Note getOne(int id) {
        Note note = null;
        for (Note noteL: mData ) {
            int idL = noteL.getId();
            if (idL == id) {
                note = noteL;
                break;
            }
        }
        return note;
    }

    public ArrayList<Note> getAll() {
        return parseJson(loadJSONFromAsset(mContext)) ;
    }

    public static ArrayList<Note> parseJson(JSONObject json) {
        ArrayList notesList = new ArrayList();
        HashMap<Integer, Note> notesMap = new HashMap<>();
        try {
            // parsing json object
            if (json != null ) {
                JSONArray notes = json.getJSONArray("notes");

                for (int i = 0; i < ((JSONArray) notes).length(); i++) {
                    JSONObject noteJs = (JSONObject) notes.getJSONObject(i);
                    Note note = new Note();
                    note.setId(Integer.parseInt(noteJs.getString("id")));
                    note.setTittle(noteJs.getString("tittle"));
                    note.setDescription(noteJs.getString("description"));
                    note.setDate(noteJs.getString("date"));
                    note.setText(noteJs.getString("text"));

                    notesMap.put(Integer.parseInt(noteJs.getString("id")), note);
                    notesList.add(note);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  notesList;
    }

    public static JSONObject loadJSONFromAsset(Context context) {
        String json = null;
        JSONObject jsonObj = null;
        try {
            InputStream is = context.getAssets().open("notes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonObj = new JSONObject(json);
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonObj;
    }
    public void editNote(int noteId, Note note) {
        Note noteSearch = getOne(noteId);
        int idx = mData.indexOf(noteSearch);
        mData.set(idx, note);
    }

    public int getIdByPosition(int position) {
        return mData.get(position).getId();
    }

    @Override
    public List<Note> getNoteData() {
        return mData;
//        return Collections.unmodifiableList(mData);
    }

    @Override
    public Note getItemAt(int idx) {
        return mData.get(idx);
    }

    @Override
    public int getItemsCount() {
        return mData.size();
    }



    @Override
    public void add(@NonNull Note note) {
        mData.add(note);
    }

    @Override
    public void remove(int position) {
        mData.remove(position);
        int i = 1;
    }

    @Override
    public void clear() {
        mData.clear();
    }
}
