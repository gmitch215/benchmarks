public class Main {

    public static void main(String[] args) {
        long before = System.nanoTime();

        int[] arr = new int[100_000];
        for (int i = 0; i < 100_000; i++) {
            arr[i] = i;
        }

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}