package name.xrodney.amazris.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextClock;

import name.xrodney.amazris.R;
import name.xrodney.amazris.fragment.AbcStopListFragment;
import name.xrodney.amazris.fragment.NearbyStopListFragment;

public class StopsActivity extends AppCompatActivity {

    // When requested, this adapter returns a AbcStopListFragment,
    // representing an object in the collection.
    StopListsPagerAdapter pagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        pagerAdapter = new StopListsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        TextClock clock = findViewById(R.id.clock);
        clock.setOnClickListener((x) -> {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment instanceof AbcStopListFragment) {
                    ((AbcStopListFragment)fragment).refresh();
                }
            }
        });

    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class StopListsPagerAdapter extends FragmentStatePagerAdapter {
        public StopListsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;
            if (i == 0) {
                fragment = new NearbyStopListFragment();
            } else {
                fragment = new AbcStopListFragment();
            }
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putInt(AbcStopListFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.nearby);
                default: return "OBJECT " + (position + 1);
            }
        }
    }

}
