/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.social.validate.lib.profile.validator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 *
 * @author huuloc.tran89
 */
public abstract class AbstractCallHttp {

    private static Logger logger = Logger.getLogger(AbstractCallHttp.class);

    public static String doHttp(String urlStr, String method, String param) {
        try {
            URL url = new URL(urlStr);
            String proxyHost = System.getProperty("http.proxyHost");
            String proxyPort = System.getProperty("http.proxyPort");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (proxyHost != null && proxyPort != null) {
                try {
                    int port = Integer.parseInt(proxyPort);
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
                    conn = (HttpURLConnection) url.openConnection(proxy);
                } catch (Exception e) {
                    logger.error("Proxy " + proxyHost + " didn't works!. " + e.getMessage(), e);
                }
            }

            conn.setRequestMethod(method);
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000); // Milliseconds
            conn.setDoOutput(true); // Triggers POST.

            if (method.equals("POST")) {
                try (OutputStream output = conn.getOutputStream()) {
                    output.write(param.getBytes());
                }
            }

            int code = conn.getResponseCode();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getResponseCode() / 100 == 2
                                    ? conn.getInputStream() : conn.getErrorStream()));
            String line = "";
            StringBuilder lines = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                lines.append(line);
            }
            rd.close();
            logger.info("Validator url: " + url + " - Param: " + param + " - Code: " + code + " - Result: " + lines);
            return lines.toString();

        } catch (Exception e) {
            logger.error("Error at sendPostRequest. " + urlStr + " " + e.getMessage(), e);
        }
        return null;
    }

}
