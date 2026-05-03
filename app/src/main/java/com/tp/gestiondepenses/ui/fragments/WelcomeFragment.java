package com.tp.gestiondepenses.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.FragmentWelcomeBinding;

import java.util.ArrayList;
import java.util.List;

public class WelcomeFragment extends Fragment {

    private FragmentWelcomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<OnboardingItem> items = new ArrayList<>();
        items.add(new OnboardingItem("Suivi Intelligent", "Gérez vos dépenses et revenus en toute simplicité avec une interface intuitive.", R.drawable.ic_nav_home));
        items.add(new OnboardingItem("Maîtrisez vos Budgets", "Définissez des plafonds par catégorie et recevez des alertes en temps réel.", R.drawable.ic_nav_home));
        items.add(new OnboardingItem("Analyse Précise", "Visualisez la répartition de vos dépenses avec des graphiques clairs et détaillés.", R.drawable.ic_nav_home));

        OnboardingAdapter adapter = new OnboardingAdapter(items);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {}).attach();

        binding.btnNext.setOnClickListener(v -> {
            if (binding.viewPager.getCurrentItem() < items.size() - 1) {
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
            } else {
                finishOnboarding();
            }
        });

        binding.btnSkip.setOnClickListener(v -> finishOnboarding());

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == items.size() - 1) {
                    binding.btnNext.setText("Commencer");
                } else {
                    binding.btnNext.setText("Suivant");
                }
            }
        });
    }

    private void finishOnboarding() {
        SharedPreferences prefs = requireContext().getSharedPreferences("FinanceTrackPrefs", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("onboarding_done", true).apply();
        navigateToNext();
    }

    private void navigateToNext() {
        Navigation.findNavController(requireView()).navigate(R.id.navigation_auth);
    }

    static class OnboardingItem {
        String title, description;
        int imageRes;
        OnboardingItem(String title, String description, int imageRes) {
            this.title = title;
            this.description = description;
            this.imageRes = imageRes;
        }
    }

    static class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {
        private final List<OnboardingItem> items;
        OnboardingAdapter(List<OnboardingItem> items) { this.items = items; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            OnboardingItem item = items.get(position);
            holder.tvTitle.setText(item.title);
            holder.tvDesc.setText(item.description);
            holder.ivImage.setImageResource(item.imageRes);
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivImage;
            TextView tvTitle, tvDesc;
            ViewHolder(View itemView) {
                super(itemView);
                ivImage = itemView.findViewById(R.id.iv_onboarding);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvDesc = itemView.findViewById(R.id.tv_description);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
