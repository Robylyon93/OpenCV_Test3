package bicoccalab.opencv_test3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btnOpenCamera;
    FloatingActionButton btnOpenGallery;
    Uri photoUri;
    String photoPath;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpenCamera = (FloatingActionButton) findViewById(R.id.opencamera_btn);
        btnOpenGallery = (FloatingActionButton) findViewById(R.id.opengallery_btn);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            btnOpenCamera.setEnabled(false);
            btnOpenGallery.setEnabled(false);
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnOpenCamera.setEnabled(true);
                btnOpenGallery.setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent displayPhotoIntent = new Intent(this, DisplayActivity.class);
        /**
         * In base alla richiesta vengono chiamati i metodi per l'avvio della fotocamera o l'apertura
         * della galleria.
         */
        if (requestCode == TAKE_PIC) {
            if (resultCode == RESULT_OK) {
                displayPhotoIntent.putExtra("photoUri", photoPath);
                startActivity(displayPhotoIntent); // si può scirvere una volta sola fuori dall'if!
            }
        }
        else if (requestCode == PICK_PIC) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                displayPhotoIntent.putExtra("photoUri", imageUri.toString());
                startActivity(displayPhotoIntent);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    // METODI DI SUPPORTO --------------------------------------------------------------------------

    private static final int TAKE_PIC = 100;

    /**
     * Creazione dell'intent per la chiamata della camera.
     * Creazione del file contente la fotografia e chiamata dell'intent CAMERA.
     * @param view view corrente
     */
    public void openCamera(View view) {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (openCameraIntent.resolveActivity(getPackageManager()) != null){
            // creazione del file in cui verrà messa la foto
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // bad things happened here...
            }
            // continua solamente se il File viene creato con successo
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        "bicoccalab.opencv_test3.fileprovider", photoFile);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(openCameraIntent, TAKE_PIC);
            }
        }
    }

    private static final int PICK_PIC = 200;

    /**
     * Metodo per l'apertura della galleria immagini del dispositivo.
     * @param view view corrente
     */
    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_PIC);
    }

    /**
     * Metodo per la creazione del File Immagine.
     * @return File creato
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ApeDemo");
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // il nome del file viene generato con il timestamp al momento della creazione
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = File.createTempFile(imageFileName, ".jpg", mediaStorageDir);
        photoPath = image.getAbsolutePath();
        return image;
    }


}