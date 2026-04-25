package sn.esmt.financetrack.ui.budget;

import sn.esmt.financetrack.data.entity.Budget;

public class BudgetWithProgress {
    private Budget budget;
    private double currentExpenses;
    private String categoryName;

    public BudgetWithProgress(Budget budget, double currentExpenses, String categoryName) {
        this.budget = budget;
        this.currentExpenses = currentExpenses;
        this.categoryName = categoryName;
    }

    public Budget getBudget() { return budget; }
    public double getCurrentExpenses() { return currentExpenses; }
    public String getCategoryName() { return categoryName; }

    public int getPercentage() {
        if (budget.getPlafond() <= 0) return 0;
        return (int) Math.min(100, (currentExpenses / budget.getPlafond()) * 100);
    }
}
