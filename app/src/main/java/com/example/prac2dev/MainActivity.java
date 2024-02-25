package com.example.prac2dev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    EditText edtA, title;
    Uri mUri;
    private EditText edtNote, edtTitle;
    private static final String STATE_NOTE = "state_note";
    private static final String STATE_TITLE = "state_title";
    TextView mTextView;
    ImageView mirea;
    Camera mCamera;



    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)

    int mCurrentScore =2;
    int mCurrentLevel =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.mTextView);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(false);
            }
        }
        if (savedInstanceState != null) {
            edtNote.setText(savedInstanceState.getString(STATE_NOTE));
            edtTitle.setText(savedInstanceState.getString(STATE_TITLE));
        }
        if (savedInstanceState != null) {
            mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
            mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);

        }
        mUri = Uri.parse("content://com.example.app.provider/table/1");

    }
    private void initializeCamera() {
        // TODO: Add your camera setup code here
        mCamera = Camera.open(); // Mở camera

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera != null){
            initializeCamera();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Đây là nơi bạn thực hiện các hoạt động lưu trữ dữ liệu trước khi Activity không còn nhìn thấy được.
        // Ví dụ: bạn có thể muốn lưu nội dung EditText vào cơ sở dữ liệu hoặc thông qua ContentProvider.

        // Lấy nội dung từ EditText
        String noteText = edtNote.getText().toString();
        String titleText = edtTitle.getText().toString();

        // Tạo ContentValues để chứa cặp key-value dữ liệu để lưu
        ContentValues values = new ContentValues();
        values.put(NotePad.Notes.COLUMN_NAME_NOTE, noteText);
        values.put(NotePad.Notes.COLUMN_NAME_TITLE, titleText);

        // Giả định chúng ta có một Uri hợp lệ đến nơi dữ liệu cần được cập nhật
        // Uri này cần được định nghĩa và khởi tạo trước khi sử dụng trong onStop()


        // Thực hiện cập nhật vào cơ sở dữ liệu thông qua ContentProvider
        getContentResolver().update(mUri, values, null, null);

        // Lưu ý: Cần đảm bảo rằng bạn đã xử lý trường hợp mUri chưa được khởi tạo
        // Bạn cũng cần xác định và xử lý các khả năng ngoại lệ có thể xảy ra khi cập nhật cơ sở dữ liệu.
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocationManager locationManager =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    static final String STATE_SCORE = "playerSoccer";
    static final String STATE_LEVEL = "playerLevel";

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putInt(STATE_SCORE, mCurrentScore);
        saveInstanceState.putInt(STATE_LEVEL, mCurrentLevel);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
        mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);
    }
}