public class Main {

    public static void main(String[] args) {
        long before = System.nanoTime();

        int n = 0;
        for (int i = 0; i < 31; i++) {
            n = 1 << i;
        }

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}