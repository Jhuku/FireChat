package com.shuvam.basicfirebase.utils;
import android.app.ProgressDialog;
import android.content.Context;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class ServerHelper {

    Context context;

    public ServerHelper(Context context) {
        this.context = context;
    }
/*------------------------------------------------------*
*             Server request Functions                  *
*-------------------------------------------------------*/

    //---- Volley Custom Request Methods for all POST requests
    public void serverPostRequest(String URL, final Map<String, String> params, final String progressMessage, final ServerCallback callback) {
        //----Prepare Progress Dialog Here
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(progressMessage);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        //--- Show Prepared Progress dialog
        if (!progressMessage.equals(""))
            progressDialog.show();

        //--- Creating Volley Request to process URL request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //--- Call Back on Response of data
                        callback.onSuccess(response);
                        if (!progressMessage.equals(""))
                            progressDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //--- Call Back on Error of data
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            callback.onError("Please check you internet connection");
                        }
                        else {
                            callback.onError(error.toString());
                        }

                        if (!progressMessage.equals(""))
                            progressDialog.hide();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
           /* @Override
            public Map<String, String> getHeaders() {
                Map<String,String> headers = new HashMap<>();
                //--- Add headers
                return headers;
            }*/
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //--- Creating Volley Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //--- Adding request in Queue
        requestQueue.add(stringRequest);
    }

    //---- Volley Custom Request Methods for all GET requests
    public void serverGetRequest(String URL, final String progressMessage, final ServerCallback callback) {
        //----Prepare Progress Dialog Here
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(progressMessage);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        //--- Show Prepared Progress dialog
        if (!progressMessage.equals(""))
            progressDialog.show();

        //--- Creating Volley Request to process URL request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //--- Call Back on Response of data
                        callback.onSuccess(response);
                        if (!progressMessage.equals(""))
                            progressDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //--- Call Back on Error of data
                        callback.onError(error.toString());
                        if (!progressMessage.equals(""))
                            progressDialog.hide();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //--- Creating Volley Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //--- Adding request in Queue
        requestQueue.add(stringRequest);
    }

    //--- Server Callback interface for responses of Volley
    public interface ServerCallback {
        void onSuccess(String response);

        void onError(String error);
    }


}