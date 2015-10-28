package com.nvrsty.extras;

import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Calendar;

/**
 * Created by Matheus on 02/10/2015.
 */
public class Utils {
    private static NumberFormat format = NumberFormat.getInstance();

    static {
        format.setMinimumIntegerDigits(2);
    }

    public static String getDiaSemana(int posicao) {
        String[] days = new String[]{"segunda-feira", "terça-feira", "quarta-feira",
                "quinta-feira", "sexta-feira", "sábado", "domingo"};

        return days[posicao];
    }

    public static String formatarDataDiaMesAno(Calendar calendar) {
        return format.format(calendar.get(Calendar.DAY_OF_MONTH)) + "/" +
                format.format(calendar.get(Calendar.MONTH)) + "/" +
                calendar.get(Calendar.YEAR);
    }

    public static String formatarDataHoraMinutos(Calendar calendar) {
        return format.format(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + format.format(calendar.get(Calendar.MINUTE));
    }

    public static String getTipoStr(int tipo) {
        String[] tipos = new String[]{"Rep. de Aula", "Prova", "Atividade", "Trabalho"};

        return tipos[tipo];
    }
}
