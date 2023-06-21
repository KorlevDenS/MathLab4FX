package com.example.mathlab4fx;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import lombok.Setter;
import lombok.Getter;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

@Getter
@Setter
public class Approximation {

    protected ArrayList<Double> y_values;
    protected ArrayList<Double> old_x_values;
    protected ArrayList<Double> new_y_values = new ArrayList<>();
    protected ArrayList<Double> approx_coefficients = new ArrayList<>();
    protected Expression approx_expr;
    protected double deviation_measure_S;
    protected ArrayList<Double> epsilon_list = new ArrayList<>();
    protected double standard_square_deviation;
    protected double pirson_correlation_coefficient;
    protected double r2_evaluation;
    protected int vars_amount_N;
    protected String evaluationToString;

    public Approximation(ArrayList<Double> y_values, ArrayList<Double> old_x_values) {
        this.y_values = new ArrayList<>(y_values);
        this.old_x_values = new ArrayList<>(old_x_values);
        this.vars_amount_N = y_values.size();
    }

    public void approximate() {
        calc_pirson_line_correlation();

        evaluate_coefficients();
        make_new_expression();

        calc_new_x_values();
        find_epsilons();
        calc_standard_square_deviation();
    }

    /*public void power_approximation() {
        calc_pirson_line_correlation();

        evaluate_coefficients_power();
        make_new_power_expression();

        calc_new_x_values();
        find_epsilons();
        calc_standard_square_deviation();
    }*/


    protected void evaluate_coefficients() {
        double sx = 0;
        double sxx = 0;
        double sy = 0;
        double sxy = 0;
        for (int i = 0; i < vars_amount_N; i++) {
            sx += old_x_values.get(i);
            sxx += Math.pow(old_x_values.get(i), 2);
            sy += y_values.get(i);
            sxy += old_x_values.get(i) * y_values.get(i);
        }
        BigDecimal delta = BigDecimal.valueOf(sxx * vars_amount_N - sx * sx);
        BigDecimal delta_1 = BigDecimal.valueOf(sxy * vars_amount_N - sx * sy);
        BigDecimal delta_2 = BigDecimal.valueOf(sxx * sy - sx * sxy);
        approx_coefficients.add(delta_1.divide(delta, 4, RoundingMode.HALF_UP).doubleValue());
        approx_coefficients.add(delta_2.divide(delta, 4, RoundingMode.HALF_UP).doubleValue());
    }

    /*private void make_new_power_expression() {
        String expr = approx_coefficients.get(0) + " * x^" + approx_coefficients.get(1);
        approx_expr = new ExpressionBuilder(expr).variable("x").build();
    }*/

    protected void make_new_expression() {
        evaluationToString = approx_coefficients.get(0) + "x + " + approx_coefficients.get(1);
        approx_expr = new ExpressionBuilder(evaluationToString).variable("x").build();
    }

    protected void calc_new_x_values() {
        for (int i = 0; i < vars_amount_N; i++) {
            BigDecimal x = BigDecimal.valueOf(approx_expr.setVariable("x", old_x_values.get(i)).evaluate())
                    .setScale(4 , RoundingMode.HALF_UP);
            new_y_values.add(x.doubleValue());
        }
    }

    protected void find_epsilons() {
        for (int i = 0; i < vars_amount_N; i++) {
            BigDecimal eps = (BigDecimal.valueOf(approx_expr.setVariable("x", old_x_values.get(i)).evaluate())
                    .subtract(BigDecimal.valueOf(y_values.get(i)))).setScale(4, RoundingMode.HALF_UP);
            epsilon_list.add(eps.doubleValue());
            deviation_measure_S += eps.multiply(eps).doubleValue();
        }
    }

    protected void calc_pirson_line_correlation() {
        double x_mid = old_x_values.stream().mapToDouble(Double::doubleValue).sum() / vars_amount_N;
        double y_mid = y_values.stream().mapToDouble(Double::doubleValue).sum() / vars_amount_N;
        double numerator = 0;
        double denominator1 = 0;
        double denominator2 = 0;
        for (int i = 0; i < vars_amount_N; i++) {
            numerator += (old_x_values.get(i) - x_mid) * (y_values.get(i) - y_mid);
            denominator1 += (old_x_values.get(i) - x_mid) * (old_x_values.get(i) - x_mid);
            denominator2 += (y_values.get(i) - y_mid) * (y_values.get(i) - y_mid);
        }
        pirson_correlation_coefficient = numerator / Math.sqrt(denominator1 * denominator2);
    }

    protected void calc_standard_square_deviation() {
        standard_square_deviation = Math.sqrt(BigDecimal.valueOf(deviation_measure_S)
                .divide(BigDecimal.valueOf(vars_amount_N), 5, RoundingMode.HALF_UP).doubleValue());
    }

    protected double calcDetOf3x3(double[][] a) {
        return a[0][0] * (a[1][1] * a[2][2] - a[2][1] * a[1][2]) -
                a[0][1] * (a[1][0] * a[2][2] - a[2][0] * a[1][2]) +
                a[0][2] * (a[1][0] * a[2][1] - a[2][0] * a[1][1]);
    }

}
