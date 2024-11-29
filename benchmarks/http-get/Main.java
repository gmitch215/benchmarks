import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception {
        long before = System.currentTimeMillis();

        URL url = URI.create("http://httpbin.org/get").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.getResponseCode();

        long after = System.currentTimeMillis();

        System.out.println(after - before);
    }

}