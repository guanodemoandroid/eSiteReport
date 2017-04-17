package com.buduroid.esitereport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class ReportAdd extends AppCompatActivity implements View.OnClickListener {


    private Button buttonChoose;
    private Button buttonUpload;
    private Button btnMaterial;


    private ImageView imageView;
    private EditText editTextName;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    //private String UPLOAD_URL ="http://simplifiedcoding.16mb.com/VolleyUpload/upload.php";
    private String UPLOAD_URL = "http://www.innovacia.com.my/mobile/sitereport/upload.php";
    private String REPID_URL = "http://www.innovacia.com.my/mobile/sitereport/get_repid.php";


    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    private String KEY_TITLE = "reptitle";
    private String KEY_DESC = "repdesc";
    private String KEY_LOCATION = "replocation";
    private String KEY_DATE = "repdate";


    EditText etRepTitle, etRepDesc, etRepLocation, etRepDate;
    String repTitle, repDesc, repLocation, repDate;
    String image, name;

    TextView tvRepID, tvTitle;
    String editMode;
    String intentRepID, intentRepLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_add);

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        btnMaterial = (Button) findViewById(R.id.btnMaterial);


        editTextName = (EditText) findViewById(R.id.editText);

        tvRepID = (TextView) findViewById(R.id.tvRepID);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        etRepTitle = (EditText) findViewById(R.id.etRepTitle);
        etRepDesc = (EditText) findViewById(R.id.etRepDesc);
        etRepLocation = (EditText) findViewById(R.id.etRepLocation);
        etRepDate = (EditText) findViewById(R.id.etRepDate);


        imageView = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        btnMaterial.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            editMode = extras.getString("EDITMODE");
            intentRepID = extras.getString("REPID");
            intentRepLocation = extras.getString("REPLOCATION");


            //The key argument here must match that used in the other activity
        }

        if (editMode.equals("TRUE"))
        {
            bindData();
        }

    }


    private void bindData()
    {

        tvRepID.setText(intentRepID);
        etRepLocation.setText(intentRepLocation);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }

        if (v == buttonUpload) {

            getInfo();
            uploadImage();
        }
        if (v == btnMaterial) {

            getRepID();

            //Intent i = new Intent(ReportAdd.this, MaterialAdd.class);
            //i.putExtra("REPID", "13-1");
            //startActivity(i);
        }



    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(ReportAdd.this, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(ReportAdd.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        //tvTitle.setText(volleyError.getMessage().toString());
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);
                //Getting Image Name
                String name = editTextName.getText().toString().trim();
                //getInfo();
                //repTitle = etRepTitle.getText().toString().trim();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);


                params.put(KEY_TITLE, repTitle);
                params.put(KEY_DESC, repDesc);
                params.put(KEY_LOCATION, repLocation);
                params.put(KEY_DATE, repDate);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void getInfo(){

        repTitle = etRepTitle.getText().toString().trim();
        repDesc = etRepDesc.getText().toString().trim();
        repLocation = etRepLocation.getText().toString().trim();
        repDate = etRepDate.getText().toString().trim();

        //image = getStringImage(bitmap);
        //name = editTextName.getText().toString().trim();



    }

    private void getRepID() {
        final ProgressDialog loading = ProgressDialog.show(this, "Getting Rep ID...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REPID_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(ReportAdd.this, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                       // Toast.makeText(ReportAdd.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        //tvTitle.setText(volleyError.getMessage().toString());
                    }
                }) {


        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


}
