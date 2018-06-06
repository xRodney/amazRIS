package name.xrodney.amazris.data;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import name.xrodney.amazris.model.StopDepartures;
import name.xrodney.amazris.model.Stop;

import org.apache.commons.io.IOUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RisClient {
    public static final String STOPS = "Stops";
    private final GenericClient genericClient = new GenericClient();

    private ObjectMapper mapper = new ObjectMapper();

    public void getDepartures(int stopId, Context context, final GenericClient.RisCallback<StopDepartures> callback) {
        genericClient.getData("http://iris.bmhd.cz/api/getDeparturesNew.php?stopId=" + stopId,
                context, callback, string -> {
                    JsonParser parser = mapper.getFactory().createParser(string);
                    return mapper.reader().readValue(parser, StopDepartures.class);
                });
    }

    public void getStops(Context context, final GenericClient.RisCallback<List<Stop>> callback) {

        GenericClient.ConnectionHandler<List<Stop>> handler = string -> {
            genericClient.storeToCache(context, STOPS, string);
            return parseStops(string);
        };

        if (tryLoadStopsFromCache(context, callback, handler)) {
            Log.i(getClass().getSimpleName(), "Loaded from cache");
        } else {
            Log.i(getClass().getSimpleName(), "Loading from net");
            genericClient.getData("http://iris.bmhd.cz/api/stops.json", context, callback, handler);
        }
    }

    private boolean tryLoadStopsFromCache(Context context, GenericClient.RisCallback<List<Stop>> callback, GenericClient.ConnectionHandler<List<Stop>> handler) {
        String cached = genericClient.loadFromCache(context, STOPS);
        if (cached == null) {
            return false;
        }
        new StopsCacheTask(callback, cached, handler).execute();
        return true;
    }

    @NonNull
    private List<Stop> parseStops(String string) throws IOException {
        TypeReference<Map<Integer, Stop>> typeReference = new TypeReference<Map<Integer, Stop>>() {
        };
        JsonParser parser = mapper.getFactory().createParser(string);
        Map<Integer, Stop> stopsMap = mapper.reader().readValue(parser, typeReference);
        List<Stop> stopsList = new ArrayList<>(stopsMap.size());
        for (Map.Entry<Integer, Stop> entry : stopsMap.entrySet()) {
            entry.getValue().setId(entry.getKey());
            stopsList.add(entry.getValue());
        }
        return stopsList;
    }

    private static class StopsCacheTask extends AsyncTask<Void, Void, Void> {
        private final GenericClient.RisCallback<List<Stop>> callback;
        private final String cached;
        private final GenericClient.ConnectionHandler<List<Stop>> handler;

        public StopsCacheTask(GenericClient.RisCallback<List<Stop>> callback, String cached, GenericClient.ConnectionHandler<List<Stop>> handler) {
            this.callback = callback;
            this.cached = cached;
            this.handler = handler;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                callback.onResult(handler.handle(new ByteArrayInputStream(cached.getBytes())));
            } catch (IOException e) {
                callback.onError(e);
            }
            return null;
        }
    }
}
