public class Main {

    public static long next(long n) {
        if (n % 2 == 0) {
            return n / 2;
        } else {
            return 3 * n + 1;
        }
    }

    public static void main(String[] args) {
        long before = System.nanoTime();

        long n = 837799L;
        while (n != 1L) {
            n = next(n);
        }

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}