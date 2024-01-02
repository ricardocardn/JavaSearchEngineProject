package controller.http.connections;

import model.Book;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import spark.Request;
import spark.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HTTPConnectionHandler implements ConnectionHandler {
    private final String SERVER_API_URL;

    public HTTPConnectionHandler() {
        this.SERVER_API_URL = System.getenv("SERVER_API_URL");
    }

    @Override
    public String makeUrlRequest(String path, Book book) {
        try {
            String finalUrl = buildUrlWithQueryParams(path, book);
            System.out.println(finalUrl);

            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpResponse httpResponse = null;

            HttpPost httpPost = new HttpPost(finalUrl);
            httpPost.setEntity(new StringEntity(book.content()));
            httpResponse = httpClient.execute(httpPost);

            int responseCode = httpResponse.getStatusLine().getStatusCode();

            if (responseCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                return EntityUtils.toString(entity);
            } else {
                throw new RuntimeException(String.valueOf(responseCode));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }

    private String buildUrlWithQueryParams(String path, Book book) throws Exception {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(SERVER_API_URL + "/");
        urlBuilder.append(path + "?");

        setQueryParam(urlBuilder, "name", book.name());
        setQueryParam(urlBuilder, "author", book.author());
        setQueryParam(urlBuilder, "date", book.date());
        setQueryParam(urlBuilder, "language", book.language());

        return urlBuilder.toString().replaceAll("&$", "");
    }

    private void setQueryParam(StringBuilder urlBuilder, String key, String value) throws UnsupportedEncodingException {
        String encodedKey = URLEncoder.encode(key, "UTF-8");
        String encodedValue = URLEncoder.encode(value, "UTF-8");
        urlBuilder.append(encodedKey).append("=").append(encodedValue).append("&");
    }
}
