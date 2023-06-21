package com.example.mathlab4fx;

import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class CubeApproximation extends Approximation {

    public CubeApproximation(ArrayList<Double> y_values, ArrayList<Double> old_x_values) {
        super(y_values, old_x_values);
    }

    protected void evaluate_coefficients() {
        double sn = vars_amount_N;
        double sx = 0;
        double sxx = 0;
        double sy = 0;
        double s3x = 0;
        double sxy = 0;
        double s4x = 0;
        double sxxy = 0;
        double s5x = 0;
        double s6x = 0;
        double s3xy = 0;

        for (int i = 0; i < vars_amount_N; i++) {
            sx += old_x_values.get(i);
            sxx += Math.pow(old_x_values.get(i), 2);
            sy += y_values.get(i);
            s3x += Math.pow(old_x_values.get(i), 3);
            sxy += old_x_values.get(i) * y_values.get(i);
            s4x += Math.pow(old_x_values.get(i), 4);
            sxxy += Math.pow(old_x_values.get(i), 2) * y_values.get(i);
            s5x += Math.pow(old_x_values.get(i), 5);
            s6x += Math.pow(old_x_values.get(i), 6);
            s3xy += Math.pow(old_x_values.get(i), 3) * y_values.get(i);
        }

        double detA = calcDetOf4x4(new double[][]{{sn, sx, sxx, s3x}, {sx, sxx, s3x, s4x}, {sxx, s3x, s4x, s5x}, {s3x, s4x, s5x, s6x}});
        double detA0 = calcDetOf4x4(new double[][]{{sy, sx, sxx, s3x}, {sxy, sxx, s3x, s4x}, {sxxy, s3x, s4x, s5x}, {s3xy, s4x, s5x, s6x}});
        double detA1 = calcDetOf4x4(new double[][]{{sn, sy, sxx, s3x}, {sx, sxy, s3x, s4x}, {sxx, sxxy, s4x, s5x}, {s3x, s3xy, s5x, s6x}});
        double detA2 = calcDetOf4x4(new double[][]{{sn, sx, sy, s3x}, {sx, sxx, sxy, s4x}, {sxx, s3x, sxxy, s5x}, {s3x, s4x, s3xy, s6x}});
        double detA3 = calcDetOf4x4(new double[][]{{sn, sx, sxx, sy}, {sx, sxx, s3x, sxy}, {sxx, s3x, s4x, sxxy}, {s3x, s4x, s5x, s3xy}});

        approx_coefficients.add(BigDecimal.valueOf(detA3)
                .divide(BigDecimal.valueOf(detA), 4, RoundingMode.HALF_UP).doubleValue());
        approx_coefficients.add(BigDecimal.valueOf(detA2)
                .divide(BigDecimal.valueOf(detA), 4, RoundingMode.HALF_UP).doubleValue());
        approx_coefficients.add(BigDecimal.valueOf(detA1)
                .divide(BigDecimal.valueOf(detA), 4, RoundingMode.HALF_UP).doubleValue());
        approx_coefficients.add(BigDecimal.valueOf(detA0)
                .divide(BigDecimal.valueOf(detA), 4, RoundingMode.HALF_UP).doubleValue());

    }

    protected void make_new_expression() {
        evaluationToString = approx_coefficients.get(0) + "x^3 + " + approx_coefficients.get(1) + "x^2 + " + approx_coefficients.get(2) + "x + " + approx_coefficients.get(3);
        approx_expr = new ExpressionBuilder(evaluationToString).variable("x").build();
    }

    protected double calcDetOf4x4(double[][] a) {
        return a[0][0] * calcDetOf3x3(new double[][]{{a[1][1], a[1][2], a[1][3]}, {a[2][1], a[2][2], a[2][3]}, {a[3][1], a[3][2], a[3][3]}}) -
                a[0][1] * calcDetOf3x3(new double[][]{{a[1][0], a[1][2], a[1][3]}, {a[2][0], a[2][2], a[2][3]}, {a[3][0], a[3][2], a[3][3]}}) +
                a[0][2] * calcDetOf3x3(new double[][]{{a[1][0], a[1][1], a[1][3]}, {a[2][0], a[2][1], a[2][3]}, {a[3][0], a[3][1], a[3][3]}}) -
                a[0][3] * calcDetOf3x3(new double[][]{{a[1][0], a[1][1], a[1][2]}, {a[2][0], a[2][1], a[2][2]}, {a[3][0], a[3][1], a[3][2]}});
    }
}
