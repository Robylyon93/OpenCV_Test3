package Filters;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import static org.opencv.android.Utils.bitmapToMat;
import static org.opencv.android.Utils.matToBitmap;
import static org.opencv.core.CvType.CV_8U;
import static org.opencv.imgproc.Imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
import static org.opencv.imgproc.Imgproc.blur;
import static org.opencv.imgproc.Imgproc.initUndistortRectifyMap;
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

    public static Bitmap cartoonizeApply(Bitmap image) {

        // Cartoon Style
        // Ho ancora dei problemi con il bitwise and, mi da errore sulle dimensioni delle mat ma non capisco il perchè...

        Mat img_rgb = new Mat();
        bitmapToMat(image, img_rgb);
        Imgproc.cvtColor(img_rgb, img_rgb, Imgproc.COLOR_RGBA2RGB);
        Mat img_gray = new Mat();
        Mat img_blur = new Mat();

        // STEP 1: Edge-aware smoothing using a bilateral filter

        Size mask_s = new Size(4,4);

        Mat img_color = img_rgb;
        Imgproc.blur(img_color,img_color, mask_s);

        // STEP 2: Reduce noise using a median filter

        // convert to grayscale and apply median blur
        Imgproc.cvtColor(img_rgb, img_gray, Imgproc.COLOR_RGB2GRAY);
        Imgproc.medianBlur(img_gray, img_blur, 7);

        // STEP 3: Create an edge mask using adaptive thresholding

        Mat img_edge = new Mat();
        // detect and enhance edges
        Imgproc.adaptiveThreshold(img_blur, img_edge, 255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY,
                9,
                2);

        // STEP 4: Combine color image with edge mask

        Mat img_cartoon = img_color;
        Mat res = new Mat();
        // convert back to color, bit-AND with color image
        Imgproc.cvtColor(img_edge, img_edge, Imgproc.COLOR_GRAY2RGB);
        Log.e("Cartoonize", img_color.size().toString() +"; "+ img_edge.size().toString() );
        Core.bitwise_and(img_color, img_edge, img_cartoon);

        Utils.matToBitmap(img_cartoon, image);
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
