import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validate(x, 0, width() - 1);
        validate(y, 0, height() - 1);
        // we define pixel at the border's energy to be 1000
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return 1000;

        Color color1 = picture.get(x - 1, y);
        Color color2 = picture.get(x + 1, y);
        double deltaX2 = square(color1.getRed() - color2.getRed()) + square(
                color1.getBlue() - color2.getBlue()) + square(
                color1.getGreen() - color2.getGreen());

        color1 = picture.get(x, y - 1);
        color2 = picture.get(x, y + 1);
        double deltaY2 = square(color1.getRed() - color2.getRed()) + square(
                color1.getBlue() - color2.getBlue()) + square(
                color1.getGreen() - color2.getGreen());
        return Math.sqrt(deltaX2 + deltaY2);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] energies = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energies[i][j] = energy(j, i);
            }
        }

        double[][] totalEnergies = new double[height()][width()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                if (col == 0)
                    totalEnergies[row][col] = energies[row][col];
                else {
                    double tempParentMin = totalEnergies[row][col - 1];
                    if (row >= 1 && totalEnergies[row - 1][col - 1] < tempParentMin)
                        tempParentMin = totalEnergies[row - 1][col - 1];

                    if (row < height() - 1 && totalEnergies[row + 1][col - 1] < tempParentMin)
                        tempParentMin = totalEnergies[row + 1][col - 1];

                    totalEnergies[row][col] = tempParentMin + energies[row][col];
                }
            }
        }

        int[] seam = new int[width()];
        seam[width() - 1] = 0;
        for (int row = 1; row < height(); row++) {
            if (totalEnergies[row][width() - 1] < totalEnergies[seam[width() - 1]][width() - 1])
                seam[width() - 1] = row;
        }
        for (int col = width() - 2; col >= 0; col--) {
            int minRow = seam[col + 1];
            if (seam[col + 1] >= 1
                    && totalEnergies[seam[col + 1] - 1][col] < totalEnergies[minRow][col])
                minRow = seam[col + 1] - 1;
            if (seam[col + 1] < height() - 1
                    && totalEnergies[seam[col + 1] + 1][col] < totalEnergies[minRow][col])
                minRow = seam[col + 1] + 1;
            seam[col] = minRow;
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energies = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energies[i][j] = energy(j, i);
            }
        }

        double[][] totalEnergies = new double[height()][width()];
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (row == 0)
                    totalEnergies[row][col] = energies[row][col];
                else {
                    double tempParentMin = totalEnergies[row - 1][col];
                    if (col >= 1 && totalEnergies[row - 1][col - 1] < tempParentMin)
                        tempParentMin = totalEnergies[row - 1][col - 1];

                    if (col < width() - 1 && totalEnergies[row - 1][col + 1] < tempParentMin)
                        tempParentMin = totalEnergies[row - 1][col + 1];

                    totalEnergies[row][col] = tempParentMin + energies[row][col];
                }
            }
        }

        int[] seam = new int[height()];
        seam[height() - 1] = 0;
        for (int col = 1; col < width(); col++) {
            if (totalEnergies[height() - 1][col] < totalEnergies[height() - 1][seam[height() - 1]])
                seam[height() - 1] = col;
        }
        for (int row = height() - 2; row >= 0; row--) {
            int minCol = seam[row + 1];
            if (seam[row + 1] >= 1
                    && totalEnergies[row][seam[row + 1] - 1] < totalEnergies[row][minCol])
                minCol = seam[row + 1] - 1;
            if (seam[row + 1] < width() - 1
                    && totalEnergies[row][seam[row + 1] + 1] < totalEnergies[row][minCol])
                minCol = seam[row + 1] + 1;
            seam[row] = minCol;
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, width(), height());

        Picture new_picture = new Picture(width(), height() - 1);
        for (int x = 0; x < width(); x++) {
            int new_y = 0;
            for (int y = 0; y < height(); y++) {
                if (y == seam[x])
                    continue;
                new_picture.set(x, new_y, picture.get(x, y));
                new_y++;
            }
        }
        picture = new_picture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, height(), width());
        Picture new_picture = new Picture(width() - 1, height());
        for (int y = 0; y < height(); y++) {
            int new_x = 0;
            for (int x = 0; x < width(); x++) {
                if (x == seam[y])
                    continue;
                new_picture.set(new_x, y, picture.get(x, y));
                new_x++;
            }
        }
        picture = new_picture;
    }

    private void validate(int index, int min, int max) {
        if (!(min <= index && index <= max))
            throw new IllegalArgumentException();
    }

    private void validateSeam(int[] seam, int len, int image_len) {
        if (seam == null || seam.length != len || image_len <= 1)
            throw new IllegalArgumentException();
        for (int index : seam)
            validate(index, 0, image_len - 1);
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException();
        }
    }

    private double square(double x) {
        return x * x;
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}
