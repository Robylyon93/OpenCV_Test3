package Filters;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import static org.opencv.android.Utils.bitmapToMat;
import static org.opencv.android.Utils.matToBitmap;
import static org.opencv.core.CvType.CV_8U;
import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
import static org.opencv.imgproc.Imgproc.resize;

public class FilterUtils {


    public static Bitmap grayApply(Bitmap image){

        Mat imageMat = new Mat();
        bitmapToMat(image,imageMat);
        Mat dst = new Mat();

        Imgproc.cvtColor(imageMat, dst, Imgproc.COLOR_BGR2GRAY);

        Utils.matToBitmap(dst,image);

        return image;
    }

    public static Bitmap stylizationApply(Bitmap image) {

        // Cartoon Style
        // Ho ancora dei problemi con il bitwise and, mi da errore sulle dimensioni delle mat ma non capisco il perchè...

        Mat imageMat = new Mat();
        bitmapToMat(image, imageMat);
        Mat imageGray = new Mat();
        Mat dst = new Mat();
        Mat edges = new Mat();
        Mat mask = new Mat();

        // convert to grayscale and apply median blur
        Imgproc.cvtColor(imageMat,imageGray, Imgproc.COLOR_BGR2GRAY);

        Mat imageBlur = new Mat();
        Imgproc.medianBlur(imageGray,imageBlur,3);

        // detect and enhance edges
        Imgproc.adaptiveThreshold(imageBlur,edges, 255, ADAPTIVE_THRESH_MEAN_C,
                                THRESH_BINARY, 9, 2);

        // convert back to color so that it can be bit-ANDed with color image
        Mat edges_rgb = new Mat();
        Imgproc.cvtColor(edges, edges_rgb, Imgproc.COLOR_GRAY2RGB);
        resize(edges_rgb,edges_rgb, imageMat.size());
        Log.i("Filters", edges_rgb.size().toString() + "," + imageMat.size().toString());
        Core.bitwise_and(edges_rgb,imageMat, dst);


        matToBitmap(dst, image);

        return image;
    }

    public static Bitmap pencilApply(Bitmap image) {
        Mat imageMat = new Mat();
        bitmapToMat(image, imageMat);
        Mat dst = new Mat();
        Mat dstGray = new Mat();

        Photo.pencilSketch(imageMat,dstGray,dst,60,0.02f,0.03f);

        matToBitmap(dst, image);
        return image;
    }
}
