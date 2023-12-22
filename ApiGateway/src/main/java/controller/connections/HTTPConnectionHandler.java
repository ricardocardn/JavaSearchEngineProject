package controller.connections;

import controller.ConnectionHandler;
import spark.Request;
import spark.Response;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

public class HTTPConnectionHandler implements ConnectionHandler {
    public HTTPConnectionHandler() {}

    @Override
    public String makeUrlRequest(String apiUrl, Request request, Response response) {
        try {
            String finalUrl = buildUrlWithQueryParams(apiUrl, request.queryMap().toMap());
            System.out.println(finalUrl);

            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            setSession(request, connection);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                setCookiesToResponse(response, connection);

                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder res = new StringBuilder();
                while (scanner.hasNextLine()) {
                    res.append(scanner.nextLine());
                }
                scanner.close();
                return res.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }

        return "";
    }

    private void setSession(Request request, HttpURLConnection connection) {
        String session = getSession(request);

        if (session != null)
            connection.setRequestProperty("Cookie", String.format("Session=%s", session));
    }

    private void setCookiesToResponse(Response response, HttpURLConnection connection) {
        String cookieHeader = connection.getHeaderField("Set-Cookie");

        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";\\s*");
            for (String cookie : cookies) {
                String[] cookieValues = cookie.split("=");
                response.cookie(cookieValues[0], cookieValues[1]);
            }
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
        } catch (Exception e) {}
        return session;
    }
}
