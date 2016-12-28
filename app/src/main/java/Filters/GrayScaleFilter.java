package Filters;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.getDefaultNewCameraMatrix;


/**
 * Created by Simone on 28/12/2016.
 */

public class GrayScaleFilter {


    public static Bitmap applyFilter(Bitmap image){
        Mat inImage = new Mat();

        Utils.bitmapToMat(image,inImage);

        Mat outputImage = new Mat();


        cvtColor(inImage, outputImage, COLOR_RGB2GRAY);

        Bitmap imageOut = image;

        Utils.matToBitmap(outputImage, imageOut);

        return imageOut;
    }

}
