package com.example.myapplication.webcalls;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.myapplication.R;

public class VolleyErrorHelper {
    /**
     * Returns appropriate message which is to be displayed to the user against
     * the specified error object.
     *
     * @param error
     * @param context
     * @return
     */
    public static String getMessage(Object error, Context context) {

        if (error instanceof TimeoutError) {
            return "Server is not responding";
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);
        } else if (error instanceof VolleyError) {
            return ((VolleyError) error).getMessage();
        } else if (isNetworkProblem(error)) {
            return "No internet connection";
        }
        return "Error occuered please try again..";
    }

    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server
     *
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock
     * message or to show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;
        if (response != null) {
            switch (response.statusCode) {
                case 400:
                    return context.getResources().getString(R.string.GENERIC_WEB_ERROR_400);
                case 404:
                    return context.getResources().getString(R.string.GENERIC_WEB_ERROR_404);
                case 422:
                    return context.getResources().getString(R.string.GENERIC_WEB_ERROR_422);
                case 401:
                    return context.getResources().getString(R.string.GENERIC_WEB_ERROR_401);
                case 500:
                    return context.getResources().getString(R.string.GENERIC_WEB_ERROR_500);
                case 204:
                    return context.getResources().getString(R.string.GENERIC_WEB_ERROR_204);
                case 504:
                    return context.getResources().getString(R.string.str_no_internet_connection);
                default:
                    return context.getResources().getString(R.string.GENERIC_WEB_ERROR_DEFAULT);
            }
        }

        return context.getResources().getString(R.string.str_error_occured_please_try_again);
    }
}
