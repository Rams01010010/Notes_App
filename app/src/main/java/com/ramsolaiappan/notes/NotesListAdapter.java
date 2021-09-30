package com.ramsolaiappan.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NotesListAdapter extends ArrayAdapter<NotesList> {

    private Context mContext;
    private int mResource;

    NotesListAdapter(Context context, int resource, ArrayList<NotesList> objects)
    {
        super(context,resource,objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(mResource,parent,false);
        }

        TextView titleTV = (TextView) convertView.findViewById(R.id.titleTextView);
        TextView descTV = (TextView) convertView.findViewById(R.id.descTextView);

        titleTV.setText(getItem(position).getTitle());
        descTV.setText(getItem(position).getDescription());

        return convertView;
    }
}
