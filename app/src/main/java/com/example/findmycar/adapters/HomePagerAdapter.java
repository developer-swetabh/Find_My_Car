package com.example.findmycar.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.findmycar.contract.MainContract;
import com.example.findmycar.ui.fragment.BaseFragment;
import com.example.findmycar.ui.fragment.MapView;
import com.example.findmycar.ui.fragment.ParkingHistoryFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 2;
    private BaseFragment mBaseFragment;
    private MainContract.IActivityCommunicator mCommunicator;

    public HomePagerAdapter(FragmentManager fm, BaseFragment baseFragment, MainContract.IActivityCommunicator communicator) {
        super(fm);
        mBaseFragment = baseFragment;
        mCommunicator = communicator;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mBaseFragment = new MapView();
                mBaseFragment.setCommunicator(mCommunicator);
                mCommunicator.updateBaseFragment(mBaseFragment);
                return mBaseFragment;
            case 1:
                mBaseFragment = new ParkingHistoryFragment();
                mBaseFragment.setCommunicator(mCommunicator);
                mCommunicator.updateBaseFragment(mBaseFragment);
                return mBaseFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Map View";
            case 1:
                return "History";
            default:
                return null;
        }
    }
}
