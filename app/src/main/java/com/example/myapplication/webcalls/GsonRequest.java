package com.example.myapplication.webcalls;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.myapplication.utils.Utility;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class GsonRequest<T> extends Request<T> {

    private static final String TAG = null;
    private HeaderCallBack mHeaderCallBack;
    private Gson gson = new Gson();
    private Class<T> clazz;
    private Map<String, String> headers;
    private Listener<T> listener;
    private String data;

    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                       Listener<T> listener, ErrorListener errorListener, String data, HeaderCallBack headerCallBack) {
        super(method, url, errorListener);
        Log.d("W_APP", "Api Response 1: " + getTag() + ":" + method + "\n:" + url + ":" + data
                + "\n: " + clazz + ":" + headers + "\n:" + listener + " : " + errorListener + " : " + headerCallBack);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.data = data;
        mHeaderCallBack = headerCallBack;
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        try {
            listener.onResponse(response);
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBodyContentType() {
//        return "application/x-www-form-urlencoded; charset=UTF-8";
    return "application/json";
    }

//    @Override
//    public byte[] getBody() {
//        Log.i(TAG, "Data called");
//        if (data != null) {
//            return data.getBytes();
//        } else {
//            return null;
//        }
//    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            Log.d("APP","getbody  : " + Arrays.toString((data == null ? null : data.getBytes())));
            return data == null ? null : data.getBytes("utf-8");
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", data, "utf-8");
            return null;
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            Log.d("W_APP", "Api Response 2: " + getTag() + ": Time :: " + response.networkTimeMs);
            if (response.statusCode == HttpsURLConnection.HTTP_OK) {
                String json = null;
                if (response.headers.containsKey("Content-Encoding")) {
                    try {
                        json = Utility.decompress(response.data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                }
                mHeaderCallBack.onHeaderReceived(response.headers);
                Log.d("W_APP", "APIR data received else" + getTag() + ": Time :: " + response.networkTimeMs + "::" + json);
                if (json.trim().length() > 0 && String.valueOf(json.trim().charAt(0)).equals("[")) {
                    return Response.success(gson.fromJson("{\"master\":" + json + "}", clazz), HttpHeaderParser.parseCacheHeaders(response));
                } else {
                    return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
                }
            } else {
                Log.d("W_APP", "error ocured" + new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
                ServerError serverError = new ServerError(response);
                return Response.error(serverError);
            }
        } catch (Exception | Error e) {
            Log.d("W_APP", "encoding exception");
            return Response.error(new ParseError(e));
        }
    }
}
