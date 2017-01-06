package bicoccalab.opencv_test3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import Filters.FilterUtils;

public class DisplayActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri photoUri;
    private Bitmap originalImg;
    private Bitmap newImage;
    private FloatingActionButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        saveButton = (FloatingActionButton) findViewById(R.id.saveImg);
        saveButton.setEnabled(false);
        imageView = (ImageView) findViewById(R.id.imageViewDisplay);
        Intent i = getIntent();
        photoUri = Uri.parse(i.getExtras().getString("photoUri"));
        imageView.setImageURI(photoUri);
        originalImg = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public void onClick(View v) throws FileNotFoundException {

        if(v.getId() == R.id.saveImg){
            this.SaveImage(this.newImage);
        }
        else {

            // In base all'id del bottone premuto vengono chiamate i corrispondenti filtri
            if (v.getId() == R.id.grayScale) {

                newImage = FilterUtils.grayApply(originalImg);

            } else if (v.getId() == R.id.stylization) {

                newImage = FilterUtils.stylizationApply(originalImg);

            } else if (v.getId() == R.id.pencil) {
                newImage = FilterUtils.pencilApply(originalImg);
            }

            saveButton.setEnabled(true);
            imageView.setImageBitmap(newImage);
//
//          String destFolder = getCacheDir().getAbsolutePath();
//
//          FileOutputStream out = new FileOutputStream(destFolder + "/temp_image.png");
//          newImage.compress(Bitmap.CompressFormat.PNG, 100, out);
//
//          String newPath = destFolder + "/temp_image.png";
//
//          Intent optionsIntent = new Intent(this, OptionActivity.class);
//          optionsIntent.putExtra("photoUri", newPath);
//
//          optionsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); //impedisce che ci sia l'animazione di slide
//          startActivity(optionsIntent);

        }
    }


    private void SaveImage(Bitmap image) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/ApeDemo");

        String fileName = "Test";

        if (!direct.exists()) {
            File myDir = new File("/sdcard/ApeDemo/");
            myDir.mkdirs();
        }

        File file = new File(new File("/sdcard/ApeDemo/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            // Forza il rescan dei media (per visualizzare nella galleria)
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
