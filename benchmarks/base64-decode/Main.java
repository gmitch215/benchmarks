import java.util.Base64;

public class Main {

    public static void main(String[] args) {
        String i1 = "SGVsbG8sIFdvcmxkIQ==";
        String i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";
        String i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ==";

        long before = System.nanoTime();

        Base64.getDecoder().decode(i1.getBytes());
        Base64.getDecoder().decode(i2.getBytes());
        Base64.getDecoder().decode(i3.getBytes());

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}