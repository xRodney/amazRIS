package name.xrodney.amazris.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.View;
import android.widget.ProgressBar;
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
    private WearableRecyclerView signList;
    private RisClient client;
//    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private TextView errorText;
    private Handler handler;
    private Runnable tick = this::tick;
    private int stopId;
    private long lastTick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departures);
        clock = findViewById(R.id.clock);
        signList = findViewById(R.id.sign_list);
        //progressDialog = new ProgressDialog(this);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);
        handler = new Handler(this.getMainLooper());


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        stopId = intent.getIntExtra(EXTRA_STOP, 0);

        client = new RisClient(AppDatabase.getInstance(this));

        refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(tick);
    }

    private void refresh() {
//        progressDialog.setMessage(getString(R.string.loading));
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        errorText.setVisibility(View.GONE);

        client.getDepartures(stopId, this, this);
    }

    @Override
    public void onResult(StopDepartures result) {
        runOnUiThread(() -> {
//            progressDialog.dismiss();
            startCountdown();

            if (result.getDepartures().isEmpty()) {
                errorText.setVisibility(View.VISIBLE);
                errorText.setText(R.string.nothing_to_show);
            } else {
                errorText.setVisibility(View.GONE);
            }

            // Save state
            Parcelable recyclerViewState = null;
            if (signList.getLayoutManager() != null)
                recyclerViewState = signList.getLayoutManager().onSaveInstanceState();

            SignDeparturesAdapter adapter = new SignDeparturesAdapter(result.getDepartures());
            signList.setHasFixedSize(true);
            signList.setAdapter(adapter);
            signList.setLayoutManager(new LinearLayoutManager(this));

            if (recyclerViewState != null) {
                signList.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }
        });

    }

    private void startCountdown() {
        progressBar.setIndeterminate(false);
        progressBar.setMax(30);
        progressBar.setProgress(progressBar.getMax());
        lastTick = System.currentTimeMillis() / 1000;
        handler.postDelayed(tick, 1000);
    }

    @Override
    public void onError(Exception exception) {
        runOnUiThread(() -> {
//            progressDialog.dismiss();
            startCountdown();
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(exception.getMessage());
            exception.printStackTrace();
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void tick() {
        long thisTick = System.currentTimeMillis() / 1000;
        long diff = thisTick - lastTick;

        int progress = progressBar.getProgress();

        progress -= diff;
        if (progress > 0) {
            progressBar.setProgress(progress);
            handler.postDelayed(tick, 1000);
        } else {
            refresh();
        }

        lastTick = thisTick;
    }
}
