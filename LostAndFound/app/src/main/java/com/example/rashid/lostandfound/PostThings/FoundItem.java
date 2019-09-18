package com.example.rashid.lostandfound.PostThings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class FoundItem extends AppCompatActivity {

    String title, desc, keywords, contact, image1, fk_rid, currentDateTimeString;
    Bitmap bitmap1;
    Uri uri;
    ImageView imageV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_item);

    }

    //-------------------------------------  Post Button -----------------------------------------------
    public void Click_FPost(View view)
    {
        EditText ed1 = (EditText) findViewById(R.id.found_title);
        EditText ed2 = (EditText) findViewById(R.id.found_desc);
        EditText ed3 = (EditText) findViewById(R.id.found_keywords);
        EditText ed5 = (EditText) findViewById(R.id.found_contact);

        title = ed1.getText().toString();
        desc = ed2.getText().toString();
        keywords = ed3.getText().toString();
        contact = ed5.getText().toString();

        if (title.equalsIgnoreCase(""))
        {
            ed1.setError("Please Enter Title");
        }
        if (title.length() >= 45)
        {
            ed1.setError("Must be Less than 45 characters");
        }
        if (desc.equalsIgnoreCase(""))
        {
            ed2.setError("Please Enter Description");
        }
        if (keywords.equalsIgnoreCase(""))
        {
            ed3.setError("Please Enter at least 1 Keyword");
        }
        if (contact.equalsIgnoreCase(""))
        {
            ed5.setError("Please Enter Contact No.");
        }
        else
        {
            currentDateTimeString = DateFormat.getDateInstance().format(new Date());
            if (currentDateTimeString == null)
            {
                currentDateTimeString = "blank";
            }
            Toast.makeText(FoundItem.this, "Current: "+ currentDateTimeString, Toast.LENGTH_SHORT).show();
            CallVolley();
        }
    }
    //------------------------------------- Uploads buttons --------------------------------------------
    public void Click_upload1 (View view)
    {
        Intent i = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        final int a =1234;
        startActivityForResult(i, a);

    }


    //-------------------------------------- ONactivity Results ------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== 1234 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            uri = data.getData();
            try
            {
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = (ImageView) findViewById(R.id.found_img1);
                imageView.setImageBitmap(bitmap1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //------------------------------------ CAll Volley ----------------------------------------------------

    public void CallVolley()
    {
        final ProgressDialog loading = ProgressDialog.show(FoundItem.this,"Posting...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(FoundItem.this);
        String UPLOAD_URL = getString(R.string.ip)+"/LOST_AND_FOUND/PostFoundItem.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String r) {
                        //Disimissing the progress dialog

                        if(r.contains("UserSuccess"))
                        {
                            Intent i = new Intent (FoundItem.this, Home.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(FoundItem.this, "Could not Sign Up",Toast.LENGTH_LONG).show();
                        }

                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        Toast.makeText(FoundItem.this, "Error: " +volleyError,Toast.LENGTH_LONG).show();
                        loading.dismiss();

                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                if (bitmap1 == null)
                {
                    runOnUiThread(new Runnable(){
                        public void run(){
                            Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                            imageV = (ImageView) findViewById(R.id.found_img1);
                            imageV.setImageBitmap(bm1);
                            image1 = getStringImage(bm1);
                        }
                    });

                } else {
                    image1 = getStringImage(bitmap1);
                }

                java.util.Map<String, String> params = new HashMap<String, String>();

                params.put("KEY_TITLE", title);
                params.put("KEY_DESC", desc);
                params.put("KEY_KEYWORDS", keywords);
                params.put("KEY_CONTACT", contact);
                params.put("KEY_IMAGE1", image1);
                params.put("KEY_DATETIME", currentDateTimeString);
                params.put("KEY_STATUS", "FOUND");

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(FoundItem.this);
                fk_rid = prefs.getString("SP_id", "");

                params.put("KEY_FK", fk_rid);

                return params;
            }
        };

        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //----------------------------------- Conversion method -----------------------------------------------
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
