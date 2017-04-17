package com.buduroid.esitereport;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;
import java.util.Map;

public class MaterialAdd extends AppCompatActivity {


    EditText etRepID, etMatName;
    Button btnAdd;

    private String KEY_REPID = "repid";
    private String KEY_MATNAME = "matname";


    private String MAT_URL = "http://www.innovacia.com.my/mobile/sitereport/material_add.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_add);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        etMatName = (EditText) findViewById(R.id.etMatName);
        etRepID = (EditText) findViewById(R.id.etRepID);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });


    }


    private void sendData() {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Sending...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MAT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        //Toast.makeText(ReportAdd.this, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        //Toast.makeText(ReportAdd.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        //tvTitle.setText(volleyError.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                String repid = etRepID.getText().toString().trim();
                String matname = etMatName.getText().toString().trim();


                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put(KEY_REPID, repid);
                params.put(KEY_MATNAME, matname);


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


}
