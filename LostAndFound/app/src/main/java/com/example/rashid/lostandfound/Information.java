package com.example.rashid.lostandfound;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;

public class Information extends AppCompatActivity {

    String _id, rid;
    ImageView img;
    TextView tv1, tv2, tv3, tv4, tv5;
    ArrayList<LostFoundDataClass> res;
    String id, title, desc, contact, img1, timedate;
    Bitmap decodedByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Intent i = getIntent();
        _id = i.getStringExtra("KEY_LIST_ID");
        rid = i.getStringExtra("KEY_LIST_RID");

        img = (ImageView)findViewById(R.id.details_img1);
        tv1 = (TextView)findViewById(R.id.details_title);
        tv2 = (TextView)findViewById(R.id.desc2);
        tv3 = (TextView)findViewById(R.id.details_contact);
        tv4 = (TextView)findViewById(R.id.details_date);
        tv5 = (TextView)findViewById(R.id.name);

        Toast.makeText(Information.this, "ID: "+ _id, Toast.LENGTH_SHORT).show();
        CallVolley();
        GetName();
    }

    //============================================== Call Now ===============================================
    public void Click_Call(View v)
    {
        String a="tel:"+contact ;
        Uri call = Uri.parse(a);

        Intent in = new Intent("android.intent.action.DIAL",call);
        startActivity(in);
    }

    //============================================== Get Name ===============================================
    public void GetName()
    {
        RequestQueue queue = Volley.newRequestQueue(Information.this);
        String UPLOAD_URL = getString(R.string.ip)+"/LOST_AND_FOUND/GetName.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Disimissing the progress dialog

                        if(!(r.contains("notexist")))
                        {
                            tv5.setText(r);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Toast.makeText(Information.this, "Error: " +volleyError,Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                //Creating parameters
                java.util.Map<String, String> params = new HashMap  <String, String>();

                params.put("KEY_RID", rid);
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

    //============================================== Volley Call ===============================================
    public void CallVolley()
    {
        final ProgressDialog loading = ProgressDialog.show(Information.this,"Getting Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(Information.this);
        String UPLOAD_URL = getString(R.string.ip)+"/LOST_AND_FOUND/GetInformation.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Disimissing the progress dialog

                        LostFoundJSONParser obj1 = new LostFoundJSONParser();
                        res = obj1.showJSON(r);

                        int count;
                        for (count = 0 ;  count < res.size(); count++)
                        {
                            id = res.get(count).getId();
                            title = res.get(count).getTitle();
                            desc = res.get(count).getDesc();
                            contact = res.get(count).getContact();
                            img1 = res.get(count).getImg1();
                            timedate = res.get(count).getTimedate();

                            //Decoding Image
                            byte[] decodedString = Base64.decode(img1, Base64.DEFAULT);
                            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        }

                        img.setImageBitmap(decodedByte);
                        tv1.setText(title);
                        tv2.setText(desc);
                        tv3.setText(contact);
                        tv4.setText(timedate);

                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Toast.makeText(Information.this, "Error: " +volleyError,Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                //Creating parameters
                java.util.Map<String, String> params = new HashMap  <String, String>();

                params.put("KEY_ID", _id);
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
}
