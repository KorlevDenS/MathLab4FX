package com.example.mathlab4fx;

import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class PowerApproximation extends Approximation {

    public PowerApproximation(ArrayList<Double> y_values, ArrayList<Double> old_x_values) {
        super(y_values, old_x_values);
    }

    protected void evaluate_coefficients() {
        ArrayList<Double> yStorage = new ArrayList<>(y_values);
        ArrayList<Double> xStorage = new ArrayList<>(old_x_values);

        for (int i = 0; i < vars_amount_N; i++) {
            y_values.set(i, Math.log(y_values.get(i)));
            old_x_values.set(i, Math.log(old_x_values.get(i)));
        }
        super.evaluate_coefficients();
        approx_coefficients.set(1, BigDecimal.valueOf(Math.pow(Math.E, approx_coefficients.get(1)))
                .setScale(4, RoundingMode.HALF_UP).doubleValue());

        y_values = yStorage;
        old_x_values = xStorage;
    }

    protected void make_new_expression() {
        evaluationToString = approx_coefficients.get(1) + "x^" + approx_coefficients.get(0);
        approx_expr = new ExpressionBuilder(evaluationToString).variable("x").build();
    }
}
