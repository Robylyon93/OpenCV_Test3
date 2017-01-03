package bicoccalab.opencv_test3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import Filters.FilterUtils;

public class DisplayActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri photoUri;
    private Uri newUri;
    private Bitmap newImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);


        imageView = (ImageView) findViewById(R.id.imageViewDisplay);
        Intent i = getIntent();
        photoUri = Uri.parse(i.getExtras().getString("photoUri"));
        imageView.setImageURI(photoUri);
    }

    public void onClick(View v) throws FileNotFoundException {

        // In base all'id del bottone premuto vengono chiamate i corrispondenti filtri
        if(v.getId() == R.id.grayScale) {

            newImage = FilterUtils.grayApply(((BitmapDrawable) imageView.getDrawable()).getBitmap());

        }
        else if(v.getId() == R.id.stylization) {

            newImage = FilterUtils.stylizationApply(((BitmapDrawable) imageView.getDrawable()).getBitmap());

        }

        imageView.setImageBitmap(newImage);
//
//        String destFolder = getCacheDir().getAbsolutePath();
//
//        FileOutputStream out = new FileOutputStream(destFolder + "/temp_image.png");
//        newImage.compress(Bitmap.CompressFormat.PNG, 100, out);
//
//        String newPath = destFolder + "/temp_image.png";
//
//        Intent optionsIntent = new Intent(this, OptionActivity.class);
//        optionsIntent.putExtra("photoUri", newPath);
//
//        optionsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); //impedisce che ci sia l'animazione di slide
//        startActivity(optionsIntent);


    }

}
