public class Main {

    public static void main(String[] args) {
        long before = System.nanoTime();

        for (int i = 0; i < 1_000; i++) {
            Math.sqrt(i);
        }

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}