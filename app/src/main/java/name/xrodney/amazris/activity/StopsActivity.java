package name.xrodney.amazris.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.widget.Toast;

import java.util.List;

import name.xrodney.amazris.R;
import name.xrodney.amazris.adapter.StopsListAdapter;
import name.xrodney.amazris.data.GenericClient;
import name.xrodney.amazris.data.RisClient;
import name.xrodney.amazris.database.AppDatabase;
import name.xrodney.amazris.layout.AmazfitLayoutCallback;
import name.xrodney.amazris.model.Stop;

public class StopsActivity extends Activity implements GenericClient.RisCallback<List<Stop>>,StopsListAdapter.StopClickListener {

    private WearableRecyclerView stopsList;
    private RisClient client;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);
        stopsList = findViewById(R.id.stops_list);
        progressDialog = new ProgressDialog(this);

        client = new RisClient(AppDatabase.getInstance(this));

        refresh();
    }

    private void refresh() {
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        client.getStops(this, this);
    }


    public void onResult(List<Stop> stops) {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            StopsListAdapter adapter = new StopsListAdapter(stops, this);
            stopsList.setHasFixedSize(true);
            stopsList.setAdapter(adapter);

            stopsList.setEdgeItemsCenteringEnabled(true);
            stopsList.setLayoutManager(new WearableLinearLayoutManager(this, new AmazfitLayoutCallback(this)));
        });
    }

    public void onError(Exception exception) {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            exception.printStackTrace();
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    public void selectStop(Stop stop) {
        Intent intent = new Intent(this, DeparturesActivity.class);
        intent.putExtra(DeparturesActivity.EXTRA_STOP, stop.getId());
        startActivity(intent);
    }

    @Override
    public void onStopClicked(Stop stop) {
        selectStop(stop);
    }
}
