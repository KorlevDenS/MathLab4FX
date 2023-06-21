package com.example.mathlab4fx;

import lombok.Getter;
import lombok.Setter;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@Getter
@Setter
public class SquareApproximation extends Approximation {
    public SquareApproximation(ArrayList<Double> y_values, ArrayList<Double> old_x_values) {
        super(y_values, old_x_values);
    }

    protected void evaluate_coefficients() {
        double sn = vars_amount_N;
        double sx = 0;
        double sxx = 0;
        double sy = 0;
        double sxxx = 0;
        double sxy = 0;
        double sxxxx = 0;
        double sxxy = 0;

        for (int i = 0; i < vars_amount_N; i++) {
            sx += old_x_values.get(i);
            sxx += Math.pow(old_x_values.get(i), 2);
            sy += y_values.get(i);
            sxxx += Math.pow(old_x_values.get(i), 3);
            sxy += old_x_values.get(i) * y_values.get(i);
            sxxxx += Math.pow(old_x_values.get(i), 4);
            sxxy += Math.pow(old_x_values.get(i), 2) * y_values.get(i);
        }

        double detA = calcDetOf3x3(new double[][]{{sn, sx, sxx}, {sx, sxx, sxxx}, {sxx, sxxx, sxxxx}});
        double detA0 = calcDetOf3x3(new double[][]{{sy, sx, sxx}, {sxy, sxx, sxxx}, {sxxy, sxxx, sxxxx}});
        double detA1 = calcDetOf3x3(new double[][]{{sn, sy, sxx}, {sx, sxy, sxxx}, {sxx, sxxy, sxxxx}});
        double detA2 = calcDetOf3x3(new double[][]{{sn, sx, sy}, {sx, sxx, sxy}, {sxx, sxxx, sxxy}});

        approx_coefficients.add(BigDecimal.valueOf(detA2)
                .divide(BigDecimal.valueOf(detA), 4, RoundingMode.HALF_UP).doubleValue());
        approx_coefficients.add(BigDecimal.valueOf(detA1)
                .divide(BigDecimal.valueOf(detA), 4, RoundingMode.HALF_UP).doubleValue());
        approx_coefficients.add(BigDecimal.valueOf(detA0)
                .divide(BigDecimal.valueOf(detA), 4, RoundingMode.HALF_UP).doubleValue());
    }

    protected void make_new_expression() {
        evaluationToString = approx_coefficients.get(0) + "x^2 + " + approx_coefficients.get(1) + "x + " + approx_coefficients.get(2);
        approx_expr = new ExpressionBuilder(evaluationToString).variable("x").build();
    }

}
