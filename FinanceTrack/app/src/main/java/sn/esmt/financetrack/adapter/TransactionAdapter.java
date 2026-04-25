package sn.esmt.financetrack.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sn.esmt.financetrack.R;
import sn.esmt.financetrack.databinding.ItemTransactionBinding;
import sn.esmt.financetrack.model.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactions = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.binding.tvDescription.setText(transaction.getDescription());
        holder.binding.tvDate.setText(dateFormat.format(new Date(transaction.getDate())));
        
        if ("REVENU".equals(transaction.getType())) {
            holder.binding.tvMontant.setText("+ " + String.format("%.2f", transaction.getMontant()) + " CFA");
            holder.binding.tvMontant.setTextColor(Color.parseColor("#10B981"));
            holder.binding.ivType.setImageResource(android.R.drawable.ic_input_add);
            holder.binding.ivType.setBackgroundResource(R.drawable.bg_circle_green);
        } else {
            holder.binding.tvMontant.setText("- " + String.format("%.2f", transaction.getMontant()) + " CFA");
            holder.binding.tvMontant.setTextColor(Color.RED);
            holder.binding.ivType.setImageResource(android.R.drawable.ic_delete);
            holder.binding.ivType.setBackgroundResource(R.drawable.bg_circle_red);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        ItemTransactionBinding binding;

        public TransactionViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
