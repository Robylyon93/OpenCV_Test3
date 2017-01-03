package bicoccalab.opencv_test3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;

public class OptionActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri photoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        imageView = (ImageView) findViewById(R.id.imageViewDisplay);
        Intent i = getIntent();
        photoUri = Uri.parse(i.getExtras().getString("photoUri")); // recupera dall'intent lo uri dell'immagine modificata (temporanea!)

        imageView.setImageURI(photoUri);
    }

    // aggiunto per eliminare la transizione alla pressione del tasto back
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
