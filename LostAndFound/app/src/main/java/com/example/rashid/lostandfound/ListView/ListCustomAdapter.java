package com.example.rashid.lostandfound.ListView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rashid.lostandfound.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by rashid on 8/20/2017.
 */

public class ListCustomAdapter extends ArrayAdapter<MyListDataClass>
{
    Context c;
    int layoutFile;
    MyListDataClass [] data;

    private ArrayList<MyListDataClass> arraylist;

    public ListCustomAdapter(Context c, int layoutFile, MyListDataClass[] data)
    {
        super(c, layoutFile, data);
        this.c = c;
        this.layoutFile = layoutFile;
        this.data = data;

        this.arraylist = new ArrayList<MyListDataClass>(Arrays.asList(data));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v, row,row2;
        if(convertView == null)
        {
            LayoutInflater inflater = ((Activity)c).getLayoutInflater();
            row = inflater.inflate(R.layout.list_row, parent, false);
        }
        else
        {
            row = (View) convertView;
        }

        if (data[position] == null)
        {
            LayoutInflater inflater = ((Activity)c).getLayoutInflater();
            row2 = inflater.inflate(R.layout.list_row, parent, false);
            return row2;
        }
        else
        {
            TextView txt = (TextView)row.findViewById(R.id.text1);
            txt.setText(data[position].getText1());

            TextView txt2 = (TextView) row.findViewById(R.id.text2);
            txt2.setText(data[position].getText2());

            ImageView img = (ImageView) row.findViewById(R.id.img1);
            img.setImageBitmap(data[position].getImg());

            TextView txt3 = (TextView) row.findViewById(R.id.txt_datetime);
            txt3.setText(data[position].getTimedate());

            return row;

        }


    }
//=================================== filter ================================================
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        Arrays.fill(data, null);
        if (charText.length() == 0)
        {
            data = arraylist.toArray(new MyListDataClass[arraylist.size()]);
        }
        else
        {
            int index = 0;
            for (MyListDataClass wp : arraylist)
            {
                if (wp.getText1().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getText2().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    data[index] = wp;
                    index++;
                }

            }
        }
        notifyDataSetChanged();
    }
}
