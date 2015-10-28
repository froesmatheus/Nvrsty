package com.nvrsty.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nvrsty.fragments.EventosFragment;
import com.nvrsty.models.Evento;

/**
 * Created by Matheus on 28/09/2015.
 */
public class TabsAdapterEventos extends FragmentPagerAdapter {
    private Context context;
    private String[] titles = {"REP. DE AULA", "PROVAS", "ATIVIDADES", "TRABALHOS"};
    private EventosFragment repAulaFrag, provaFrag, atividadeFrag, trabalhoFrag;

    public TabsAdapterEventos(FragmentManager fm, Context ctx) {
        super(fm);
        this.context = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        switch (position) {
            case Evento.REP_DE_AULA:
                if (repAulaFrag == null) {
                    repAulaFrag = new EventosFragment();
                    repAulaFrag.setArguments(bundle);
                }
                return repAulaFrag;
            case Evento.PROVA:
                if (provaFrag == null) {
                    provaFrag = new EventosFragment();
                    provaFrag.setArguments(bundle);
                }
                return provaFrag;
            case Evento.TRABALHO:
                if (trabalhoFrag == null) {
                    trabalhoFrag = new EventosFragment();
                    trabalhoFrag.setArguments(bundle);
                }
                return trabalhoFrag;
            case Evento.ATIVIDADE:
                if (atividadeFrag == null) {
                    atividadeFrag = new EventosFragment();
                    atividadeFrag.setArguments(bundle);
                }
                return atividadeFrag;
            default:
                return new Fragment();
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
