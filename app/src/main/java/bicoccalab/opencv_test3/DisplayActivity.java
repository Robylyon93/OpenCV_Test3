package bicoccalab.opencv_test3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class DisplayActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri photoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        imageView = (ImageView) findViewById(R.id.imageViewDisplay);
        Intent i = getIntent();
        photoUri = Uri.parse(i.getExtras().getString("photoUri"));
        imageView.setImageURI(photoUri);
    }

    public void test(View v) {
        Intent optionsIntent = new Intent(this, OptionActivity.class);
        optionsIntent.putExtra("photoUri", photoUri.toString());

        optionsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); //impedisce che ci sia l'animazione di slide
        startActivity(optionsIntent);
    }

}
