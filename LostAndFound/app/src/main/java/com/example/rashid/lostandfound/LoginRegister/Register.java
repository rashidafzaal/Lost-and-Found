package com.example.rashid.lostandfound.LoginRegister;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rashid.lostandfound.Home;
import com.example.rashid.lostandfound.R;

import java.util.HashMap;

public class Register extends Activity {

    String name, email, password;
    EditText ed2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        CheckBox box = (CheckBox)findViewById(R.id.Register_checkbox);
        final EditText password = (EditText) findViewById(R.id.xml_reg_password);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if(checked){
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else{
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });


        On_Press_Register();
    } // end of onCreate

    public void On_Press_Register()
    {
        Button btn = (Button) findViewById(R.id.reg_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText ed1 = (EditText) findViewById(R.id.xml_reg_name);
                ed2 = (EditText) findViewById(R.id.xml_reg_email);
                EditText ed3 = (EditText) findViewById(R.id.xml_reg_password);

                name = ed1.getText().toString();
                email = ed2.getText().toString();
                password = ed3.getText().toString();

//                boolean found = true;
//
//                if (name.matches(".*\\d+.*") || name.equalsIgnoreCase("")) {
//                    ed1.setError("Please Enter name in Alphabets");
//                    found = false;
//                }
//                if (!(email.trim().contains("@")) || email.length() <= 10) {
//                    ed2.setError("Please Enter a Valid Email Address");
//                    found = false;
//
//                }
//                if (password.length() < 8) {
//                    ed3.setError("Must be at least 8 characters long");
//                    found = false;
//                }
//                if (found) {
//                    EmailDuplicationCheck();
//                }

                Intent i = new Intent (Register.this, Home.class);
                startActivity(i);
            }
        });
    }

    public void CallVolley()
    {
        final ProgressDialog loading = ProgressDialog.show(Register.this,"Signing Up...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(Register.this);
        String UPLOAD_URL = getString(R.string.ip)+"/LOST_AND_FOUND/register.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Disimissing the progress dialog
                        if(r.contains("UserSuccess"))
                        {
                            Intent i = new Intent (Register.this, Home.class);
                            startActivity(i);
                            GetRID(); //Get Rid and Save in SharedPreferences
                        }
                        else{
                            Toast.makeText(Register.this, "Please Enter Valid Phone and Password",Toast.LENGTH_LONG).show();
                        }
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Toast.makeText(Register.this, "Error: " +volleyError,Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                //Creating parameters
                java.util.Map<String, String> params = new HashMap<String, String>();

                //Adding parameters
                params.put("KEY_NAME", name);
                params.put("KEY_EMAIL", email);
                params.put("KEY_PASSWORD", password);

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

    //------------------------------------------ Get RID against given Email ----------------------------------

    public void GetRID()
    {
        RequestQueue queue = Volley.newRequestQueue(Register.this);
        String UPLOAD_URL = getString(R.string.ip)+"/LOST_AND_FOUND/GetRID.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Disimissing the progress dialog

                        String rid = r.replaceAll("[^0-9]", "");
                        Toast.makeText(Register.this, "Person who Logged in: " + rid, Toast.LENGTH_SHORT).show();

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("SP_id", rid);
                        editor.apply();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Toast.makeText(Register.this, "Error: " +volleyError,Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                //Creating parameters
                java.util.Map<String, String> params = new HashMap<String, String>();

                //Adding parameters
                params.put("KEY_EMAIL", email);

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

    //---------------------------------------- Email Duplicate Volley ------------------------------------

    public void EmailDuplicationCheck()
    {
        RequestQueue queue = Volley.newRequestQueue(Register.this);
        String UPLOAD_URL = getString(R.string.ip)+"/LOST_AND_FOUND/Duplicate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Disimissing the progress dialog

                        if (r.contains("UserSuccess"))
                        {
                            ed2.setError("Email is Already Registered");
                        }
                        else
                        {
                            CallVolley(); // if Everything is correct then register the credentials
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Toast.makeText(Register.this, "Error: " +volleyError,Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                //Creating parameters
                java.util.Map<String, String> params = new HashMap<String, String>();

                //Adding parameters
                params.put("KEY_EMAIL", email);

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
