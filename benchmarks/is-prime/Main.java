public class Main {

    public static boolean isPrime(int n) {
        int start = 2;
        double limit = Math.sqrt(n);

        while (start <= limit) {
            if (n % start++ == 0) return false;
        }

        return n > 1;
    }

    public static void main(String[] args) {
        long before = System.nanoTime();

        for (int i = 0; i < 100000; i++) {
            isPrime(i);
        }

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}