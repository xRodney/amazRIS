package name.xrodney.amazris.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import name.xrodney.amazris.R;
import name.xrodney.amazris.model.Stop;

import java.util.List;

public class StopsListAdapter extends RecyclerView.Adapter<StopsListAdapter.StopViewHolder> {
    private List<Stop> model;
    private StopClickListener listener;

    public StopsListAdapter(List<Stop> model, StopClickListener listener) {

        this.model = model;
        this.listener = listener;
    }

    static class StopViewHolder extends RecyclerView.ViewHolder {
        TextView stopName;

        public StopViewHolder(View itemView) {
            super(itemView);
            stopName = itemView.findViewById(R.id.stop_name);
        }

        public void bindData(Stop stop, StopClickListener listener) {
            stopName.setText(stop.getName());
            itemView.setOnClickListener((v) -> listener.onStopClicked(stop));
        }
    }

    public interface StopClickListener {
        void onStopClicked(Stop stop);
    }

    @NonNull
    @Override
    public StopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new StopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StopViewHolder holder, int position) {
        holder.bindData(model.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.stop_item;
    }
}
