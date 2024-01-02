package controller.connections;

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

import java.net.URLEncoder;
import java.util.Map;

public class HTTPConnectionHandler implements ConnectionHandler {

    @Override
    public String makeUrlRequest(String apiUrl, Request request, Response response, String method) {
        try {
            String finalUrl = buildUrlWithQueryParams(apiUrl, request.queryMap().toMap());
            System.out.println(finalUrl);

            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpResponse httpResponse = null;

            if (method.equals("POST")) {
                HttpPost httpPost = new HttpPost(finalUrl);
                setSession(request, httpPost);
                httpPost.setEntity(new StringEntity(request.body()));
                httpResponse = httpClient.execute(httpPost);

            } else if (method.equals("GET")) {
                HttpGet httpGet = new HttpGet(finalUrl);
                setSession(request, httpGet);
                httpResponse = httpClient.execute(httpGet);
            }

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

    private void setSession(Request request, HttpPost httpPost) {
        String session = getSession(request);

        if (session != null) {
            httpPost.setHeader("Cookie", String.format("Session=%s", session));
        }
    }

    private void setSession(Request request, HttpGet httpGet) {
        String session = getSession(request);

        if (session != null) {
            httpGet.setHeader("Cookie", String.format("Session=%s", session));
        }
    }

    private String buildUrlWithQueryParams(String apiUrl, Map<String, String[]> queryParams) throws Exception {
        if (queryParams != null && !queryParams.isEmpty()) {
            StringBuilder urlBuilder = new StringBuilder(apiUrl);
            if (!apiUrl.contains("?")) {
                urlBuilder.append("?");
            } else {
                urlBuilder.append("&");
            }

            for (Map.Entry<String, String[]> entry : queryParams.entrySet()) {
                String encodedKey = URLEncoder.encode(entry.getKey(), "UTF-8");
                String encodedValue = URLEncoder.encode(entry.getValue()[0], "UTF-8");
                urlBuilder.append(encodedKey).append("=").append(encodedValue).append("&");
            }

            return urlBuilder.toString().replaceAll("&$", "");
        } else {
            return apiUrl;
        }
    }

    private String getSession(Request request) {
        String session = null;
        try {
            session = request.cookie("Session");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }
}
