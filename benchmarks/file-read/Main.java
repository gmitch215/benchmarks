import java.io.*;

public class Main {

    public static void main(String[] args) {
        long before = System.nanoTime();

        try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Do Nothing
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long after = System.nanoTime();

        System.out.println(after - before);
    }

}