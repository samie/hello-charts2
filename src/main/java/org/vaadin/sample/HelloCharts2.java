package org.vaadin.sample;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.Options3d;
import com.vaadin.addon.charts.model.PlotOptionsPie;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.List;

@Theme("valo")
@SuppressWarnings("serial")
public class HelloCharts2 extends UI {

    private static final String SPREADSHEET_KEY = "1GlnG75I-i12iW0I9DCwyCCzUJ79uWTpEgRZVwMKEUjw";

    // The data is in a spreadsheet in Google Docs
    final GoogleSpreadsheet googleSpreadsheet = new GoogleSpreadsheet(SPREADSHEET_KEY);

    final Chart chart = new Chart(ChartType.PIE) {
        {
            setSizeFull();
        }
    };

    final VerticalLayout baseLayout = new VerticalLayout() {
        {
            setSizeFull();
            addComponent(chart);
        }
    };

    @Override
    protected void init(VaadinRequest request) {
        setContent(baseLayout);

        final Configuration conf = chart.getConfiguration();
        conf.setTitle(googleSpreadsheet.getTitle());
        conf.setSubTitle("Total 1645 commits");
        configureChartOptions(conf);

        // Read data from spreadsheet               
        final DataSeries series = new DataSeries("Commits");
        List<DataSeriesItem> data = googleSpreadsheet.readChartRows();
        for (DataSeriesItem slice : data) {
            series.add(slice);
            slice.setSliced(slice.getName().equals("Jonatan"));
        }
        
        conf.setSeries(series);
        chart.drawChart(conf);
    }

    /**
     * This configures the Charts styles.
     */
    private void configureChartOptions(Configuration conf) {

        // Normal pie chart options
        PlotOptionsPie plotOptions = new PlotOptionsPie();
        Labels dataLabels = new Labels(true);
        dataLabels.setFormatter("''+ this.point.name + ''");
        plotOptions.setDataLabels(dataLabels);
        plotOptions.setDepth(45);
        conf.setPlotOptions(plotOptions);

        // Make it 3D also
        Options3d options3d = new Options3d();
        options3d.setEnabled(true);
        options3d.setAlpha(60);
        conf.getChart().setOptions3d(options3d);
    }

}
