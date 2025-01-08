public class Main {

    public static int jumpSearch(int[] arr, int x) {
        int n = arr.length;
        int step = (int) Math.floor(Math.sqrt(n));
 
        int prev = 0;
        for (int minStep = Math.min(step, n)-1; arr[minStep] < x; minStep = Math.min(step, n) - 1) {
            prev = step;
            step += (int) Math.floor(Math.sqrt(n));
            if (prev >= n)
                return -1;
        }
 
        while (arr[prev] < x) {
            prev++;
            if (prev == Math.min(step, n))
                return -1;
        }
 
        if (arr[prev] == x)
            return prev;
 
        return -1;
    }

    public static void main(String[] args) {
        int[] arr = new int[] {
                2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834
        };

        long before = System.nanoTime();

        jumpSearch(arr, arr[2]);
        jumpSearch(arr, arr[8]);
        jumpSearch(arr, arr[12]);
        jumpSearch(arr, arr[15]);
        jumpSearch(arr, arr[20]);

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}