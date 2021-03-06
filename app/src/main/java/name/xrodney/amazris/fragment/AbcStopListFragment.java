package name.xrodney.amazris.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import name.xrodney.amazris.R;
import name.xrodney.amazris.activity.DeparturesActivity;
import name.xrodney.amazris.adapter.StopsListAdapter;
import name.xrodney.amazris.data.GenericClient;
import name.xrodney.amazris.data.RisClient;
import name.xrodney.amazris.database.AppDatabase;
import name.xrodney.amazris.layout.AmazfitLayoutCallback;
import name.xrodney.amazris.model.Stop;

public class AbcStopListFragment extends Fragment implements GenericClient.RisCallback<List<Stop>>, StopsListAdapter.StopClickListener {
    public static final String ARG_OBJECT = "object";

    private WearableRecyclerView stopsList;
    protected RisClient client;
    private ProgressBar progressBar;
    private TextView errorText;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stop_list, container, false);

        stopsList = rootView.findViewById(R.id.stops_list);
        progressBar = rootView.findViewById(R.id.progressBar);
        errorText = rootView.findViewById(R.id.errorText);

        client = new RisClient(AppDatabase.getInstance(requireContext()));

        Bundle args = getArguments();

        refresh();
        return rootView;

    }

    public void refresh() {
        if (getActivity() == null) {
            return;
        }

        requireActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);

            errorText.setVisibility(View.GONE);
        });

        load();
    }

    protected void load() {
        client.getStops(this.requireContext(), this);
    }


    public void onResult(List<Stop> stops) {
        if (getActivity() == null) {
            return;
        }
        requireActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);

            StopsListAdapter adapter = new StopsListAdapter(stops, this);
            stopsList.setHasFixedSize(true);
            stopsList.setAdapter(adapter);

            stopsList.setEdgeItemsCenteringEnabled(true);
            stopsList.setLayoutManager(new WearableLinearLayoutManager(requireContext(), new AmazfitLayoutCallback(requireContext())));
        });
    }

    public void onError(Exception exception) {
        if (getActivity() != null) {
            requireActivity().runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                exception.printStackTrace();
                errorText.setVisibility(View.VISIBLE);
                errorText.setText(exception.getMessage());
            });
        }
        Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void selectStop(Stop stop) {
        Intent intent = new Intent(this.requireContext(), DeparturesActivity.class);
        intent.putExtra(DeparturesActivity.EXTRA_STOP, stop.getId());
        startActivity(intent);
    }

    @Override
    public void onStopClicked(Stop stop) {
        selectStop(stop);
    }
}
