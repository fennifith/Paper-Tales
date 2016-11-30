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
import java.util.ArrayList;

public class ElementUtils {

    @Nullable
    public static Document getDocument(URL url) {
        try {
            String result = "";
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            in.close();
            connection.disconnect();

            return Jsoup.parse(result);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static String getTitle(Document document) {
        if (document.title() != null) return document.title();
        else {
            Elements elements = document.select("image");
            if (elements.size() > 0) {
                Elements titles = elements.get(0).select("title");
                if (titles.size() > 0) return titles.get(0).text();
                else return null;
            } else return null;
        }
    }

    public static String getDescription(Document document) {
        Elements elements = document.select("description");
        if (elements.size() > 0) return elements.get(0).text();
        else return null;
    }

    public static String getName(Element item) {
        Elements elements = item.select("title");
        if (elements.size() > 0) return elements.get(0).text();
        else return null;
    }

    public static String getDescription(Element item) {
        Elements elements = item.select("content|encoded");
        if (elements.size() > 0) {
            Document document = Jsoup.parse(elements.get(0).text());
            document.select("img").remove();
            return document.html();
        } else {
            elements = item.select("description");
            if (elements.size() > 0) {
                return elements.get(0).html();
            } else return null;
        }
    }

    public static String getDate(Element item) {
        Elements elements = item.select("pubDate");
        if (elements.size() > 0) return elements.get(0).text();
        else return null;
    }

    public static String getLink(Element item) {
        Elements elements = item.select("guid");
        if (elements.size() > 0) return elements.get(0).text();
        else {
            elements = item.select("comments");
            if (elements.size() > 0) {
                String link = elements.get(0).text();
                if (link.contains("#")) link = link.substring(0, link.indexOf("#"));
                return link;
            } else return null;
        }
    }

    public static String getComments(Element item) {
        Elements elements = item.select("comments");
        if (elements.size() > 0) return elements.get(0).text();
        else return getLink(item);
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
