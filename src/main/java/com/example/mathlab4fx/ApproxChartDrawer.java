package com.example.mathlab4fx;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.util.ArrayList;
import java.util.List;

public class ApproxChartDrawer {

    private final List<Approximation> appList;
    public ApproxChartDrawer(List<Approximation> appList) {
        this.appList = new ArrayList<>(appList);
    }

    public void drawChart(int width, int height, Styler.ChartTheme theme, String title) {
        ArrayList<Double> xData = new ArrayList<>(appList.get(0).getOld_x_values());
        ArrayList<Double> yData = new ArrayList<>(appList.get(0).getY_values());
        XYChart chart = new XYChartBuilder().width(width).height(height).theme(theme).title(title).build();
        XYSeries points = chart.addSeries("points", xData, yData);
        points.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        appList.forEach(app -> addToChart(xData, app, chart));
        new SwingWrapper<>(chart).displayChart();
    }

    private void addToChart(List<Double> xData, Approximation app, XYChart chart) {
        List<List<Double>> list = createSeries(xData, app);
        XYSeries series1 = chart.addSeries(app.getEvaluationToString(), list.get(0), list.get(1));
        series1.setMarker(SeriesMarkers.NONE);
    }

    private List<List<Double>> createSeries(List<Double> xData, Approximation app) {
        List<Double> xData1 = new ArrayList<>();
        List<Double> yData1 = new ArrayList<>();
        double xMin = xData.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
        double xMax = xData.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
        double len = xMax - xMin;
        double part = len / 100;
        while (xMin < xMax) {
            yData1.add(app.getApprox_expr().setVariable("x", xMin).evaluate());
            xData1.add(xMin);
            xMin += part;
        }
        yData1.add(app.getApprox_expr().setVariable("x", xMin).evaluate());
        xData1.add(xMin);
        List<List<Double>> list = new ArrayList<>();
        list.add(xData1);
        list.add(yData1);
        return list;
    }
}
