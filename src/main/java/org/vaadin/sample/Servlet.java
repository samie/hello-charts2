package org.vaadin.sample;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(value = {"/*"}, asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = HelloCharts2.class, widgetset = "org.vaadin.sample.AppWidgetSet")
public class Servlet extends VaadinServlet {

}
