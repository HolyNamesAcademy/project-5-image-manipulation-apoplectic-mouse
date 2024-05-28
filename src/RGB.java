/**
 * This class encapsulates a red-green-blue representation of a pixel.
 */
public class RGB {
    private int red;
    private int green;
    private int blue;

    // Constructors

    /**
     * Default constructor, initializes channels to 0 (a black pixel)
     */
    public RGB() {
        red = 0;
        green=0;
        blue=0;
    }

    /**
     * Initializes a pixel with the given rgb values. Note this constructor
     * only creates valid pixels. Each channel value should be
     * between 0 and 255. If the provided channel value is less than 0,
     * the constructor sets the value to 0. If it is greater than 255,
     * the constructor sets the value to 255.
     * @param red red channel value
     * @param green green channel value
     * @param blue blue channel value
     */
    public RGB(int red, int green, int blue) {
        if(0<=red && red<=255){
            this.red = red;
        }
        else if(red < 0){
            this.red = 0;
        }
        else {
            this.red = 255;
        }
        if(0<=green && green<=255){
            this.green = green;
        }
        else if(green < 0){
            this.green = 0;
        }
        else {
            this.green = 255;
        }
        if(0<=blue && blue<=255){
            this.blue = blue;
        }
        else if(blue < 0){
            this.blue = 0;
        }
        else {
            this.blue = 255;
        }

    }

    // Getters

    public int GetRed() {
        return red;
    }

    public int GetGreen() {
        return green;
    }

    public int GetBlue() {
        return blue;
    }

    /*
    Setters
    NOTE: these setters should only allow valid channel values
    That means cases where a user tries to set channel value < 0
    or channel value > 255 should be handled properly.
     */
    public void SetRed(int red) {
        if(red>255){
            this.red = 255;
        }
        else if(red<0){
            this.red = 0;
        }
        else{
           this.red = red;
        }
    }

    public void SetGreen(int green) {
        if(green>255){
            this.green = 255;
        }
        else if(green<0){
            this.green = 0;
        }
        else{
            this.green = green;
        }
    }

    public void SetBlue(int blue) {
        if(blue>255){
            this.blue = 255;
        }
        else if(blue<0){
            this.blue = 0;
        }
        else{
            this.blue = blue;
        }
    }

    /**
     * Converts an RGB values to HSL values.
     * Here's some info on how the conversion works:
     *  https://math.stackexchange.com/questions/556341/rgb-to-hsv-color-conversion-algorithm
     * @return HSL representation of the pixel
     */
    public HSL ConvertToHSL() {
        double r = red / (double) 255;
        double g = green / (double) 255;
        double b = blue / (double) 255;

        double max = Math.max(r, Math.max(g, b));
        double min = Math.min(r, Math.min(g, b));

        double l = (max + min) / 2;
        double delta = max - min;

        double h = 0, s = 0;
        if (delta < 0.00001)
        {
            h = 0;
            s = 0;
        }
        else {
            if (max == r) {
                h = (g - b) / delta + (g < b ? 6 : 0);
            }
            else if (max == g) {
                h = (b - r) / delta + 2;
            }
            else if (max == b) {
                h = (r - g) / delta + 4;
            }
            h *= 60;

            s = l > 0.5 ? delta / (2 - max - min) : delta / (max + min);
        }

        return new HSL((int) h, s, l);
    }
}