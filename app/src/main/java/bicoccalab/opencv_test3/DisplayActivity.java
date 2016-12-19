package bicoccalab.opencv_test3;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class DisplayActivity extends AppCompatActivity {

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        imageView = (ImageView) findViewById(R.id.imageViewDisplay);
        Intent i = getIntent();
        imageView.setImageURI(Uri.parse(i.getExtras().getString("photoUri")));
    }
}
