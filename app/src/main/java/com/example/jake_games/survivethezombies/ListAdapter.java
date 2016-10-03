package com.example.jake_games.survivethezombies;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 22.12.2015.
 */
public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> data;
    private static LayoutInflater inflater = null;

    public ListAdapter(Context context, ArrayList<String> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_view_rank, null);

        String mine = data.get(position);
        TextView number = (TextView) vi.findViewById(R.id.nr);
        TextView score = (TextView) vi.findViewById(R.id.score);
        TextView name = (TextView) vi.findViewById(R.id.name);
        TextView place = (TextView) vi.findViewById(R.id.place);

        String[] objectProperties = TextUtils.split(mine, "/");

        number.setText((position + 1) + ".");
        score.setText(objectProperties[0].toString());
        name.setText(objectProperties[1].toString());
        place.setText(objectProperties[2].toString());

        return vi;
    }
}
