package com.example.mathlab4fx;

import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainT {

    public static void main(String[] args) {

        List<Approximation> approxList = new ArrayList<>();

        //Double[] y_arr = {2.73, 5.12, 7.74, 8.91, 10.59, 12.75, 13.43};
        //Double[] x_arr = {1.1, 2.3, 3.7, 4.5, 5.4, 6.8, 7.5};

        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> x = new ArrayList<>();

        ApproxIO approxIO1 = new ApproxIO();
        try {
            List<List<Double>> xy = approxIO1.tryToRead();
            x = (ArrayList<Double>) xy.get(0);
            y = (ArrayList<Double>) xy.get(1);
        } catch (WrongInputException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Невозможно прочитать данные");
            System.exit(1);
        }

        //Double[] y_arr = {3.5, 4.1, 5.2, 6.9, 8.3, 14.8, 21.2};
        //Double[] x_arr = {1.1, 2.3, 3.7, 4.5, 5.4, 6.8, 7.5};

        boolean foundXisOrLessThen0 = false;
        boolean foundYisOrLessThen0 = false;

        for (Double num : x)
            if (num <= 0) {
                foundXisOrLessThen0 = true;
                break;
            }

        for (Double num : y)
            if (num <= 0) {
                foundYisOrLessThen0 = true;
                break;
            }


        StringBuilder comments = new StringBuilder();

        approxList.add(new Approximation(y, x));
        approxList.add(new SquareApproximation(y, x));
        approxList.add(new CubeApproximation(y, x));

        if (!foundYisOrLessThen0 && !foundXisOrLessThen0) {
            approxList.add(new PowerApproximation(y, x));
            comments.append("Аппроксимация всех функций удачна\n");
        } else {
            comments.append("Аппроксимация степенной функции не удалась, есть Yi или Xi <= 0.\n");
        }

        if (!foundYisOrLessThen0) {
            approxList.add(new ExpApproximation(y, x));
        } else {
            comments.append("Аппроксимация экспоненциальной функции не удалась, есть Yi <= 0.\n");
        }

        if (!foundXisOrLessThen0) {
            approxList.add(new LogApproximation(y, x));
        } else {
            comments.append("Аппроксимация логарифмической функции не удалась, есть Xi <= 0.\n");
        }


        approxList.forEach(Approximation::approximate);

        ApproxIO approxIO = new ApproxIO();
        System.out.println(approxIO.formDataArrays(approxList));
        System.out.println(comments);




        ApproxChartDrawer drawer = new ApproxChartDrawer(approxList);
        drawer.drawChart(1000, 600, Styler.ChartTheme.Matlab, "Approximate functions");
    }
}
