package Filters;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import static org.opencv.android.Utils.bitmapToMat;
import static org.opencv.android.Utils.matToBitmap;
import static org.opencv.core.CvType.CV_8U;

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

        Mat imageMat = new Mat();
        bitmapToMat(image, imageMat);
        Mat dst = new Mat();


        imageMat.convertTo(imageMat, CV_8U);

        Photo.stylization(imageMat, dst);

        matToBitmap(dst, image);

        return image;
    }
}
