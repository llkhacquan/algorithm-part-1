/******************************************************************************
 *  Compilation:  javac PrintEnergy.java
 *  Execution:    java PrintEnergy input.png
 *  Dependencies: SeamCarver.java
 *                
 *
 *  Read image from file specified as command line argument. Print energy
 *  of each pixel as calculated by SeamCarver object. 
 * 
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class PrintEnergy {

    public static void main(String[] args) {
        Picture picture = new Picture("part2-week2/seamCarving/6x5.png");
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        for (int row = 0; row < picture.height(); row++) {
            for (int col = 0; col < picture.width(); col++)
                StdOut.printf("%3d %3d %3d\t\t", picture.get(col, row).getAlpha(), picture.get(col, row).getBlue(), picture.get(col, row).getRed());
            StdOut.println();
        }

        SeamCarver sc = new SeamCarver(picture);



        StdOut.printf("Printing energy calculated for each pixel.\n");

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
            StdOut.println();
        }
        StdOut.println(sc.findVerticalSeam());
    }

}
