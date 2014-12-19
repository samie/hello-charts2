package org.vaadin.sample;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.Options3d;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.List;

@Theme("valo")
@SuppressWarnings("serial")
public class HelloCharts2 extends UI {

    private static final String SPREADSHEET_KEY = "1GlnG75I-i12iW0I9DCwyCCzUJ79uWTpEgRZVwMKEUjw";
    private static final String SPREADSHEET_URL = "https://docs.google.com/a/vaadin.com/spreadsheets/d/" + SPREADSHEET_KEY + "/pubhtml";

    final GoogleSpreadSheet gsheet = new GoogleSpreadSheet(SPREADSHEET_KEY);
    final Chart chart = new Chart(ChartType.PIE) {{
            setSizeFull();
        }};
    final HorizontalLayout links = new HorizontalLayout() {{
            setMargin(true);
            setWidth("100%");
            addComponent(new Link("Spreadsheet data",
                    new ExternalResource(SPREADSHEET_URL)));
            addComponent(new Link("Available in GitHub",
                    new ExternalResource("https://vaadin.com/samie")));
        }};
    
    final VerticalLayout baseLayout = new VerticalLayout() {{
            setSizeFull();
            addComponent(chart);
            addComponent(links);
            setExpandRatio(chart, 1);
        }};


    @Override
    protected void init(VaadinRequest request) {
        setContent(baseLayout);

        final Configuration conf = chart.getConfiguration();
        conf.setTitle(gsheet.getTitle());
        
        // Configure all
        configureChartOptions(conf);

        // Add data from spreadsheet               
        final DataSeries series = new DataSeries("Commits");
        List<DataSeriesItem> data = gsheet.readChartRows();
        if (data != null) {
            for (DataSeriesItem slice : data) {
                slice.setSliced(true);
                slice.setSelected(true);
                series.add(slice);
            }
        }
        conf.setSeries(series);
        chart.drawChart(conf);
    }

    private void configureChartOptions(Configuration conf) {
        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        Labels dataLabels = new Labels(true);
        dataLabels.setFormatter("''+ this.point.name + ''");
        plotOptions.setDataLabels(dataLabels);
        plotOptions.setDepth(45);
        conf.setPlotOptions(plotOptions);

        // Make it 3D
        Options3d options3d = new Options3d();
        options3d.setEnabled(true);
        options3d.setAlpha(60);
        conf.getChart().setOptions3d(options3d);
    }

}
