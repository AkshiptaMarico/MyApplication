package com.example.myapplication.webcalls;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.example.myapplication.model.BasicModel;
import com.example.myapplication.model.CommonResponse;

import java.util.Map;

public class RequestBuilder {
    public static String BASE_URL = "https://www.wrenchub.com/";
    //    public static String BASE_URL = "http://Wrenchub-env.eba-uxjrdtae.us-west-2.elasticbeanstalk.com/";
    public static String IMAGE_BASE_URL = "http://www.wrenchub.com/storage/DOC/";
    private static final String METHOD_CheckIfUserActivated = "GARAGE/CheckIfUserActivated";
    private static final String METHOD_SaveProfile = "GARAGE/SaveProfile";
    private static final String METHOD_RetrieveProfile = "GARAGE/RetrieveProfile";
    private static final String METHOD_RetrieveDashboardData = "GARAGE/RetrieveDashboardData";
    private static final String METHOD_RetrieveAllAppointment = "GARAGE/RetrieveAllAppointment";
    private static final String METHOD_RetrieveAppointmentDetailById = "GARAGE/RetrieveAppointmentDetailById";
    private static final String METHOD_UpdateAppointmentStatus = "GARAGE/UpdateAppointmentStatus";
    public static final String METHOD_SaveNewAd = "SaveNewAd";
    private static final String METHOD_FetchVehiclesDetailById = "FetchVehiclesDetailById";
    private static final String METHOD_FetchVehiclesList = "FetchVehiclesList";
    private static final String METHOD_FetchMyAds = "FetchMyAds";
    public static final String METHOD_UploadDocument = "COMMON/uploadDocument";
    private static final String METHOD_FetchManufacturer = "COMMON/fetchManufacturer";
    private static final String METHOD_FetchModelByManufacture = "COMMON/fetchModelByManufacture?MANUFACTURER_ID=";
    private static final String METHOD_FetchProductList = "FetchProductList";
    private static final String METHOD_FetchProductDetail = "FetchProductDetail";
    private static final String METHOD_FetchCategoryList = "FetchCategoryList";
    private static final String METHOD_SaveOwnRequirement = "SaveOwnRequirement";
    private static final String METHOD_AddProductToCart = "AddToCart";
    private static final String METHOD_DeleteCartItem = "RemoveFromCart";
    private static final String METHOD_IncreaseCartItemQuantity = "IncreaseQuantity";
    private static final String METHOD_DecreaseCartItemQuantity = "DecreaseQuantity";
    private static final String METHOD_FetchCart = "FetchCart";
    private static final String METHOD_FetchParentCategoryList = "COMMON/FetchParentCategoryList";

    //uploadBackup
    public static GsonRequest<CommonResponse> toCheckIfUserActivated(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_CheckIfUserActivated;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toSaveProfile(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_SaveProfile;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toRetrieveProfile(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_RetrieveProfile;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toRetrieveDashboardData(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_RetrieveDashboardData;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toRetrieveAllAppointment(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_RetrieveAllAppointment;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toRetrieveAppointmentDetailById(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_RetrieveAppointmentDetailById;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toUpdateAppointmentStatus(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_UpdateAppointmentStatus;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toSaveNewAd(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_SaveNewAd;
        return new GsonRequest<CommonResponse>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toFetchVehiclesDetailById(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_FetchVehiclesDetailById;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toFetchVehiclesList(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_FetchVehiclesList;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<BasicModel> toFetchCategoryList(Map<String, String> headers, String data, Response.Listener<BasicModel> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_FetchParentCategoryList;
        return new GsonRequest<>(Method.GET, url, BasicModel.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toFetchProductList(int categoryId, Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_FetchProductList+(categoryId>0?"?CATEGORY_ID="+categoryId:"");
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toFetchProductDetail(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_FetchProductDetail;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toFetchMyAds(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_FetchMyAds;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toUploadDocument(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_UploadDocument;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<BasicModel> toFetchManufacturer(Response.Listener<BasicModel> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_FetchManufacturer;
        return new GsonRequest<>(Method.GET, url, BasicModel.class, null, successListener, errorListener, "", onHeaderListenerCalled());
    }

    public static GsonRequest<BasicModel> toFetchModelByManufacture(int model, Response.Listener<BasicModel> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_FetchModelByManufacture + model;
        return new GsonRequest<>(Method.GET, url, BasicModel.class, null, successListener, errorListener, "", onHeaderListenerCalled());
    }

    public static GsonRequest<BasicModel> toFetchCategoryList(Response.Listener<BasicModel> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_FetchParentCategoryList;
        return new GsonRequest<>(Method.GET, url, BasicModel.class, null, successListener, errorListener, "", onHeaderListenerCalled());
    }

    public static GsonRequest<BasicModel> toSaveOwnRequirements(Map<String, String> headers, String data, Response.Listener<BasicModel> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_SaveOwnRequirement;
        return new GsonRequest<>(Method.POST, url, BasicModel.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<BasicModel> toAddProductToCart(int model, Response.Listener<BasicModel> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_FetchModelByManufacture + model;
        return new GsonRequest<>(Method.POST, url, BasicModel.class, null, successListener, errorListener, "", onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toDeleteCartItem(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_DeleteCartItem;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toIncreaseCartItemQuantity(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_IncreaseCartItemQuantity;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toDecreaseCartItemQuantity(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_DecreaseCartItemQuantity;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    public static GsonRequest<CommonResponse> toFetchCart(Map<String, String> headers, String data, Response.Listener<CommonResponse> successListener, Response.ErrorListener errorListener) {
        String url = BASE_URL + METHOD_FetchCart;
        return new GsonRequest<>(Method.POST, url, CommonResponse.class, headers, successListener, errorListener, data, onHeaderListenerCalled());
    }

    private static HeaderCallBack onHeaderListenerCalled() {
        return new HeaderCallBack() {
            @Override
            public void onHeaderReceived(Map<String, String> headerValues) {
            }
        };
    }
}
