package name.xrodney.amazris.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import name.xrodney.amazris.R;
import name.xrodney.amazris.model.SignDepartures;

import java.util.List;

public class SignDeparturesAdapter extends RecyclerView.Adapter<SignDeparturesAdapter.SignViewHolder> {
    private List<SignDepartures> model;

    public SignDeparturesAdapter(List<SignDepartures> model) {

        this.model = model;
    }

    static class SignViewHolder extends RecyclerView.ViewHolder {
        TextView signName;
        private final RecyclerView departuresList;

        public SignViewHolder(View itemView) {
            super(itemView);
            signName = itemView.findViewById(R.id.sign_name);
            departuresList = itemView.findViewById(R.id.departures_list);
        }

        public void bindData(SignDepartures sign) {
            signName.setText(sign.getName());

            DeparturesListAdapter adapter = new DeparturesListAdapter(sign.getDepartures());
            departuresList.setHasFixedSize(true);
            departuresList.setAdapter(adapter);
            departuresList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }

    @NonNull
    @Override
    public SignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new SignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignViewHolder holder, int position) {
        holder.bindData(model.get(position));
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.sign_item;
    }
}
