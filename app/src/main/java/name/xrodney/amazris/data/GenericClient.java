package name.xrodney.amazris.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.kieronquinn.library.amazfitcommunication.internet.LocalHTTPRequest;
import com.kieronquinn.library.amazfitcommunication.internet.LocalHTTPResponse;
import com.kieronquinn.library.amazfitcommunication.internet.LocalURLConnection;

import org.apache.commons.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GenericClient {
    static final String CACHE = RisClient.class.getCanonicalName() + ".CACHE";

    public GenericClient() {
    }

    public <T> void getData(String urlSpec, Context context, RisCallback<T> callback, ConnectionHandler<T> converter) {
        URL url;
        try {
            url = new URL(urlSpec);
        } catch (MalformedURLException e) {
            callback.onError(e);
            return;
        }

        LocalURLConnection connection = new LocalURLConnection();
        connection.setUrl(url);
        //connection.setReadTimeout(READ_TIMEOUT);
        //connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setRequestMethod("GET");

        // setDoOutput to true as we recieve data from json file
        connection.setDoOutput(true);

        new LocalHTTPRequest(context, connection, new LocalHTTPResponse() {
            @Override
            public void onResult(HttpURLConnection httpURLConnection) {
                try {
                    T result = converter.handle(httpURLConnection.getInputStream());
                    callback.onResult(result);
                } catch (IOException e) {
                    callback.onError(e);
                }
            }

            @Override
            public void onConnectError() {
                callback.onError(new Exception("Connection error"));
            }

            @Override
            public void onTimeout() {
                callback.onError(new Exception("Timeout"));
            }
        });
    }

    public SharedPreferences getCache(Context context) {
        return context.getSharedPreferences(CACHE, Context.MODE_PRIVATE);
    }

    public void storeToCache(Context context, String key, String stopsString) {
        SharedPreferences cache = getCache(context);
        SharedPreferences.Editor editor = cache.edit();
        editor.putString(key, stopsString);
        editor.apply();
    }

    public String loadFromCache(Context context, String key) {
        SharedPreferences cache = getCache(context);
        return cache.getString(key, null);
    }

    public interface ConnectionHandler<T> {
        default T handle(InputStream inputStream) throws IOException {
            String string = IOUtil.toString(inputStream);
            return handle(string);
        }

        T handle(String string) throws IOException ;
    }

    public interface RisCallback<T> {
        void onResult(T result);

        void onError(Exception error);
    }
}