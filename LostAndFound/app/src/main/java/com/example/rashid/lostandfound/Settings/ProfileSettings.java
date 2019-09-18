package com.example.rashid.lostandfound.Settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rashid.lostandfound.DataClassAndParsers.UserDataClass;
import com.example.rashid.lostandfound.DataClassAndParsers.UserJSONParser;
import com.example.rashid.lostandfound.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileSettings extends AppCompatActivity {

    String id, name,email,password, rid;
    TextView tv1,tv2, tv3;
    ArrayList<UserDataClass> res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);


        tv1= (TextView) findViewById(R.id.u_XML_name);
        tv2= (TextView) findViewById(R.id.u_XML_email);
        tv3= (TextView) findViewById(R.id.u_XML_password);


        CallVolley();
    }

    public void onClickEditBtn(View view)
    {
        Intent i = new Intent(ProfileSettings.this, EditSettings.class);
        i.putExtra("key_name", tv1.getText().toString());
        i.putExtra("key_password",tv3.getText().toString());
        startActivity(i);
    }
    //---------------------------------------- get Profile Data Volley -------------------------------------
    public void CallVolley()
    {
        final ProgressDialog loading = ProgressDialog.show(ProfileSettings.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProfileSettings.this);
        String UPLOAD_URL = getString(R.string.ip)+"/LOST_AND_FOUND/getProfileData.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        UserJSONParser obj1 = new UserJSONParser();
                        res = obj1.showJSON(response);

                        int count;
                        for (count = 0 ;  count < res.size(); count++)
                        {
                            id = res.get(count).getId();
                            name = res.get(count).getName();
                            email = res.get(count).getEmail();
                            password = res.get(count).getPassword();

                            tv1.setText(name);
                            tv2.setText(email);
                            tv3.setText(password);
                        }

                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {

                java.util.Map<String, String> params = new HashMap<String, String>();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProfileSettings.this);
                rid = prefs.getString("SP_id", "");

                params.put("KEY_RID", rid);

                return params;
            }
        };
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
