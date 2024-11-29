import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception {
        long before = System.currentTimeMillis();

        URL url = URI.create("http://httpbin.org/post").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        byte[] body = "Hello World".getBytes();
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setRequestProperty("Content-Length", Integer.toString(body.length));
        connection.getOutputStream().write(body);

        connection.getResponseCode();

        long after = System.currentTimeMillis();

        System.out.println(after - before);
    }

}