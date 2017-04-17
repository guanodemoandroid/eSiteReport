package com.buduroid.esitereport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends Activity implements AdapterView.OnItemClickListener {

    // Log tag
    private static final String TAG = MainActivity2.class.getSimpleName();

    // Billionaires json url
    //private static final String urlReport = "https://raw.githubusercontent.com/mobilesiri/Android-Custom-Listview-Using-Volley/master/richman.json";
    private static final String urlReport = "http://www.innovacia.com.my/mobile/sitereport/list_report.php";

    private ProgressDialog pDialog;
    private ReportData reportData;
    private List<ReportData> dataset = new ArrayList<ReportData>();
    private ListView listView;
    private ReportAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = new ReportAdapter(this, dataset);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
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
                                //ReportData worldsBillionaires = new ReportData();
                                //worldsBillionaires.setName(obj.getString("name"));
                                //worldsBillionaires.setThumbnailUrl(obj.getString("image"));
                                //worldsBillionaires.setWorth(obj.getString("worth"));
                                //worldsBillionaires.setYear(obj.getInt("InYear"));
                                //worldsBillionaires.setSource(obj.getString("source"));


                                reportData = new ReportData();
                                reportData.setRepDate(obj.getString("repdate"));
                                reportData.setRepTitle(obj.getString("reptitle"));
                                reportData.setThumbnailUrl(obj.getString("repimage"));
                                reportData.setRepDesc(obj.getString("repdesc"));
                                //reportData.setYear(obj.getInt("InYear"));
                                reportData.setRepLocation(obj.getString("replocation"));


                                // adding Billionaire to worldsBillionaires array
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


        String selectedValue = adapter.getItem(position).toString();
        Toast.makeText(getApplicationContext(),"Hello Gapo" + selectedValue ,Toast.LENGTH_SHORT).show();

    }



}
