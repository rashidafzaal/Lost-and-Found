package com.example.rashid.lostandfound.LoginRegister;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.example.rashid.lostandfound.ForgotPassword;
import com.example.rashid.lostandfound.Home;
import com.example.rashid.lostandfound.R;

import java.util.HashMap;

public class Login extends Activity {

    String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CheckBox box = (CheckBox)findViewById(R.id.Login_checkbox);
        final EditText password = (EditText) findViewById(R.id.xml_password);
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


        Login_btn_Pressed();
        Forgot_Pressed();
    }
    public void Forgot_Pressed()
    {
        TextView t2 = (TextView) findViewById(R.id.XML_textView_forgot);
        t2.setPaintFlags(t2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, ForgotPassword.class);
                startActivity(i);

            }
        });
    }

    public void Login_btn_Pressed()
    {
        Button btn = (Button) findViewById(R.id.login_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                EditText ed1 = (EditText) findViewById(R.id.xml_email);
                EditText ed2 = (EditText) findViewById(R.id.xml_password);
                email = ed1.getText().toString();
                password = ed2.getText().toString();

//                if (!(email.trim().contains("@")) || email.length() <= 10)
//                {
//                    ed1.setError("Please Enter a Valid Email Address");
//                }
//                else
//                {
//                    CallVolley();
//                }

                GetRID();
                CallVolley();

            }
        });
    }

    //------------------------------------------ Volley Call --------------------------------------------
    public void CallVolley()
    {
        final ProgressDialog loading = ProgressDialog.show(Login.this,"Logging in...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        String UPLOAD_URL = getString(R.string.ip)+"/LOST_AND_FOUND/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Disimissing the progress dialog
                        if(r.contains("UserSuccess"))
                        {
                            Intent i = new Intent (Login.this, Home.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(Login.this, "Email and Password Doesn't Match",Toast.LENGTH_LONG).show();
                        }
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Toast.makeText(Login.this, "Error: " +volleyError,Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                //Creating parameters
                java.util.Map<String, String> params = new HashMap<String, String>();

                //Adding parameters
                params.put("KEY_EMAIL", email);
                params.put("KEY_PASSWORD", password);

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


    //------------------------------------------ Get RID against given Email ----------------------------------

    public void GetRID()
    {
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        String UPLOAD_URL = getString(R.string.ip)+"/LOST_AND_FOUND/GetRID.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Disimissing the progress dialog

                        String rid = r.replaceAll("[^0-9]", "");
                        Toast.makeText(Login.this, "Person who Logged in: " + rid, Toast.LENGTH_SHORT).show();

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("SP_id", rid);
                        editor.apply();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Toast.makeText(Login.this, "Error: " +volleyError,Toast.LENGTH_LONG).show();

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
