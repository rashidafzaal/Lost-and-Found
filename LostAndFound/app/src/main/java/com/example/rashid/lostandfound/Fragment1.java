package com.example.rashid.lostandfound;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rashid.lostandfound.DataClassAndParsers.LostFoundDataClass;
import com.example.rashid.lostandfound.DataClassAndParsers.LostFoundJSONParser;
import com.example.rashid.lostandfound.ListView.ListCustomAdapter;
import com.example.rashid.lostandfound.ListView.MyListDataClass;

import java.util.ArrayList;
import java.util.HashMap;


public class Fragment1 extends Fragment{

    public Fragment1() {
        // Required empty public constructor
    }

    View view;
    ArrayList<LostFoundDataClass> res;
    String id, title, desc, contact, img1, rid, timedate;
    String _id, _rid;
    MyListDataClass[] array;
    static ListCustomAdapter adapter;
    ListView lv;

    //============================================== OnCreate ===============================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_fragment1, container, false);

        return view;
    }

    //============================================== OnView ===============================================

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CallVolley();

    }

    //============================================== Volley Call ===============================================
    public void CallVolley()
    {
        final Context c = getContext();
        final ProgressDialog loading = ProgressDialog.show(c,"Getting Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(c);
        String UPLOAD_URL = getString(R.string.ip)+"/LOST_AND_FOUND/GetData.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Disimissing the progress dialog

                        LostFoundJSONParser obj1 = new LostFoundJSONParser();
                        res = obj1.showJSON(r);

                        array = new MyListDataClass[res.size()]; //Array of Size = No. of Rows Record
                        int count;
                        for (count = 0 ;  count < res.size(); count++)
                        {
                            id = res.get(count).getId();
                            title = res.get(count).getTitle();
                            desc = res.get(count).getDesc();
                            contact = res.get(count).getContact();
                            img1 = res.get(count).getImg1();
                            timedate = res.get(count).getTimedate();
                            rid = res.get(count).getRid();

                            //Decoding Image
                            byte[] decodedString = Base64.decode(img1, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            //Populating Objects and then Adding them in an Array
                            MyListDataClass c1 = new MyListDataClass(title, desc, decodedByte, timedate);
                            array[count] = c1;
                            adapter = new ListCustomAdapter(c,R.layout.list_row,array);

                        }
                        lv = (ListView) view.findViewById(R.id.list1);
                        lv.setAdapter(adapter);

                        listItemClickFunction();
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Toast.makeText(c, "Error: " +volleyError,Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                //Creating parameters
                java.util.Map<String, String> params = new HashMap<String, String>();

                //returning parameters
                return params;
            }
        };

        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //======================================= listItemClickFunction ==============================================

    public void listItemClickFunction()
    {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View v, int position, long id) {

                _id = res.get(position).getId(); //getting id of ListItem
                _rid = res.get(position).getRid();

                v.animate().setDuration(100).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                adapter.notifyDataSetChanged();
                                v.setAlpha(1);

                                final Context c = getContext();
                                Intent i = new Intent(c, Information.class);
                                i.putExtra("KEY_LIST_ID", _id);
                                i.putExtra("KEY_LIST_RID", _rid);
                                startActivity(i);
                            }
                        });
            }
        });
    }


}
