package com.example.myapplication.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.model.BasicModel;
import com.example.myapplication.model.DataPart;
import com.example.myapplication.permissions.PermissionResultCallback;
import com.example.myapplication.permissions.PermissionUtils;
import com.example.myapplication.utils.Utility;
import com.example.myapplication.webcalls.ApiRequest;
import com.example.myapplication.webcalls.RequestBuilder;
import com.example.myapplication.webcalls.VolleyMultipartRequest;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateNewAdsActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionResultCallback {

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final int MY_PERMISSIONS_REQUEST_WRITE = 200;
    public static final int MY_PERMISSIONS_REQUEST_READ = 300;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";

    // private TextView tvTitle;
    private Button btnSubmit;
    private ImageView ivAddImage, ivShowImage;
    private EditText edtYear, edtAdTitle, edtDescribe, edtPrice, edtKMSDriven, edtLocation;//edtBrandName,
    private RequestQueue rQueue;
    private String path = "";
    private Spinner spnManufacturer, spnModel;
    private Uri uri;
    private File file;
    private String mCurrentPhotoPath;
    private PermissionUtils permissionUtils;
    private List<BasicModel> manufacturerList, modelList;
    String[] list1, list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_ads);
        toGetManufacturerList();
        toSetActionBar();
        initViews();
    }

    private void initViews() {
        try {
            spnManufacturer = findViewById(R.id.spnManufacturer);
            spnModel = findViewById(R.id.spnModel);

//            edtBrandName = findViewById(R.id.etBrandName);
            edtYear = findViewById(R.id.etYear);
            edtAdTitle = findViewById(R.id.etAdTitle);
            edtDescribe = findViewById(R.id.etDescribe);
            edtPrice = findViewById(R.id.etPrice);
            edtKMSDriven = findViewById(R.id.etKMSDriven);
            edtLocation = findViewById(R.id.etLocation);

            ivAddImage = findViewById(R.id.ivAddImage);
            ivShowImage = findViewById(R.id.ivShowImage);

            btnSubmit = findViewById(R.id.btnSubmitNewAd);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (toValidate()) {
                        toCallCreateNewAdApi();
                    }
                }
            });

            ivAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toOpenCamera();
                }
            });

            spnManufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        toGetModelList(position);
                    } else {
                        Toast.makeText(CreateNewAdsActivity.this, R.string.str_please_select_manufacturer, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spnModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                    } else {
                        Toast.makeText(CreateNewAdsActivity.this, R.string.str_please_select_model, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private boolean toValidate() {
        if (spnManufacturer.getSelectedItemPosition() > 0) {
            if (spnModel.getSelectedItemPosition() > 0) {
                if (edtYear != null & edtYear.getText().toString().trim().length() > 0) {
                    if (edtAdTitle != null & edtAdTitle.getText().toString().trim().length() > 0) {
                        if (edtDescribe != null & edtDescribe.getText().toString().trim().length() > 0) {
                            if (edtPrice != null & edtPrice.getText().toString().trim().length() > 0) {
                                if (edtKMSDriven != null & edtKMSDriven.getText().toString().trim().length() > 0) {
                                    if (edtLocation != null & edtLocation.getText().toString().trim().length() > 0) {
                                        return true;
                                    } else {
                                        Toast.makeText(CreateNewAdsActivity.this, R.string.str_please_enter_location, Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                } else {
                                    Toast.makeText(CreateNewAdsActivity.this, R.string.str_please_enter_kms_driven, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            } else {
                                Toast.makeText(CreateNewAdsActivity.this, R.string.str_please_enter_price, Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        } else {
                            Toast.makeText(CreateNewAdsActivity.this, R.string.str_please_enter_describe, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } else {
                        Toast.makeText(CreateNewAdsActivity.this, R.string.str_please_enter_ad_title, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(CreateNewAdsActivity.this, R.string.str_please_enter_year, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(CreateNewAdsActivity.this, R.string.str_please_select_model, Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(CreateNewAdsActivity.this, R.string.str_please_select_manufacturer, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void toCallCreateNewAdApi() {
        try {
            Utility.showLoadingDialog(CreateNewAdsActivity.this, "", " Please wait ... ");
            ApiRequest.getInstance(CreateNewAdsActivity.this).addToRequestQueue(
                    new StringRequest(Request.Method.POST,
                            RequestBuilder.BASE_URL + RequestBuilder.METHOD_SaveNewAd,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("APP ", "Success" + response);
                                    try {
                                        if (response.trim().toLowerCase().contains("success")) {
                                            Utility.toCallToast("Ad Created Successfully", CreateNewAdsActivity.this);
                                            finish();
                                        } else {
//                                            if (response.REASON != null) {
//                                                Utility.toCallToast("Failed. Error :" + response.REASON, CreateNewAdsActivity.this);
//                                            } else {
                                            Utility.toCallToast("Failed to create Ad", CreateNewAdsActivity.this);
//                                            }
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.toCallToast("Failed. Error :" + e.getMessage(), CreateNewAdsActivity.this);
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                Utility.toCallToast("Failed. Error :" + error.getMessage(), CreateNewAdsActivity.this);
                                Utility.dismissLoadingDialog();
                                error.printStackTrace();
                            } catch (Exception | Error e) {
                                Utility.dismissLoadingDialog();
                                e.printStackTrace();
                                Utility.toCallToast("Failed. Error : " + e.getMessage(), CreateNewAdsActivity.this);
                            }
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded; charset=UTF-8";
                        }

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("TOKEN", "ABCD");
                            params.put("MANUFACTURER_ID", "5");
                            params.put("MODEL_ID", "28");
                            params.put("YEAR", edtYear.getText().toString());
                            params.put("IMAGE", path);
                            params.put("ASKING_PRICE", edtPrice.getText().toString());
                            params.put("KMS_DRIVEN", edtKMSDriven.getText().toString());
                            params.put("IS_NEGOTIABLE", "0");
                            params.put("DESCRIPTION", edtDescribe.getText().toString());
                            params.put("TITLE", edtAdTitle.getText().toString());
                            params.put("LOCATION", edtLocation.getText().toString());
                            Log.d("APP", " params" + params);
                            return params;
                        }
                    });
        } catch (Exception | Error e) {
            Utility.toCallToast("Failed. Error :" + e.getMessage(), CreateNewAdsActivity.this);
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    private void toSetActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pale_lilac)));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toOpenCamera() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                if (getFromPref(this, ALLOW_KEY)) {
                    showSettingsAlert();
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        showAlert();
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                }
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (getFromPref(this, ALLOW_KEY)) {
                        showSettingsAlert();

                    } else if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showAlertReadWrite(true);
                        } else {
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_WRITE);
                        }
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (getFromPref(this, ALLOW_KEY)) {
                            showSettingsAlert();
                        } else if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                showAlertReadWrite(false);
                            } else {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ);
                            }
                        }
                    } else {
                        openCamera();
                    }
                }
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                photoFile = createImageFile();
                if (photoFile != null) {
                    cameraIntent.setClipData(ClipData.newRawUri("", FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", photoFile)));
                    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    cameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            | Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(cameraIntent, 2020);
                }
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp;//+".png";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            storageDir.mkdirs();
            File image = File.createTempFile(imageFileName, ".png", storageDir);
            mCurrentPhotoPath = "file:" + image.getAbsolutePath();
            return image;
        } catch (Exception | Error e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showAlert() {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("App needs to access the Camera.");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(CreateNewAdsActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);

                        }
                    });
            alertDialog.show();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void showAlertReadWrite(final boolean isRead) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage(isRead ? "App needs to access the Write Accesss." : "App needs to access the Write Accesss.");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(CreateNewAdsActivity.this,
                                    (isRead ?
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                                            : new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                    ),
                                    (isRead ? MY_PERMISSIONS_REQUEST_READ : MY_PERMISSIONS_REQUEST_WRITE));

                        }
                    });
            alertDialog.show();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void showSettingsAlert() {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("App needs to access the Camera.");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //finish();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startInstalledAppDetailsActivity(CreateNewAdsActivity.this);

                        }
                    });
            alertDialog.show();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    public void startInstalledAppDetailsActivity(final Activity context) {
        try {
            if (context == null) {
                return;
            }
            final Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + context.getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(i);
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            permissionUtils = new PermissionUtils(CreateNewAdsActivity.this);
            permissionUtils.check_permission();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_CAMERA: {
                    for (int i = 0, len = permissions.length; i < len; i++) {
                        String permission = permissions[i];
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                            if (showRationale) {
                                showAlert();
                            } else if (!showRationale) {
                                saveToPreferences(CreateNewAdsActivity.this, ALLOW_KEY, true);
                            }
                        }
                    }
                }

                case MY_PERMISSIONS_REQUEST_WRITE: {
                    for (int i = 0, len = permissions.length; i < len; i++) {
                        String permission = permissions[i];
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                            if (showRationale) {
                                showAlertReadWrite(true);
                            } else if (!showRationale) {
                                saveToPreferences(CreateNewAdsActivity.this, ALLOW_KEY, true);
                            }
                        }
                    }
                }
                case MY_PERMISSIONS_REQUEST_READ: {
                    for (int i = 0, len = permissions.length; i < len; i++) {
                        String permission = permissions[i];
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                            if (showRationale) {
                                showAlertReadWrite(false);
                            } else if (!showRationale) {
                                saveToPreferences(CreateNewAdsActivity.this, ALLOW_KEY, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ApplySharedPref")
    public void saveToPreferences(Context context, String key, Boolean allowed) {
        try {
            SharedPreferences myPrefs = context.getSharedPreferences
                    (CAMERA_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putBoolean(key, allowed);
            prefsEditor.commit();
            prefsEditor.apply();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void uploadImage() {
        try {
            Utility.showLoadingDialog(CreateNewAdsActivity.this, "", " Please wait ... ");
            VolleyMultipartRequest volleyMultipartRequest =
                    new VolleyMultipartRequest(
                            Request.Method.POST, RequestBuilder.BASE_URL + RequestBuilder.METHOD_UploadDocument,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    try {
                                        if (response != null) {
                                            Log.d("APP : ", "  Upload Image String  : \n " + response.data.toString());
                                            rQueue.getCache().clear();
                                            JSONObject jsonObject = new JSONObject(new String(response.data));
                                            Log.d("APP : ", "  Upload Image String  : \n " + jsonObject.toString());
                                            if (jsonObject.getString("STATUS") != null && jsonObject.getString("STATUS").trim().equalsIgnoreCase(getResources().getString(R.string.str_success_uppercase))) {
                                                if (jsonObject.getString("DATA") != null && jsonObject.getString("DATA").trim().length() > 0) {
                                                    path = jsonObject.getString("DATA");
                                                }
                                            }
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        e.printStackTrace();
                                        Utility.dismissLoadingDialog();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Utility.dismissLoadingDialog();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            return new HashMap<>();
                        }

                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            try {
                                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(CreateNewAdsActivity.this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                                ivShowImage.setImageBitmap(mImageBitmap);

                                params.put("DOC", new DataPart(System.currentTimeMillis() + "_1" + ".png", Utility.getJPGLessThanMaxSize(mImageBitmap, 1000)));//getFileDataFromDrawable(mImageBitmap)));
                                Log.d("APP : ", "  Upload Image Data part  : \n " + params.toString());
                            } catch (Exception | Error e) {
                                e.printStackTrace();
                            }
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() {
                            return new HashMap<>();
                        }
                    };
            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(CreateNewAdsActivity.this);
            rQueue.add(volleyMultipartRequest);
        } catch (Exception | Error e) {
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("APP  ", " onActivityResult " + requestCode + ":" + resultCode + ":" + (data != null ? data.getData() : "null"));
        try {
            if (data != null) {
                if (data.getData() != null) {
                    uri = data.getData();
                    file = new File(Objects.requireNonNull(uri.getPath()));
                    mCurrentPhotoPath = file.getPath();
                }
            }
            if (resultCode == RESULT_OK) {
                uploadImage();
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void toGetManufacturerList() {
        try {
            Utility.showLoadingDialog(CreateNewAdsActivity.this, "", " Please wait ... ");
            ApiRequest.getInstance(CreateNewAdsActivity.this).addToRequestQueue(
                    RequestBuilder.toFetchManufacturer(
                            new Response.Listener<BasicModel>() {
                                @Override
                                public void onResponse(BasicModel response) {
                                    try {
                                        if (response != null) {
                                            manufacturerList = response.basicModelList;
                                        }
                                        if (manufacturerList != null && manufacturerList.size() > 0) {
                                            list1 = new String[manufacturerList.size() + 1];
                                            list1[0] = getResources().getString(R.string.select);
                                            for (int i = 0; i < manufacturerList.size(); i++) {
                                                list1[i + 1] = manufacturerList.get(i).NAME;
                                            }
                                        }
                                        if (list1 != null && list1.length > 0) {
                                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CreateNewAdsActivity.this, android.R.layout.simple_spinner_item, list1);
                                            dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                            spnManufacturer.setAdapter(dataAdapter);
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Utility.dismissLoadingDialog();
                                    error.printStackTrace();
                                }
                            }));
        } catch (Exception | Error e) {
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    private void toGetModelList(int model) {
        try {
            Utility.showLoadingDialog(CreateNewAdsActivity.this, "", " Please wait ... ");
            ApiRequest.getInstance(CreateNewAdsActivity.this).addToRequestQueue(
                    RequestBuilder.toFetchModelByManufacture(model,
                            new Response.Listener<BasicModel>() {
                                @Override
                                public void onResponse(BasicModel response) {
                                    try {
                                        if (response != null) {
                                            modelList = response.basicModelList;
                                        }
                                        if (modelList != null && modelList.size() > 0) {
                                            list2 = new String[modelList.size() + 1];
                                            list2[0] = getResources().getString(R.string.select);
                                            for (int i = 0; i < modelList.size(); i++) {
                                                list1[i + 1] = modelList.get(i).NAME;
                                            }
                                        }
                                        if (list2 != null && list2.length > 0) {
                                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CreateNewAdsActivity.this, android.R.layout.simple_spinner_item, list2);
                                            dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                            spnModel.setAdapter(dataAdapter);
                                        }
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Utility.dismissLoadingDialog();
                                    error.printStackTrace();

                                }
                            }
                    ));
        } catch (Exception | Error e) {
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void PermissionGranted(int request_code) {
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
    }

    @Override
    public void PermissionDenied(int request_code) {
    }

    @Override
    public void NeverAskAgain(int request_code) {
    }
}
