package name.xrodney.amazris.fragment;

public class NearbyStopListFragment extends AbcStopListFragment {
    @Override
    protected void load() {
        client.getStops(requireContext(), 0, 0, this);
    }
}
