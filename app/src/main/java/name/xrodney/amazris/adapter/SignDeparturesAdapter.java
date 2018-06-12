package name.xrodney.amazris.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import name.xrodney.amazris.R;
import name.xrodney.amazris.model.Departure;
import name.xrodney.amazris.model.SignDepartures;

public class SignDeparturesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> model;

    public SignDeparturesAdapter(List<SignDepartures> model) {

        this.model = new ArrayList<>(model.size() * 6);
        for (SignDepartures signDepartures : model) {
            this.model.add(signDepartures);
            this.model.addAll(signDepartures.getDepartures());
        }
    }

    static class SignViewHolder extends RecyclerView.ViewHolder {
        TextView signName;

        public SignViewHolder(View itemView) {
            super(itemView);
            signName = itemView.findViewById(R.id.sign_name);
        }

        public void bindData(SignDepartures sign) {
            signName.setText(sign.getName());
        }
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        switch (viewType) {
            case R.layout.sign_name:
                return new SignViewHolder(view);
            case R.layout.departure_item:
                return new DepartureViewHolder(view);
        }
        throw new IllegalStateException();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SignViewHolder) {
            ((SignViewHolder)holder).bindData((SignDepartures) model.get(position));
        } else if (holder instanceof DepartureViewHolder) {
            ((DepartureViewHolder)holder).bindData((Departure) model.get(position));
        } else {
            throw new IllegalStateException(holder.getClass().getCanonicalName());
        }
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = model.get(position);
        if (item instanceof SignDepartures) {
            return R.layout.sign_name;
        } else if (item instanceof Departure) {
            return R.layout.departure_item;
        } else {
            throw new IllegalStateException(item.getClass().getCanonicalName());
        }
    }
}
