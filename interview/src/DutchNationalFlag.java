import java.util.Random;

public class DutchNationalFlag {

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            test();
        }
    }

    /**
     * 
     */
    private static void test() {
        Random ran = new Random();

        int n = ran.nextInt(100) + 100;
        Array a = new Array(n);
        int first1 = -1, beforeFirst2 = n - 1;
        int i = 0;
        while (true) {
            if (i > beforeFirst2)
                break;
            int currentColor = a.color(i);
            switch (currentColor) {
            case 0:
                // move to next
                if (first1 < 0) {
                    i++;
                    break;
                } else {
                    a.swap(first1, i);
                    first1++;
                    i++;
                    break;
                }
            case 1:
                if (first1 < 0) {
                    first1 = i;
                    i++;
                    break;
                } else {
                    i++;
                    break;
                }
            case 2:
                if (beforeFirst2 == n - 1) {
                    beforeFirst2 = n - 2;
                    a.swap(i, n - 1);
                    break;
                } else {
                    a.swap(i, beforeFirst2);
                    beforeFirst2--;
                    break;
                }
            default:
                assert (false);
            }
        }

        assert a.isSorted();
        assert a.getColorCount() <= n;
        assert a.getSwapCount() <= n;
    }

}

class Array {
    private final int[] a;
    private final int n;
    private int colorCount;
    private int swapCount;

    public Array(int n) {
        colorCount = swapCount = 0;
        this.n = n;
        a = new int[n];
        Random ran = new Random();
        for (int i = 0; i < n; i++)
            a[i] = ran.nextInt(3);
    }

    public int getColorCount() {
        return colorCount;
    }

    public int getSwapCount() {
        return swapCount;
    }

    public boolean isSorted() {
        for (int i = 0; i < n - 1; i++)
            if (a[i] > a[i + 1])
                return false;
        return true;
    }

    public int color(int i) {
        colorCount++;
        return a[i];
    }

    public void swap(int i, int j) {
        swapCount++;
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
}