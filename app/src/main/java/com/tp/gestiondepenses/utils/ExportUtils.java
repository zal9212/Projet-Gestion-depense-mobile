package com.tp.gestiondepenses.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import com.tp.gestiondepenses.model.Depense;
import com.tp.gestiondepenses.model.Revenu;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExportUtils {

    public static void exportTransactionsToCSV(Context context, List<Depense> depenses, List<Revenu> revenus) {
        StringBuilder csv = new StringBuilder();
        csv.append("Type,Description,Montant,Date,Categorie\n");

        for (Depense d : depenses) {
            csv.append("Dépense,")
               .append(d.description).append(",")
               .append(d.montant).append(",")
               .append(d.date).append(",")
               .append(d.categorie_id).append("\n");
        }

        for (Revenu r : revenus) {
            csv.append("Revenu,")
               .append(r.description).append(",")
               .append(r.montant).append(",")
               .append(r.date).append(",")
               .append(r.source).append("\n");
        }

        try {
            File file = new File(context.getExternalFilesDir(null), "transactions.csv");
            FileOutputStream out = new FileOutputStream(file);
            out.write(csv.toString().getBytes());
            out.close();

            Uri path = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_STREAM, path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(intent, "Partager le rapport CSV"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
