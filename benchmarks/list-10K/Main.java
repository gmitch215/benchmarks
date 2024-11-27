import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        long before = System.nanoTime();

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            list.add(i);
        }

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}