package name.xrodney.amazris.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import name.xrodney.amazris.R;
import name.xrodney.amazris.model.Departure;

import java.util.List;

public class DeparturesListAdapter extends RecyclerView.Adapter<DeparturesListAdapter.DepartureViewHolder> {
    private List<Departure> model;

    public DeparturesListAdapter(List<Departure> model) {
        this.model = model;
    }

    static class DepartureViewHolder extends RecyclerView.ViewHolder {
        TextView line;
        TextView endStop;
        View lowFloor;
        TextView time;

        public DepartureViewHolder(View itemView) {
            super(itemView);
            line = itemView.findViewById(R.id.line);
            endStop = itemView.findViewById(R.id.end_stop);
            lowFloor = itemView.findViewById(R.id.low_floor);
            time = itemView.findViewById(R.id.time);
        }

        public void bindData(Departure departure) {
            line.setText(departure.getLine());
            endStop.setText(departure.getEndStop());
            lowFloor.setVisibility(departure.isLowFloor() ? View.VISIBLE : View.GONE);
            time.setText(departure.getTime());
        }
    }

    @NonNull
    @Override
    public DepartureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new DepartureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartureViewHolder holder, int position) {
        holder.bindData(model.get(position));
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.departure_item;
    }
}
