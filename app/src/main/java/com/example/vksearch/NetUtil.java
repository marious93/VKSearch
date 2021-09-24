package com.example.vksearch;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetUtil {
    private static final String vk_api = "https://api.vk.com";
    private static final String vk_user_get = "/method/users.get";
    private static final String user_id = "user_ids";
    private static final String version = "v";
    private static final String token = "access_token";
    //&access_token=10259175b5475861292b276d0b2103f8a4fde6677edb8fee1002911529f0fcba84ad2cd1939662ba758a5&

    public static URL generateURL(String userIds) {
        Uri builtUri = Uri.parse(vk_api + vk_user_get)
                .buildUpon()
                .appendQueryParameter(user_id, userIds)
                .appendQueryParameter(version, "5.52")
                .appendQueryParameter(token, "10259175b5475861292b276d0b2103f8a4fde6677edb8fee1002911529f0fcba84ad2cd1939662ba758a5")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getRespondFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch(UnknownHostException e){
            return null;
        }
            finally
         {
            urlConnection.disconnect();

        }
    }
}
