package com.example.rashid.lostandfound.DataClassAndParsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rashid on 8/20/2017.
 */

public class LostFoundJSONParser
{
    String id, title, desc, contact, img1, rid, timedate;
    ArrayList<LostFoundDataClass> arr = new ArrayList<>();

    public ArrayList<LostFoundDataClass> showJSON (String response)
    {

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");


            for (int i = 0; i < result.length(); i++)
            {
                JSONObject Data = result.getJSONObject(i);

                id = Data.getString("id");
                title = Data.getString("title");
                desc = Data.getString("desc");
                contact = Data.getString("contact");
                img1 = Data.getString("img1");
                rid = Data.getString("rid");
                timedate = Data.getString("timedate");

                LostFoundDataClass obj = new LostFoundDataClass();
                obj.setId(id);
                obj.setTitle(title);
                obj.setDesc(desc);
                obj.setContact(contact);
                obj.setImg1(img1);
                obj.setTimedate(timedate);
                obj.setRid(rid);

                arr.add(obj);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return arr;
    }
}
