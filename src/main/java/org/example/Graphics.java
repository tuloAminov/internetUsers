package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.Color;
import java.util.Map;

public class Graphics extends JFrame {

    public Graphics(Map<String, Double> internetUsers) {
        initUI(internetUsers);
    }

    private void initUI(Map<String, Double> internetUsers) {

        CategoryDataset dataset = createDataset(internetUsers);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setVerticalAxisTrace(true);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Bar chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private CategoryDataset createDataset(Map<String, Double> internetUsers) {
        var dataset = new DefaultCategoryDataset();
        internetUsers.forEach((key, value) -> dataset.setValue(value, "internetUsers", key));
        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        return ChartFactory.createBarChart(
                "internetUsers by subregions",
                "subregions",
                "internetUsers",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
    }
}