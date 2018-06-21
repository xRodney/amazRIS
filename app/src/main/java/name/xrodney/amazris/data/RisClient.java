package name.xrodney.amazris.data;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import name.xrodney.amazris.database.AppDatabase;
import name.xrodney.amazris.database.StopDao;
import name.xrodney.amazris.model.Stop;
import name.xrodney.amazris.model.StopDepartures;

public class RisClient {
    public static final String STOPS = "Stops";
    private final GenericClient genericClient = new GenericClient();

    private ObjectMapper mapper = new ObjectMapper();
    private AppDatabase database;

    public RisClient(AppDatabase database) {
        this.database = database;
    }

    public void getDepartures(int stopId, Context context, final GenericClient.RisCallback<StopDepartures> callback) {
        genericClient.getData("http://iris.bmhd.cz/api/getDeparturesNew.php?stopId=" + stopId,
                context, callback, string -> {
                    JsonParser parser = mapper.getFactory().createParser(string);
                    return mapper.reader().readValue(parser, StopDepartures.class);
                });
    }

    public void getStops(Context context, final GenericClient.RisCallback<List<Stop>> callback) {
        new StopsCacheTask(database.stopDao(), context, genericClient, callback).execute();
    }

    public void getStops(Context context, double lat, double lng, final GenericClient.RisCallback<List<Stop>> callback) {
        new StopsCacheTask(database.stopDao(), context, genericClient, callback).execute(lat, lng);
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

    private class StopsCacheTask extends AsyncTask<Double, Void, Boolean> {
        private GenericClient.RisCallback<List<Stop>> callback;
        private StopDao stopDao;
        private Context context;
        private GenericClient genericClient;

        public StopsCacheTask(StopDao stopDao, Context context, GenericClient genericClient, GenericClient.RisCallback<List<Stop>> callback) {
            this.context = context;
            this.genericClient = genericClient;
            this.callback = callback;
            this.stopDao = stopDao;
        }

        @Override
        protected Boolean doInBackground(Double... coords) {
            try {
                if (stopDao.count() == 0) {
                    Log.i(getClass().getSimpleName(), "Loading from net");
                    return false;
                }

                Log.i(getClass().getSimpleName(), "Loading from cache");
                if (coords.length == 0) {
                    callback.onResult(stopDao.getAll());
                } else if (coords.length == 2) {
                    callback.onResult(stopDao.getNearby(coords[0], coords[1]));
                } else {
                    throw new IllegalArgumentException("Exactly 0 or 2 arguments must be given");
                }
            } catch (Exception e) {
                callback.onError(e);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean foundInCache) {
            if (!foundInCache) {
                genericClient.getData("http://iris.bmhd.cz/api/stops.json", context, callback, string -> {
                    List<Stop> stops = parseStops(string);
                    stopDao.insertAll(stops);
                    return stops;
                });
            }
        }
    }
}
