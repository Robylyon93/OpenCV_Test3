package bicoccalab.opencv_test3;

import android.content.Context;
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
import android.widget.Toast;

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

    /**
     * Metodo onClick in base al chiamante varia il tipo di operazione. Attualmente salvataggio e
     * applicazione filtri.
     * @param v view del bottone
     * @throws FileNotFoundException
     */
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
//          Commentati per ora nessun filtro ha bisogno della activity opzioni.
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

    /**
     * Salvataggio dell'immagine che viene passata per parametro.
     * La cartella di salvataggio è attualmente sdcard/Pictures/ApeDemo
     * @param image Bitmap da salvare, verrà compressa come jpeg
     */

    private void SaveImage(Bitmap image) {

        File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ApeDemo");  //Environment.getExternalStorageDirectory() + "/ApeDemo");


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + "_APE.jpeg";
        // String fileName = "Test.jpeg";

        if (!direct.exists()) {

            direct.mkdir();

            //File myDir = new File("/sdcard/Picture/ApeDemo/");
            //myDir.mkdirs();
        }

        File file = new File(direct, fileName);
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

            // toast di notifica salvataggio riuscito
            Context context = getApplicationContext();
            CharSequence text = "Image saved!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
