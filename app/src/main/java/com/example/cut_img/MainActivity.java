// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.example.cut_img;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    static Bitmap mImage;
    static Bitmap mImage2;
    private static ImageView imageView;
    private static ImageView imageView2;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.onResume();
    setContentView(R.layout.activity_main);
      SharedPreferences sharedPref = getSharedPreferences(
              getString(R.string.preference_file_key), Context.MODE_PRIVATE);
      btn1 = (Button) findViewById(R.id.button1);
      btn2 = (Button) findViewById(R.id.button2);
      btn3 = (Button) findViewById(R.id.button3);
      imageView = ((ImageView) findViewById(R.id.resultImageView));
      imageView2 = ((ImageView) findViewById(R.id.resultImageView2));
      if(mImage != null) {
          imageView.setImageBitmap(mImage);
      }
      if(mImage2 != null) {
              imageView2.setImageBitmap(mImage2);
      }
      btn1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent1 = new Intent(MainActivity.this, CropMainActivity.class);
              intent1.putExtra("EXTRA_SESSION_ID", "1");
              startActivity(intent1);
          }
      });
      btn2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent1 = new Intent(MainActivity.this, CropMainActivity.class);
              intent1.putExtra("EXTRA_SESSION_ID", "2");
              startActivity(intent1);
          }
      });
      btn3.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Bitmap combineBitmap = bitmapOverlayToCenter(mImage, mImage2);
//              insertImage(getContentResolver(), combineBitmap, "title", "description");
              saveImg(combineBitmap);
          }
      });
  }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permision Write File is Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permision Write File is Denied", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
        public Bitmap bitmapOverlayToCenter(Bitmap bitmap1, Bitmap overlayBitmap) {
        int bitmap1Width = bitmap1.getWidth();
        int bitmap1Height = bitmap1.getHeight();
        int bitmap2Width = overlayBitmap.getWidth();
        int bitmap2Height = overlayBitmap.getHeight();

        float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);
        float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);

        Bitmap finalBitmap = Bitmap.createBitmap(bitmap1Width+ bitmap2Width, bitmap1Height, bitmap1.getConfig());
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(overlayBitmap, bitmap1Width, 0, null);
        return finalBitmap;
    }

    private void saveImg(Bitmap bitmap) {
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "Bird",
                "Image of bird"
        );

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(savedImageURL);
        Toast.makeText(MainActivity.this, "Image is saved", Toast.LENGTH_SHORT).show();
    }
}
