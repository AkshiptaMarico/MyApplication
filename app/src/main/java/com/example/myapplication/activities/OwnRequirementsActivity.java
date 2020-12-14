package com.example.myapplication.activities;

import android.Manifest;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.adapters.AdapterImagesList;
import com.example.myapplication.model.BasicModel;
import com.example.myapplication.model.DataPart;
import com.example.myapplication.permissions.PermissionResultCallback;
import com.example.myapplication.permissions.PermissionUtils;
import com.example.myapplication.utils.SlidingUpPanelLayout;
import com.example.myapplication.utils.SlidingUpPanelLayout.PanelState;
import com.example.myapplication.utils.Utility;
import com.example.myapplication.webcalls.ApiRequest;
import com.example.myapplication.webcalls.RequestBuilder;
import com.example.myapplication.webcalls.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OwnRequirementsActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionResultCallback {

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final int MY_PERMISSIONS_REQUEST_WRITE = 200;
    public static final int MY_PERMISSIONS_REQUEST_READ = 300;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    public static int COMPRESSED_RATIO = 13;
    public static int perPixelDataSize = 4;

    private Map<String, String> header;
    private RequestQueue rQueue;
    private Uri uri;
    private File file;
    private PermissionUtils permissionUtils;
    private List<String> listImagePath = new ArrayList<>();

    private RecyclerView rvImages;
    private Button btnSubmit;
    private TextView tvORQty;
    private EditText edtORDescription;
    private SlidingUpPanelLayout mLayout;
    private ImageView ivORCamera, ivORMinus, ivORPlus;
    private LinearLayout llTakePicture, llUploadPicture;

    @Override
    protected void onStart() {
        super.onStart();
        try {
            permissionUtils = new PermissionUtils(OwnRequirementsActivity.this);
            permissionUtils.check_permission();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_own_requirements);
            header = new HashMap<>();
            header.put("TOKEN", "ABCD");
            toSetActionBar();
            initViews();
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        try {
            mLayout = findViewById(R.id.sliding_layout);

            btnSubmit = findViewById(R.id.btnORSubmit);

            tvORQty = findViewById(R.id.tvORQty);
            edtORDescription = findViewById(R.id.edtORDescription);

            ivORCamera = findViewById(R.id.ivORCamera);
            ivORMinus = findViewById(R.id.ivORMinus);
            ivORPlus = findViewById(R.id.ivORPlus);

            rvImages = findViewById(R.id.rvImages);
            rvImages.setHasFixedSize(true);
            rvImages.setLayoutManager(new LinearLayoutManager(OwnRequirementsActivity.this, LinearLayoutManager.HORIZONTAL, true));
            rvImages.hasFixedSize();

            llTakePicture = findViewById(R.id.llTakePicture);
            llUploadPicture = findViewById(R.id.llUploadPicture);

            llTakePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toOpenCamera();
                }
            });


            ivORCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleBottomSheet();
                }
            });


            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toCallUploadOwnRequirements();
                }
            });

        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    public void toggleBottomSheet() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED
                        || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            if (mLayout != null) {
                mLayout.setPanelState(PanelState.EXPANDED);
            }
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
//                    cameraIntent.putExtra("IsbtnClick", isBtnClick);
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", photoFile));
                    //Uri.fromFile(photoFile));
                    startActivityForResult(cameraIntent, (listImagePath != null ? listImagePath.size() : 0));
                }
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }


    private File createImageFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "IMAGE_" + timeStamp + "__" + (listImagePath != null ? listImagePath.size() + 1 : 0);//+".png";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            storageDir.mkdirs();
            File image = File.createTempFile(imageFileName, ".png", storageDir);
            listImagePath.add((listImagePath != null ? listImagePath.size() : 0), "file:" + image.getAbsolutePath());
            //file:/storage/emulated/0/Pictures/JPEG_20200605_204241_1-1190445354.png
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
                            ActivityCompat.requestPermissions(OwnRequirementsActivity.this,
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
                            ActivityCompat.requestPermissions(OwnRequirementsActivity.this,
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
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startInstalledAppDetailsActivity(OwnRequirementsActivity.this);

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("APP  ", " onActivityResult " + requestCode + ":" + resultCode + ":" +
                (data != null ? data.getData() : "null")
        );
        try {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.getData() != null) {
                        uri = data.getData();
                        file = new File(uri.getPath());
                        listImagePath.add(requestCode, file.getPath());
//                    mCurrentPhotoPath = file.getPath();
//                    mCurrentPhotoPath = data.getData().toString();
//                        uploadImage(requestCode);
                    }
                }
                uploadImage(requestCode);
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    private void uploadImage(final int docCount) {
        try {
            if (file == null) {
                file = new File(listImagePath.get(docCount - 1));
            }
            rvImages.setAdapter(new AdapterImagesList(OwnRequirementsActivity.this, listImagePath));

            Utility.showLoadingDialog(OwnRequirementsActivity.this, "", " Please wait ... ");
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                    RequestBuilder.BASE_URL + RequestBuilder.METHOD_UploadDocument,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            try {
                                Log.d("APP : ", "  Upload Image String  1: \n " + response.data.toString());
                                rQueue.getCache().clear();
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                Log.d("APP : ", "  Upload Image String  2: \n " + jsonObject.toString());
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
                    Map<String, String> params = new HashMap<>();
                    params.put("TOKEN", "ABCD");
                    Log.d("APP : ", "  Upload Image String  : \n " + params.toString());
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {

                    Map<String, DataPart> params = new HashMap<>();
                    try {
                        long imagename = System.currentTimeMillis();
                        Log.d("APP", " FILE : : " + file.getName() + ":" + file.getAbsolutePath() + ":" + file.getPath() + ":" + file.getCanonicalPath());
                        Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(OwnRequirementsActivity.this.getContentResolver(), Uri.parse(listImagePath.get(docCount - 1).trim()));
                        params.put("DOC", new DataPart(imagename + "_1" + ".png", Utility.getJPGLessThanMaxSize(mImageBitmap, 1000)));
                        Log.d("APP : ", "  Upload Image Data part  : \n " + params.toString());
                    } catch (Exception | Error e) {
                        e.printStackTrace();
                    }
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    return header;
                }
            };

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(OwnRequirementsActivity.this);
            rQueue.add(volleyMultipartRequest);
        } catch (Exception | Error e) {
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    private void toCallUploadOwnRequirements() {
        try {
            JSONArray array = new JSONArray();
            Map<String, String> header = new HashMap<>();
            header.put(getResources().getString(R.string.str_token_uppercase), "ABCD");
            if (listImagePath != null && listImagePath.size() > 0) {
                for (int i = 0; i < listImagePath.size(); i++) {
                    array.put(new JSONObject().put("NAME", listImagePath.get(i)));
                }
            }
            Utility.showLoadingDialog(OwnRequirementsActivity.this, "", " Please wait ... ");
            ApiRequest.getInstance(OwnRequirementsActivity.this).addToRequestQueue(
                    RequestBuilder.toSaveOwnRequirements(
                            header,
                            new JSONObject()
                                    .put("TOKEN", "ABCD")
                                    .put("DESCRIPTION", edtORDescription.getText().toString())
                                    .put("COMMENTS", edtORDescription.getText().toString())
                                    .put("Images", array)
                                    .toString(),
                            new Response.Listener<BasicModel>() {
                                @Override
                                public void onResponse(BasicModel response) {
                                    try {
                                        Utility.dismissLoadingDialog();
                                    } catch (Exception | Error e) {
                                        Utility.toCallToast(OwnRequirementsActivity.this.getResources().getString(R.string.str_invalid_data) + "\nError: " + e.getMessage(), OwnRequirementsActivity.this);
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        Utility.toCallToast(OwnRequirementsActivity.this.getResources().getString(R.string.str_invalid_data)
                                                + "\nError : " + error.getMessage(), OwnRequirementsActivity.this);
                                        Utility.dismissLoadingDialog();
                                        error.printStackTrace();
                                    } catch (Exception | Error e) {
                                        Utility.dismissLoadingDialog();
                                        e.printStackTrace();
                                    }
                                }
                            }));
        } catch (Exception | Error e) {
            Utility.toCallToast(OwnRequirementsActivity.this.getResources().getString(R.string.str_invalid_data), OwnRequirementsActivity.this);
            Utility.dismissLoadingDialog();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toSetActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
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
