import java.util.Base64;

public class Main {

    public static void main(String[] args) {
        String i1 = "Hello, World!";
        String i2 = "The quick brown fox jumps over the lazy dog.";
        String i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-";

        long before = System.nanoTime();

        Base64.getEncoder().encodeToString(i1.getBytes());
        Base64.getEncoder().encodeToString(i2.getBytes());
        Base64.getEncoder().encodeToString(i3.getBytes());

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}