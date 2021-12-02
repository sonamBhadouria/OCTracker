package com.hosp.oxygen.entry.ui.hospuser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hosp.oxygen.entry.R;
import com.hosp.oxygen.entry.ui.airportuser.AirportUserActivity;
import com.hosp.oxygen.entry.ui.airportuser.AirportUserRequest;
import com.hosp.oxygen.entry.ui.airportuser.AirportUserResponse;
import com.hosp.oxygen.entry.ui.signup.SignUpActivity;
import com.hosp.oxygen.entry.util.APIClient;
import com.hosp.oxygen.entry.util.APIInterface;
import com.hosp.oxygen.entry.util.GpsTracker;
import com.hosp.oxygen.entry.util.SharedPrefData;
import com.hosp.oxygen.entry.util.ShowProgressDialogue;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospUserActivity extends AppCompatActivity {
    ShowProgressDialogue showProgressDialogue;

    int oc_id=0;

    TextView qr_response;

    HospUSerQRCodeResponse hospUSerQRCodeResponse;
    HospUserFormResponse hospUserFormResponse;
    private APIInterface mApiInterface;
    private GpsTracker gpsTracker;

    EditText edt_hosp_name;
    EditText edt_hosp_add;
    EditText edt_hosp_city;
  //  EditText edt_state;
    EditText edt_spoc_name;
    EditText edt_spoc_design;
    EditText edt_spoc_mobile;

    Button btn_selfie;
    ImageView iv_selfie;
    Button btn_oc_image;
    ImageView iv_oc_image;
    Button btn_pmcare_image;
    ImageView iv_pm_care_image;

    Button btn_submit;

    LinearLayout ll_form;

    private File imageF;
    private static final String CAMERA_DIR = "/dcim/";
    private Uri picUri;

    int CAMERA_REQUEST=101;
    int imageEvent=0;
    String selfieImageServerURL="";
    String ocImageServerURL="";
    String pmCareLogoImageServerURL="";


    Spinner spinner;

    String str_lat="",str_long="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showProgressDialogue =new ShowProgressDialogue();


         edt_hosp_name=findViewById(R.id.edt_hosp_name);
         edt_hosp_add=findViewById(R.id.edt_hosp_add);
         edt_hosp_city=findViewById(R.id.edt_hosp_city);
      //   edt_state=findViewById(R.id.edt_state);
         edt_spoc_name=findViewById(R.id.edt_spoc_name);
         edt_spoc_design=findViewById(R.id.edt_spoc_design);
         edt_spoc_mobile=findViewById(R.id.edt_spoc_mobile);



        ll_form=findViewById(R.id.ll_form);
        qr_response = findViewById(R.id.qr_response);
        mApiInterface = APIClient.getClient().create(APIInterface.class);

        btn_submit=findViewById(R.id.btn_submit);
        btn_selfie=findViewById(R.id.btn_selfie);
        iv_selfie=findViewById(R.id.iv_selfie);
        btn_oc_image=findViewById(R.id.btn_oc_image);
        iv_oc_image=findViewById(R.id.iv_oc_image);
        btn_pmcare_image=findViewById(R.id.btn_pmcare_image);
        iv_pm_care_image=findViewById(R.id.iv_pm_care_image);
        currentLocationGet();
        spinner=findViewById(R.id.spinner);
       /* String[] india_states = getResources().getStringArray(R.array.india_states);
        ArrayAdapter adapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_item_text,india_states);
        spinner.setAdapter(adapter);*/

        Button btn_qr_code = findViewById(R.id.btn_qr_code);
        btn_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                IntentIntegrator intentIntegrator = new IntentIntegrator(HospUserActivity.this);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     if(TextUtils.isEmpty(edt_hosp_name.getText().toString()) ||
                             TextUtils.isEmpty(edt_hosp_add.getText().toString()) ||
                             TextUtils.isEmpty(edt_hosp_city.getText().toString()) ||
                             TextUtils.isEmpty(edt_spoc_name.getText().toString()) ||
                             TextUtils.isEmpty(edt_spoc_design.getText().toString()) ||
                             TextUtils.isEmpty(edt_spoc_mobile.getText().toString()) )
                     {
                         Toast.makeText(HospUserActivity.this,"Please fill all details",Toast.LENGTH_LONG).show();
                     }else if(spinner.getSelectedItem().toString().equalsIgnoreCase("State")){
                         Toast.makeText(HospUserActivity.this,"Please select a state",Toast.LENGTH_LONG).show();
                     }else if(selfieImageServerURL.isEmpty() || ocImageServerURL.isEmpty() || pmCareLogoImageServerURL.isEmpty()){
                         Toast.makeText(HospUserActivity.this,"Please capture all images",Toast.LENGTH_LONG).show();
                     }
                     else
                     {
                         saveHospData();
                     }

            }
        });

        btn_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEvent=1;
                captureImage();
            }
        });


        btn_oc_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEvent=2;
                captureImage();
            }
        });
        btn_pmcare_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEvent=3;
                captureImage();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_app:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void currentLocationGet() {
        gpsTracker = new GpsTracker(HospUserActivity.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            if (String.valueOf(latitude).length() > 0 && String.valueOf(longitude).length() > 0) {

                str_lat = String.valueOf(latitude);
                str_long = String.valueOf(longitude);
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_REQUEST) {
            if (picUri != null) {
                Uri uri = picUri;
                String localPath = compressImage(uri.toString());
                System.out.println("compressedImageURl" + localPath);
                File imgFile = new File(localPath);
                if (imgFile.exists()) {

                    try {
                        showProgressDialogue.setProgressDialog(this, "Uploading image...");
                        //                        String filePath = getRealPathFromURI(uri.toString());
                        File fileUpload = new File(localPath);
                        // create RequestBody instance from file
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileUpload);
                        // MultipartBody.Part is used to send also the actual file name
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileUpload.getName(), requestFile);
                        String token = "Bearer " + getFromPrefs(SharedPrefData.ACCESS_Token);
                        // finally, execute the request
                        mApiInterface.Imageupload(token, body).enqueue(new Callback<ImageUploadResponse>() {
                            @Override
                            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_LONG).show();

                                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    if (imageEvent == 1) {
                                        iv_selfie.setImageBitmap(myBitmap);
                                        selfieImageServerURL = response.body().getMessage();
                                    }
                                    if (imageEvent == 2) {
                                        iv_oc_image.setImageBitmap(myBitmap);
                                        ocImageServerURL = response.body().getMessage();
                                    }
                                    if (imageEvent == 3) {
                                        iv_pm_care_image.setImageBitmap(myBitmap);
                                        pmCareLogoImageServerURL = response.body().getMessage();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_LONG).show();
                                }
                                if (showProgressDialogue != null) {
                                    showProgressDialogue.dismissDialogue();
                                }
                            }

                            @Override
                            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                if (showProgressDialogue != null) {
                                    showProgressDialogue.dismissDialogue();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (showProgressDialogue != null) {
                            showProgressDialogue.dismissDialogue();
                        }
                    }
                }

            }

        }
        else {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            // if the intentResult is null then
            // toast a message as "cancelled"
            if (intentResult != null) {
                if (intentResult.getContents() == null) {
                    // Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    // if the intentResult is not null we'll set
                    // the content and format of scan message
                    qr_response.setText(intentResult.getContents() + "-" + intentResult.getFormatName());
                    validateQRCode();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    public void validateQRCode() {
        hospUSerQRCodeResponse=new HospUSerQRCodeResponse();
        HospUserQRCodeReq hospUserQRCodeReq = new HospUserQRCodeReq();
        hospUserQRCodeReq.setQr_code_detail(qr_response.getText().toString());
        String accesstoken="Bearer "+getFromPrefs(SharedPrefData.ACCESS_Token);
        try {
            showProgressDialogue.setProgressDialog(HospUserActivity.this,"Please wait...");
            mApiInterface.getOxygenConcentration("application/json",accesstoken, qr_response.getText().toString()).enqueue(new Callback<HospUSerQRCodeResponse>() {
                @Override
                public void onResponse(Call<HospUSerQRCodeResponse> call, Response<HospUSerQRCodeResponse> response) {
                    showProgressDialogue.dismissDialogue();
                    if (response.body() != null) {
                        if(response.body().getId()==0)
                        { Toast.makeText(HospUserActivity.this,"QR Code is invalid, OC details not found",Toast.LENGTH_LONG).show();
                            ll_form.setVisibility(View.GONE);
                        }else
                        {
                            oc_id=response.body().getId();
                            Toast.makeText(HospUserActivity.this,"OC details found, Please fill below form",Toast.LENGTH_LONG).show();
                            ll_form.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        Toast.makeText(HospUserActivity.this,"Error!!",Toast.LENGTH_LONG).show();
                        ll_form.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onFailure(Call<HospUSerQRCodeResponse> call, Throwable t) {
                    showProgressDialogue.dismissDialogue();
                    Toast.makeText(HospUserActivity.this,"Error!!!",Toast.LENGTH_LONG).show();
                    ll_form.setVisibility(View.GONE);

                }
            });
        } catch (Exception ex) {
            showProgressDialogue.dismissDialogue();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void saveHospData() {
        hospUserFormResponse=new HospUserFormResponse();
        HospUserFormRequest hospUserFormRequest = new HospUserFormRequest();
        hospUserFormRequest.setOc_id(oc_id);
        hospUserFormRequest.setHosp_name(edt_hosp_name.getText().toString());
        hospUserFormRequest.setHosp_address(edt_hosp_add.getText().toString());
        hospUserFormRequest.setCity(edt_hosp_city.getText().toString());
     //   hospUserFormRequest.setState(edt_state.getText().toString());
        hospUserFormRequest.setState(spinner.getSelectedItem().toString());
        hospUserFormRequest.setSpoc_name(edt_spoc_name.getText().toString());
        hospUserFormRequest.setSpoc_design(edt_spoc_design.getText().toString());
        hospUserFormRequest.setSpoc_mobile(edt_spoc_mobile.getText().toString());
        hospUserFormRequest.setLatitude(str_lat);
        hospUserFormRequest.setLongitude(str_long);
        hospUserFormRequest.setSpoc_selfie_url(selfieImageServerURL);
        hospUserFormRequest.setOc_doc_url(ocImageServerURL);
        hospUserFormRequest.setPm_care_url(pmCareLogoImageServerURL);

        String accesstoken="Bearer "+getFromPrefs(SharedPrefData.ACCESS_Token);
        try {
            showProgressDialogue.setProgressDialog(HospUserActivity.this,"Please wait...");
            mApiInterface.saveHospitalData("application/json",accesstoken, hospUserFormRequest).enqueue(new Callback<HospUserFormResponse>() {
                @Override
                public void onResponse(Call<HospUserFormResponse> call, Response<HospUserFormResponse> response) {
                    showProgressDialogue.dismissDialogue();
                    if (response.body() != null) {
                        Toast.makeText(HospUserActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        if(response.body().isSuccess())
                         {
                            finish();
                            Intent myIntent= new Intent(HospUserActivity.this, HospUserActivity.class);
                            startActivity(myIntent);
                         }
                    }
                    else {
                        Toast.makeText(HospUserActivity.this,"Error!!",Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<HospUserFormResponse> call, Throwable t) {
                    showProgressDialogue.dismissDialogue();
                    Toast.makeText(HospUserActivity.this,"Error!!!",Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception ex) {
            showProgressDialogue.dismissDialogue();
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String getFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(SharedPrefData.PREF_NAME, MODE_PRIVATE);
        return prefs.getString(key, SharedPrefData.DEFAULT_VALUE);
    }




    public void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // takePictureIntent.putExtra("quesPos",questionPosition);
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "bmh" + timeStamp + "_";
            File albumF = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            imageF = File.createTempFile(imageFileName, ".jpg", albumF);
            picUri = Uri.fromFile(imageF);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageF));
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, getPackageName() + ".provider", imageF));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        startActivityForResult(takePictureIntent, CAMERA_REQUEST);

    }



    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }


        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

            Canvas canvas1 = new Canvas(scaledBitmap);
            //  Canvas canvas = new Canvas(bitmap); //bmp is the bitmap to dwaw into
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
            String date_string = "Date: " + sdf.format(new Date());
            String latitude_val = " Lat: " + str_lat;
            String longitude_val = " Long: " + str_long;
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(18);
            paint.setTextAlign(Paint.Align.CENTER);

            canvas1.drawText(date_string, 120, 20, paint);
            canvas1.drawText(latitude_val, 80, 40, paint);
            canvas1.drawText(longitude_val, 85, 60, paint);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename() {
        File file = new File(this.getExternalFilesDir(null), "Onsite");
        if (!file.exists()) {
            boolean isFolderCreated= file.mkdirs();
            Log.d("isFolderCreated=",isFolderCreated+"");
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
}