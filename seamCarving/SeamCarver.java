import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.lang.*;
import java.util.*;

public class SeamCarver {

    private Picture picture;
    private int width;
    private int height;
    private boolean transpose;
    public SeamCarver(Picture picture) {
        width = picture.width();
        height = picture.height();
        this.picture = new Picture(picture);
        transpose = false;

    }               
    // create a seam carver object based on the given picture
    public Picture picture() {
        if (transpose) transpose();
        return new Picture(picture);
    }                         
    // current picture
    public     int width() {
        return width;
    }                           
    // width of current picture
    public     int height() {
        return height;
    }                          
    // height of current picture
    public  double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) throw new java.lang.IndexOutOfBoundsException();
        if (x == 0 || y == 0 || x == (width() - 1) || y == (height() - 1)) return 1000;
        return Math.sqrt(centralDifference(picture.get(x, y-1), picture.get(x, y+1)) + centralDifference(picture.get(x+1, y), picture.get(x-1, y)));
    }              
    // energy of pixel at column x and row y
    public   int[] findHorizontalSeam() {
        if (transpose) {
            return findVerticalSeam();
        }
        else {
            transpose();
            return findVerticalSeam();
        }
    }              
    // sequence of indices for horizontal seam
    public   int[] findVerticalSeam() {
        double[][] energy = new double[width()][height()];
        double[][] distanceTo = new double[width()][height()];
        int[][] pathTo = new int[width()][height()];
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                energy[i][j] = energy(i, j);
                if (j == 0) {
                    distanceTo[i][j] = energy[i][j];
                    pathTo[i][j] = -1;
                }
                else {
                    double smallest = distanceTo[i][j - 1];
                    pathTo[i][j] = i;
                    if (i != 0 && distanceTo[i - 1][j - 1] < smallest) {
                        pathTo[i][j] = i - 1;
                        smallest = distanceTo[i - 1][j - 1];
                    }
                    if (i != (width() - 1) && distanceTo[i + 1][j - 1] < smallest) {
                        pathTo[i][j] = i + 1;
                        smallest = distanceTo[i + 1][j - 1];
                    }
                    distanceTo[i][j] = smallest + energy[i][j];
                }
            }
        }
        Stack<Integer> result = new Stack<Integer>();
        double maxi = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < width(); i++) {
            if (distanceTo[i][height() - 1] < maxi) {
                index = i;
                maxi = distanceTo[i][height() - 1];
            }
        }
        int h = height() - 1;
        while (index != -1) {
            result.push(index);
            index = pathTo[index][h];
            h--;
        }
        int[] seam = new int[height()];
        int count = 0;
        while (!result.empty()) {
            seam[count] = result.pop();
            count++;
        }
        if (transpose) transpose();
        return seam;
    }

    // sequence of indices for vertical seam
    public    void removeHorizontalSeam(int[] seam) {
        if (seam.length != width) throw new java.lang.IllegalArgumentException();
        if (seam[0] < 0 || seam[0] >= height) throw new java.lang.IllegalArgumentException();
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1) throw new java.lang.IllegalArgumentException();
            if (seam[i] < 0 || seam[i] >= height) throw new java.lang.IllegalArgumentException();
        }
        Color[][] color = new Color[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                color[i][j] = picture.get(i, j);
            }
        }
        Color[][] temp = new Color[width][height - 1];
        for (int i = 0; i < width; i++) {
            System.arraycopy(color[i], 0, temp[i], 0, seam[i]);
            System.arraycopy(color[i], seam[i] + 1, temp[i], seam[i], height - 1 - seam[i]);
        }
        height--;
        picture = new Picture(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                picture.set(i, j, temp[i][j]);
            }
        }
    }  
    // remove horizontal seam from current picture
    public    void removeVerticalSeam(int[] seam) {
        transpose();
        removeHorizontalSeam(seam);
        transpose();
    }    
    // remove vertical seam from current picture
    private double centralDifference(Color x, Color y) {
        return Math.pow(x.getRed()-y.getRed(), 2) + Math.pow(x.getGreen()-y.getGreen(), 2) + Math.pow(x.getBlue()-y.getBlue(), 2);
    }
    private void transpose() {
        Picture temp = new Picture(height, width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                temp.set(i, j, picture.get(j, i));
            }
        }
        this.picture = temp;
        int tempo = height;
        height = width;
        width = tempo;
        transpose = !transpose;
    }
}
