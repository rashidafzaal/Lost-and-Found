package com.example.rashid.lostandfound.Settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rashid.lostandfound.R;

import java.util.HashMap;

public class EditSettings extends AppCompatActivity {

    String old_name, old_pass, rid, name, password;
    EditText ed1, ed2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_settings);


        Intent i = getIntent();
        ed1 = (EditText) findViewById(R.id.edit_name);
        ed2 = (EditText) findViewById(R.id.edit_pass);

        old_name = i.getStringExtra("key_name");
        old_pass = i.getStringExtra("key_password");

        ed1.setText(old_name);
        ed2.setText(old_pass);
    }

    //-------------------------------------------- Button Click -----------------------------------------

    public void onClickSave(View view)
    {
        //if noChange
        if (old_name.equals(ed1.getText().toString()) && old_pass.equals(ed2.getText().toString()))
        {
            Intent i = new Intent(EditSettings.this, ProfileSettings.class);
            startActivity(i);
        }
        else //if AnyChange
        {
            VolleyForSaveChanges();
        }

    }
    //-------------------------------------------- Save Changes in Database ----------------------------------
    public void VolleyForSaveChanges()
    {
        final ProgressDialog loading = ProgressDialog.show(EditSettings.this,"Saving Changes...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(EditSettings.this);
        String UPLOAD_URL = getString(R.string.ip)+"/LOST_AND_FOUND/SaveChanges.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog
                        if (response.contains("UserSuccess"))
                        {
                            Intent i = new Intent(EditSettings.this, ProfileSettings.class);
                            startActivity(i);
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

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EditSettings.this);
                rid = prefs.getString("SP_id", "");


                params.put("KEY_ID", rid);
                params.put("KEY_NAME", ed1.getText().toString());
                params.put("KEY_PASSWORD", ed2.getText().toString());
                //returning parameters
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
