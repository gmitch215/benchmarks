public class Main {

    public static void main(String[] args) {
        long before = System.nanoTime();

        int count = 0;
        for (int i = 0; i < 1_000_000; i++) {
            count++;
        }

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}