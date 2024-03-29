package com.example.rashid.lostandfound.DataClassAndParsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rashid on 8/6/2017.
 */

public class UserJSONParser
{
    String id, name, email, password;
    ArrayList<UserDataClass> arr = new ArrayList<>();

    public ArrayList<UserDataClass> showJSON (String response)
    {

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");


            for (int i = 0; i < result.length(); i++)
            {
                JSONObject Data = result.getJSONObject(i);

                id = Data.getString("id");
                name = Data.getString("name");
                email = Data.getString("email");
                password = Data.getString("password");

                UserDataClass obj = new UserDataClass();
                obj.setId(id);
                obj.setName(name);
                obj.setEmail(email);
                obj.setPassword(password);

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
