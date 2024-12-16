public class Main {

    public static void main(String[] args) {
        long before = System.nanoTime();

        for (int i = 0; i < 1_000; i++) {
            double y = 1.1 + (i / 1000.0);
            Math.pow(2, y);
        }

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}