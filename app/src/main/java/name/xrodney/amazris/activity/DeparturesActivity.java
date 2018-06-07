package name.xrodney.amazris.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import name.xrodney.amazris.R;
import name.xrodney.amazris.adapter.SignDeparturesAdapter;
import name.xrodney.amazris.data.GenericClient;
import name.xrodney.amazris.data.RisClient;
import name.xrodney.amazris.database.AppDatabase;
import name.xrodney.amazris.model.StopDepartures;

public class DeparturesActivity extends Activity implements GenericClient.RisCallback<StopDepartures> {
    public static final String EXTRA_STOP = "name.dusanjakub.amazris.STOP";

    private TextView clock;
    private RecyclerView signList;
    private RisClient client;
    private ProgressDialog progressDialog;
    private int stopId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departures);
        clock = findViewById(R.id.clock);
        signList = findViewById(R.id.sign_list);
        progressDialog = new ProgressDialog(this);


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        stopId = intent.getIntExtra(EXTRA_STOP, 0);

        client = new RisClient(AppDatabase.getInstance(this));

        refresh();
    }

    private void refresh() {
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        client.getDepartures(stopId, this, this);
    }

    @Override
    public void onResult(StopDepartures result) {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            SignDeparturesAdapter adapter = new SignDeparturesAdapter(result.getDepartures());
            signList.setHasFixedSize(true);
            signList.setAdapter(adapter);
            signList.setLayoutManager(new LinearLayoutManager(this));
        });
    }

    @Override
    public void onError(Exception exception) {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            exception.printStackTrace();
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
}
