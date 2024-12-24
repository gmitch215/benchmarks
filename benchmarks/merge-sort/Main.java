public class Main {

    public static void merge(int arr[], int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int L[] = new int[n1];
        int R[] = new int[n2];

        for (int i = 0; i < n1; i++)
            L[i] = arr[l + i];

        for (int j = 0; j < n2; j++)
            R[j] = arr[m + 1 + j];

        int i = 0, j = 0;
        int k = l;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }

            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    public static void mergeSort(int arr[], int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    public static void main(String[] args) {
        long before = System.nanoTime();

        int[] arr = new int[] {
                639, 575, 652, 103, 262, 571, 542, 11, 278, 144, 885, 300, 897, 303, 726, 272, 214, 346, 935, 409, 76, 877, 367, 837, 728, 761, 205, 143, 658, 908, 263, 388, 317, 159, 331, 107, 127, 635, 972, 773, 81, 964, 302, 294, 82, 7, 671, 507, 802, 966
        };
        mergeSort(arr, 0, arr.length - 1);

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}