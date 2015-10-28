package com.nvrsty.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nvrsty.fragments.HorariosFragment;
import com.nvrsty.fragments.InformacoesGeraisFragment;

/**
 * Created by Matheus on 28/09/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter {
    private Context context;
    private String[] titles = {"INFORMAÇÕES GERAIS", "HORÁRIOS"};
    private Fragment informacoesGeraisFragment, horariosFragment;

    public TabsAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.context = ctx;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            if (informacoesGeraisFragment == null) {
                informacoesGeraisFragment = new InformacoesGeraisFragment();
            }
            return informacoesGeraisFragment;
        } else {
            if (horariosFragment == null) {
                horariosFragment = new HorariosFragment();
            }
            return horariosFragment;
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


}
