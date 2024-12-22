public class Main {

    public static void main(String[] args) {
        long before = System.nanoTime();

        for (int i = 0; i < 100_000; i++) {
            Math.cbrt(i);
        }

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}