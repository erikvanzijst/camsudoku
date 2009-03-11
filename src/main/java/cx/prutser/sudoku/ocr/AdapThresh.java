package cx.prutser.sudoku.ocr;

import java.util.Arrays;

/**
 * Shamelessly taken from http://homepages.inf.ed.ac.uk/rbf/HIPR2/flatjavasrc/AdapThresh.java
 * and slightly tweaked for performance.
 *
 * Also see: http://homepages.inf.ed.ac.uk/rbf/HIPR2
 */
public class AdapThresh {

    //the width and height of this image in pixels
    private int i_w, i_h;

    //pixel arrays for input and output images
    private int[] src_1d;
    private int[] dest_1d;

    /**
     * Applies the adaptive thresholding operator to the specified image array
     * using the mean function to find the threshold value
     *
     * @param src    pixel array representing image to be thresholded
     * @param width  width of the image in pixels
     * @param height height of the image in pixels
     * @param size   the size of the neigbourhood used in finding the threshold
     * @param con    the constant value subtracted from the mean
     * @return a thresholded pixel array of the input image array
     */

    public int[] mean_thresh(int[] src, int width, int height, int size,
                             int con) {

        i_w = width;
        i_h = height;
        src_1d = new int[i_w * i_h];
        dest_1d = new int[i_w * i_h];
        int mean = 0;
        int count = 0;
        src_1d = src;
        int[][] tmp_2d = new int[i_w][i_h];

        //First convert input array from 1_d to 2_d for ease of processing
        for (int i = 0; i < i_w; i++) {
            for (int j = 0; j < i_h; j++) {
                tmp_2d[i][j] = src_1d[i + (j * i_w)] & 0x000000ff;
            }
        }

        final int HALF_SIZE = size / 2;
        //Now find the sum of values in the size X size neigbourhood
        for (int j = 0; j < i_h; j++) {
            for (int i = 0; i < i_w; i++) {
                mean = 0;
                count = 0;
                //Check the local neighbourhood
                for (int k = 0; k < size; k++) {
                    for (int l = 0; l < size; l++) {

                        int x = i - HALF_SIZE + k;
                        int y = j - HALF_SIZE + l;
                        //If out of bounds then ignore pixel
                        if (x >= 0 && x < i_w && y >= 0 && y < i_h) {
                            mean = mean + tmp_2d[x][y];
                            count++;
                        }
                    }
                }
                //Find the mean value
                mean = (mean / count) - con;

                //Threshold below the mean
                if (tmp_2d[i][j] > mean) {
                    dest_1d[i + (j * i_w)] = 0xffffffff;
                } else {
                    dest_1d[i + (j * i_w)] = 0xff000000;
                }
            }
        }
        return dest_1d;
    }

    /**
     * Applies the adaptive thresholding operator to the specified image array
     * using the median function to find the threshold value
     *
     * @param src    pixel array representing image to be thresholded
     * @param width  width of the image in pixels
     * @param height height of the image in pixels
     * @param size   the size of the neigbourhood used in finding the threshold
     * @param con    the constant value subtracted from the median
     * @return a thresholded pixel array of the input image array
     */

    public int[] median_thresh(int[] src, int width, int height, int size,
                               int con) {

        i_w = width;
        i_h = height;
        src_1d = new int[i_w * i_h];
        dest_1d = new int[i_w * i_h];
        int median = 0;
        int[] values = new int[size * size];
        int count = 0;
        src_1d = src;
        int[][] tmp_2d = new int[i_w][i_h];

        //First convert input array from 1_d to 2_d for ease of processing
        for (int i = 0; i < i_w; i++) {
            for (int j = 0; j < i_h; j++) {
                tmp_2d[i][j] = src_1d[i + (j * i_w)] & 0x000000ff;
            }
        }

        //Now find the values in the size X size neigbourhood
        for (int j = 0; j < i_h; j++) {
            for (int i = 0; i < i_w; i++) {
                median = 0;
                count = 0;
                values = new int[size * size];
                //Check the local neighbourhood
                for (int k = 0; k < size; k++) {
                    for (int l = 0; l < size; l++) {
                        try {
                            values[count] = tmp_2d[(i - ((int) (size / 2)) + k)]
                                    [(j - ((int) (size / 2)) + l)];
                            count++;
                        }
                        //If out of bounds then ignore pixel
                        catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                }
                //Find the median value

                //First Sort the array
                Arrays.sort(values);

                //Then select the median
                count = count / 2;
                median = values[count] - con;

                //Threshold below the median
                if (tmp_2d[i][j] >= median) {
                    dest_1d[i + (j * i_w)] = 0xffffffff;
                } else {
                    dest_1d[i + (j * i_w)] = 0xff000000;
                }
            }
        }
        return dest_1d;
    }

    /**
     * Applies the adaptive thresholding operator to the specified image array
     * using the mean of max & min function to find the threshold value
     *
     * @param src    pixel array representing image to be thresholded
     * @param width  width of the image in pixels
     * @param height height of the image in pixels
     * @param size   the size of the neigbourhood used in finding the threshold
     * @param con    the constant value subtracted from the mean
     * @return a thresholded pixel array of the input image array
     */

    public int[] meanMaxMin_thresh(int[] src, int width, int height, int size,
                                   int con) {

        i_w = width;
        i_h = height;
        src_1d = new int[i_w * i_h];
        dest_1d = new int[i_w * i_h];
        int mean = 0;
        int max, min;
        int[] values = new int[size * size];
        src_1d = src;
        int[][] tmp_2d = new int[i_w][i_h];

        //First convert input array from 1_d to 2_d for ease of processing
        for (int i = 0; i < i_w; i++) {
            for (int j = 0; j < i_h; j++) {
                tmp_2d[i][j] = src_1d[i + (j * i_w)] & 0x000000ff;
            }
        }

        int tmp;

        //Now find the max and min of values in the size X size neigbourhood
        for (int j = 0; j < i_h; j++) {
            for (int i = 0; i < i_w; i++) {
                mean = 0;
                max = tmp_2d[i][j];
                min = tmp_2d[i][j];
                //Check the local neighbourhood
                for (int k = 0; k < size; k++) {
                    for (int l = 0; l < size; l++) {
                        try {
                            tmp = tmp_2d[(i - ((int) (size / 2)) + k)]
                                    [(j - ((int) (size / 2)) + l)];
                            if (tmp > max) {
                                max = tmp;
                            }
                            if (tmp < min) {
                                min = tmp;
                            }
                        }
                        //If out of bounds then ignore pixel
                        catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                }
                //Find the mean value

                tmp = max + min;
                tmp = tmp / 2;
                mean = tmp - con;

                //Threshold below the mean
                if (tmp_2d[i][j] >= mean) {
                    dest_1d[i + (j * i_w)] = 0xffffffff;
                } else {
                    dest_1d[i + (j * i_w)] = 0xff000000;
                }
            }
        }
        return dest_1d;
    }
}
