package com.james.papertales.utils;

import android.support.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class ElementUtils {

    @Nullable
    public static Document getDocument(URL url) {
        try {
            String result = "";
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    //BYEEEEEEEE
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    //NOPE
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    //NO SECURITY FOR YUUUUUUUUUUUU
                    return null;
                }
            }}, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            in.close();
            connection.disconnect();

            return Jsoup.parse(result);
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDescription(Document document) {
        Elements elements = document.select("description");
        if (elements.size() > 0) return elements.get(0).text();
        else return "";
    }

    public static String getIcon(Document document) {
        Elements elements = document.select("image");
        if (elements.size() > 0) {
            Elements images = elements.select("url");
            String src = images.get(0).text();
            if (src.contains("?")) src = src.substring(0, src.indexOf("?"));
            return src;
        }
        else return "";
    }

    public static String getUrl(Document document) {
        Elements elements = document.select("link");
        if (elements.size() > 0) return elements.get(0).text();
        else return "https://www.google.com/";
    }

    public static String getName(Element item) {
        Elements elements = item.select("title");
        if (elements.size() > 0) return elements.get(0).text();
        else return "";
    }

    public static String getDescription(Element item) {
        Elements elements = item.select("content|encoded");
        if (elements.size() > 0) {
            Document document = Jsoup.parse(elements.get(0).text());
            document.select("img").remove();
            return document.text();
        } else {
            elements = item.select("description");
            if (elements.size() > 0) {
                return elements.get(0).text();
            } else return "";
        }
    }

    public static String getDate(Element item) {
        Elements elements = item.select("pubDate");
        if (elements.size() > 0) return elements.get(0).text();
        else return "";
    }

    public static String getLink(Element item) {
        Elements elements = item.select("link");
        if (elements.size() > 0) return elements.get(0).text();
        else {
            elements = item.select("guid");
            if (elements.size() > 0) return elements.get(0).text();
            else return "https://www.google.com/";
        }
    }

    public static ArrayList<String> getImages(Element item) {
        ArrayList<String> images = new ArrayList<>();

        Elements elements = item.select("content|encoded");
        if (elements.size() > 0) {
            Document content = Jsoup.parse(elements.get(0).text());
            for (Element image : content.select("img")) {
                if (!image.hasAttr("src")) continue;

                String src = image.attr("src");
                if (src.contains("?")) src = src.substring(0, src.indexOf("?"));
                images.add(src);
            }
        }

        return images;
    }

    public static ArrayList<String> getCategories(Element item) {
        ArrayList<String> categories = new ArrayList<>();

        Elements elements = item.select("category");
        for (Element element : elements) {
            categories.add(element.text());
        }

        return categories;
    }
}
