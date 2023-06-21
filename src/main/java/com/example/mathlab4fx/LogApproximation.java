package com.example.mathlab4fx;

import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;

public class LogApproximation extends Approximation {
    public LogApproximation(ArrayList<Double> y_values, ArrayList<Double> old_x_values) {
        super(y_values, old_x_values);
    }

    protected void evaluate_coefficients() {
        ArrayList<Double> xStorage = new ArrayList<>(old_x_values);

        for (int i = 0; i < vars_amount_N; i++) {
            old_x_values.set(i, Math.log(old_x_values.get(i)));
        }
        super.evaluate_coefficients();

        old_x_values = xStorage;
    }

    protected void make_new_expression() {
        evaluationToString = approx_coefficients.get(0) + "log(x) + " + approx_coefficients.get(1);
        approx_expr = new ExpressionBuilder(evaluationToString).variable("x").build();
    }

}