package com.buduroid.esitereport;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    // Billionaires json url
    //private static final String urlReport = "https://raw.githubusercontent.com/mobilesiri/Android-Custom-Listview-Using-Volley/master/richman.json";
    private static final String urlReport = "http://www.innovacia.com.my/mobile/sitereport/list.php";

    private ProgressDialog pDialog;
    private ReportData reportData;
    private List<ReportData> dataset = new ArrayList<ReportData>();
    private ListView listView;
    private ReportAdapter adapter;

    Button btnAdd, btnRefresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        adapter = new ReportAdapter(this, dataset);
        listView.setAdapter(adapter);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"YOUR MESSAGE",Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, ReportAdd.class);
                i.putExtra("EDITMODE", "FALSE");
                startActivity(i);

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loadReport();
            }
        });



        //adapter = new ReportAdapter(this, dataset);
        //listView.setAdapter(adapter);
        //listView.setOnItemClickListener(this);

        loadReport();

    }


    private void loadReport()
    {

        //CLEAR DATA
        dataset.clear();


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        JsonArrayRequest billionaireReq = new JsonArrayRequest(urlReport, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                hidePDialog();

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        reportData = new ReportData();
                        reportData.setRepDate(obj.getString("repdate"));
                        reportData.setRepTitle(obj.getString("reptitle"));
                        reportData.setThumbnailUrl(obj.getString("repimage"));
                        reportData.setRepDesc(obj.getString("repdesc"));
                        //reportData.setYear(obj.getInt("InYear"));
                        reportData.setRepLocation(obj.getString("replocation"));
                        reportData.setRepID(obj.getString("uid"));

                        //ADD TO ARRAY
                        dataset.add(reportData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(billionaireReq);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        reportData = (ReportData) adapterView.getItemAtPosition(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Edit or Delete?")
                .setCancelable(false)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        String repID = reportData.getRepID();
                        String repLocation = reportData.getRepLocation();
                        //Toast.makeText(getApplicationContext(),"Hello Gapo \n" + repLocation ,Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(MainActivity.this, ReportAdd.class);
                        i.putExtra("EDITMODE", "TRUE");
                        i.putExtra("REPID", repID);
                        i.putExtra("REPLOCATION", repLocation);
                        startActivity(i);
                        //finish();
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("AlertDialogExample");
        alert.show();



    }


}
