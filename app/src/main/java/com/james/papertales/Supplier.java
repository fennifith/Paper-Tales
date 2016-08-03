package com.james.papertales;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.james.papertales.data.AuthorData;
import com.james.papertales.data.HeaderListData;
import com.james.papertales.data.WallData;
import com.james.papertales.utils.ElementUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Supplier extends Application {

    private String[] urls;

    private ArrayList<AuthorData> authors;
    private ArrayList<WallData>[] wallpapers;

    @Override
    public void onCreate() {
        super.onCreate();

        urls = getResources().getStringArray(R.array.people_wps);
    }

    public boolean getNetworkResources() {
        //download any resources needed for the voids below while the splash screen is showing
        //yes, this is thread-safe
        //no, it is not needed for the current setup since all the resources are in res/values/strings.xml

        authors = new ArrayList<>();
        wallpapers = new ArrayList[urls.length];

        for (int i = 0; i < urls.length; i++) {

            wallpapers[i] = new ArrayList<>();

            Document document;
            try {
                document = ElementUtils.getDocument(new URL(urls[i]));
                if (document == null) continue;

                AuthorData author = new AuthorData(document.title(), ElementUtils.getIcon(document), "", i, ElementUtils.getUrl(document), urls[i]);
                authors.add(author);

                Elements elements = document.select("item");
                for (Element element : elements) {
                    WallData data = new WallData(ElementUtils.getName(element), ElementUtils.getDescription(element), ElementUtils.getImages(element), author.name, author.id);
                    wallpapers[0].add(data);
                }
                // etc
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    //get a list of the different sections
    public ArrayList<AuthorData> getAuthors() {
        return authors;
    }

    @Nullable
    public AuthorData getAuthor(int id) {
        if (id < 0 || id >= authors.size()) return null;
        else return authors.get(id);
    }

    //get a list of the different wallpapers
    public ArrayList<WallData> getWallpapers() {
        ArrayList<WallData> walls = new ArrayList<>();

        for (ArrayList<WallData> wallpapers : this.wallpapers) {
            walls.addAll(wallpapers);
        }

        return walls;
    }

    public ArrayList<WallData> getWallpapers(int authorId) {
        return wallpapers[authorId];
    }

    //additional info to put in the about section
    public ArrayList<HeaderListData> getAdditionalInfo() {
        ArrayList<HeaderListData> headers = new ArrayList<>();

        return headers;
    }

    public AlertDialog getCreditDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        //dialog to be shown when credit is required
        return new AlertDialog.Builder(context)
                .setTitle(R.string.credit_required)
                .setMessage(R.string.credit_required_msg)
                .setPositiveButton("OK", onClickListener)
                .create();
    }

    public void downloadWallpaper(Context context, String name, String url) {
        //start a download
        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(url));
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + ".png");
        r.allowScanningByMediaScanner();
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(r);
    }

    public AlertDialog getDownloadedDialog(Context context, DialogInterface.OnClickListener onClickListener) {
        //dialog to be shown upon completion of a download
        return new AlertDialog.Builder(context).setTitle(R.string.download_complete).setMessage(R.string.download_complete_msg).setPositiveButton("View", onClickListener).create();
    }

    //share a wallpaper
    public void shareWallpaper(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, String.valueOf(Uri.parse(url)));
        context.startActivity(intent);
    }
}
