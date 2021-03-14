package ru.tikhvin.city.android.note;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Repository {

    public static Note getOne(int id, Context context) {
        Note note = null;
        for (Note noteL: getAll(context) ) {
            if (noteL.getId() == id) {
                note = noteL;
            }
        }
        return note;
    }

    public static ArrayList<Note> getAll(Context context) {
        return parseJson(loadJSONFromAsset(context)) ;
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

}
