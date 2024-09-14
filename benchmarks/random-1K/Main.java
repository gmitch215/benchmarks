public class Main {

    public static void main(String[] args) {
        long before = System.currentTimeMillis();

        double n = 0;
        for (int i = 0; i < 1000; i++) {
            n = Math.random();
        }

        long after = System.currentTimeMillis();

        System.out.println(after - before);
    }

}