public class Main {

    public static int binarySearch(int arr[], int x) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == x)
                return mid;

            if (arr[mid] < x)
                low = mid + 1;
            else
                high = mid - 1;
        }

        return -1;
    }

    public static void main(String[] args) {
        int[] arr = new int[] {
                2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834
        };

        long before = System.nanoTime();

        binarySearch(arr, arr[2]);
        binarySearch(arr, arr[8]);
        binarySearch(arr, arr[12]);
        binarySearch(arr, arr[15]);
        binarySearch(arr, arr[20]);

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}