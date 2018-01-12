package com.example.dell.tourassistant.EventPackage;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.tourassistant.R;

import java.util.ArrayList;

/**
 * Created by DELL on 10/26/2017.
 */

public class MomentAdapter extends ArrayAdapter<Moment> {
    private Context context;
    private ArrayList<Moment> momentList;

    public MomentAdapter(@NonNull Context context, ArrayList<Moment> momentList) {
        super(context, R.layout.moment_row,  momentList);
        this.context = context;
        this.momentList = momentList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.moment_row,parent,false);

        TextView momentNoteTV = (TextView) convertView.findViewById(R.id.moment_note_);
        ImageView momentIV=(ImageView) convertView.findViewById(R.id.moment_image);

        momentNoteTV.setText(momentList.get(position).getMomentNote()+"\n\nTime: "+momentList.get(position).getMomentTime());
        Uri photoUri = Uri.parse(momentList.get(position).getImagePath());
        momentIV.setImageURI(photoUri);
        return convertView;
    }
}
