public class Main {

    public static void main(String[] args) {
        long before = System.nanoTime();

        double n = 0;
        for (int i = 0; i < 1000000; i++) {
            n = Math.random();
        }

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}