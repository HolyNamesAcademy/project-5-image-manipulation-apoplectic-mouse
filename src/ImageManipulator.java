import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Comparator;
/**
 * Static utility class that is responsible for transforming the images.
 * Each function (or at least most functions) take in an Image and return
 * a transformed image.
 */
public class ImageManipulator {
    /**
     * Loads the image at the given path
     * @param path path to image to load
     * @return an Img object that has the given image loaded
     * @throws IOException
     */
    public static Img LoadImage(String path) throws IOException {
        Img picture = new Img(path);
        return picture;
    }

    /**
     * Saves the image to the given file location
     * @param image image to save
     * @param path location in file system to save the image
     * @throws IOException
     */
    public static void SaveImage(Img image, String path) throws IOException {
        image.Save("png", path);
    }

    /**
     * Converts the given image to grayscale (black, white, and gray). This is done
     * by finding the average of the RGB channel values of each pixel and setting
     * each channel to the average value.
     * @param image image to transform
     * @return the image transformed to grayscale
     */
    public static Img ConvertToGrayScale(Img image) {
        int avg;
        RGB pixel = new RGB();
        for(int x=0; x<image.GetWidth(); x++){
            for(int y=0; y<image.GetHeight(); y++){
             pixel = image.GetRGB(x, y);
             avg = (pixel.GetBlue()+ pixel.GetGreen()+pixel.GetRed())/3;
             pixel.SetBlue(avg);
             pixel.SetRed(avg);
             pixel.SetGreen(avg);
             image.SetRGB(x, y, pixel);

            }
        }
        return image;
    }

    /**
     * Inverts the image. To invert the image, for each channel of each pixel, we get
     * its new value by subtracting its current value from 255. (r = 255 - r)
     * @param image image to transform
     * @return image transformed to inverted image
     */
    public static Img InvertImage(Img image) {
        int newred;
        int newblue;
        int newgreen;
        RGB pixel = new RGB();
        for(int x=0; x<image.GetWidth(); x++){
            for(int y=0; y<image.GetHeight(); y++){
                pixel = image.GetRGB(x, y);
                pixel.SetRed(255-pixel.GetRed());
                pixel.SetBlue(255-pixel.GetBlue());
                pixel.SetGreen(255-pixel.GetGreen());
                image.SetRGB(x, y, pixel);

            }
        }
        return image;
    }

    /**
     * Converts the image to sepia. To do so, for each pixel, we use the following equations
     * to get the new channel values:
     * r = .393r + .769g + .189b
     * g = .349r + .686g + .168b
     * b = 272r + .534g + .131b
     * @param image image to transform
     * @return image transformed to sepia
     */
    public static Img ConvertToSepia(Img image) {
        int newred;
        int newblue;
        int newgreen;
        RGB pixel = new RGB();
        for(int x=0; x<image.GetWidth(); x++){
            for(int y=0; y<image.GetHeight(); y++){
                pixel = image.GetRGB(x, y);
                newblue = (int)pixel.GetBlue();
                newgreen = (int)pixel.GetGreen();
                newred = (int)pixel.GetRed();
                pixel.SetRed((int)(0.393*newred + 0.769*newgreen + 0.189*newblue));
                pixel.SetGreen((int)(.349*newred + .686*newgreen + .168*newblue));
                pixel.SetBlue((int)(.272*newred+.534*newgreen + .131*newgreen));
                image.SetRGB(x, y, pixel);

            }
        }
        return image;

    }

    /**
     * Creates a stylized Black/White image (no gray) from the given image. To do so:
     * 1) calculate the luminance for each pixel. Luminance = (.299 r^2 + .587 g^2 + .114 b^2)^(1/2)
     * 2) find the median luminance
     * 3) each pixel that has luminance >= median_luminance will be white changed to white and each pixel
     *      that has luminance < median_luminance will be changed to black
     * @param image image to transform
     * @return black/white stylized form of image
     */
    public static Img ConvertToBW(Img image) {
        ArrayList<Double> lums = new ArrayList<Double>();
        int middleval;
        double medianlum;
        RGB pixel = new RGB();
        double pixelLums;
        double thisPixelLums;
        for(int x = 0; x<image.GetWidth(); x++){
            for(int y=0; y<image.GetHeight(); y++){
               pixel = image.GetRGB(x, y);
               pixelLums = Math.pow(0.299*Math.pow(pixel.GetRed(), 2)+0.587*Math.pow(pixel.GetGreen(), 2)+0.114*Math.pow(pixel.GetBlue(), 2), 0.5);
               lums.add(pixelLums);
            }
        }
        lums.sort(Comparator.naturalOrder());
        middleval = (int)(0.5+lums.size()/2.0);
        medianlum = lums.get(middleval);

        for(int x = 0; x<image.GetWidth(); x++){
            for(int y =0; y<image.GetHeight(); y++){
             pixel = image.GetRGB(x, y);
             thisPixelLums = Math.pow((0.299*Math.pow(pixel.GetRed(), 2)+0.587*Math.pow(pixel.GetGreen(), 2)+0.114*Math.pow(pixel.GetBlue(), 2)), 0.5);
            if(thisPixelLums>=medianlum){
                pixel.SetRed(255);
                pixel.SetBlue(255);
                pixel.SetGreen(255);
            }
            else{
                pixel.SetRed(0);
                pixel.SetBlue(0);
                pixel.SetGreen(0);
            }
            image.SetRGB(x, y, pixel);
            }
        }
return image;
    }

    /**
     * Rotates the image 90 degrees clockwise.
     * @param image image to transform
     * @return image rotated 90 degrees clockwise
     */
    //outofbounds exception. think im trying to apply to pixel that's not there
    public  static Img RotateImage(Img image) {
        Img rotated = new Img(image.GetHeight(), image.GetWidth());
        RGB pixel = new RGB();
        for(int y = 0; y<image.GetHeight(); y++){
            for(int x=0; x<image.GetWidth(); x++){
                pixel = image.GetRGB(x, y);
                rotated.SetRGB(image.GetHeight()-1-y, x, pixel);
            }
        }
        return rotated;
    }

    /**
     * Applies an Instagram-like filter to the image. To do so, we apply the following transformations:
     * 1) We apply a "warm" filter. We can produce warm colors by reducing the amount of blue in the image
     *      and increasing the amount of red. For each pixel, apply the following transformation:
     *          r = r * 1.2
     *          g = g
     *          b = b / 1.5
     * 2) We add a vignette (a black gradient around the border) by combining our image with an
     *      an image of a halo (you can see the image at resources/halo.png). We take 65% of our
     *      image and 35% of the halo image. For example:
     *          r = .65 * r_image + .35 * r_halo
     * 3) We add decorative grain by combining our image with a decorative grain image
     *      (resources/decorative_grain.png). We will do this at a .95 / .5 ratio.
     * @param image image to transform
     * @return image with a filter
     * @throws IOException
     */
    public static Img InstagramFilter(Img image) throws IOException {
        Img halo = new Img("C:\\Users\\embin\\IdeaProjects\\project-5-image-manipulation-apoplectic-mouse\\resources\\halo.png");
        Img decg = new Img("C:\\Users\\embin\\IdeaProjects\\project-5-image-manipulation-apoplectic-mouse\\resources\\decorative_grain.png");
        RGB pixel = new RGB();
        RGB haloGrainPixel = new RGB();
        //strat: figure out how to call loadimage to be able to access halo and decg
        //average out the three images w multiplication factors on halograinpixel
        for(int x=0; x<image.GetWidth(); x++) {
            for (int y = 0; y < image.GetHeight(); y++) {
                pixel = image.GetRGB(x, y);
                pixel.SetBlue((int) (pixel.GetBlue() / 1.5));
                pixel.SetRed((int) (pixel.GetRed() * 1.2));
                image.SetRGB(x, y, pixel);
            }
        }
        for(int x=0; x<image.GetWidth(); x++){
            for(int y=0; y<image.GetHeight(); y++){

            }
        }
        return halo;
    }

    /**
     * Sets the given hue to each pixel image. Hue can range from 0 to 360. We do this
     * by converting each RGB pixel to an HSL pixel, Setting the new hue, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param hue amount of hue to add
     * @return image with added hue
     */
    public static Img SetHue(Img image, int hue) {

        for(int x = 0; x<image.GetWidth(); x++ ){
            for(int y = 0; y< image.GetHeight(); y++){
              RGB pixel=image.GetRGB(x, y);
              HSL pixel1 = pixel.ConvertToHSL();
              pixel1.SetHue(hue);
              image.SetRGB(x, y, pixel1.GetRGB());
            }
        }
        return image;
    }

    /**
     * Sets the given saturation to the image. Saturation can range from 0 to 1. We do this
     * by converting each RGB pixel to an HSL pixel, setting the new saturation, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param saturation amount of saturation to add
     * @return image with added hue
     */
    public static Img SetSaturation(Img image, double saturation) {
        for(int x = 0; x<image.GetWidth(); x++ ){
            for(int y = 0; y< image.GetHeight(); y++){
                RGB pixel=image.GetRGB(x, y);
                HSL pixel1 = pixel.ConvertToHSL();
                pixel1.SetSaturation(saturation);
                image.SetRGB(x, y, pixel1.GetRGB());
            }
        }
        return image;
    }

    /**
     * Sets the lightness to the image. Lightness can range from 0 to 1. We do this
     * by converting each RGB pixel to an HSL pixel, setting the new lightness, and then
     * converting each pixel back to an RGB pixel.
     * @param image image to transform
     * @param lightness amount of hue to add
     * @return image with added hue
     */
    public static Img SetLightness(Img image, double lightness) {
        for(int x = 0; x<image.GetWidth(); x++ ){
            for(int y = 0; y< image.GetHeight(); y++){
                RGB pixel=image.GetRGB(x, y);
                HSL pixel1 = pixel.ConvertToHSL();
                pixel1.SetLightness(lightness);
                image.SetRGB(x, y, pixel1.GetRGB());
            }
        }
        return image;
    }
}
