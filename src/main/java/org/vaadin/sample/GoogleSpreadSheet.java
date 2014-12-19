package org.vaadin.sample;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;
import com.vaadin.addon.charts.model.DataSeriesItem;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleSpreadSheet extends SpreadsheetService {

    private static final String MY_APP_ID = "SpreadsheetsDemo";
    private WorksheetFeed feed;

    public GoogleSpreadSheet(String key) {
        super(MY_APP_ID);
        try {
            URL url = FeedURLFactory.getDefault().getWorksheetFeedUrl(key, "public", "full");
            this.feed = getFeed(url, WorksheetFeed.class);
        } catch (IOException | ServiceException ex) {
            Logger.getLogger(GoogleSpreadSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getTitle() {
        return feed.getTitle().getPlainText();
    }

    public List<DataSeriesItem> readChartRows() {
        List<DataSeriesItem> result = new ArrayList<>();

        try {
            List<WorksheetEntry> worksheetList = feed.getEntries();
            WorksheetEntry worksheetEntry = worksheetList.get(0);
            CellFeed listFeed = getFeed(worksheetEntry.getCellFeedUrl(), CellFeed.class);
            String caption = null;
            Double value = null;
            for (CellEntry e : listFeed.getEntries()) {
                if (e.getCell().getCol() == 1) {
                    caption = e.getCell().getValue();
                    value = null;
                } else if (e.getCell().getCol() == 2) {
                    value = e.getCell().getDoubleValue();
                    if (value != Double.NaN && caption != null) {
                        result.add(new DataSeriesItem(caption, value));
                    }
                }
            }
        } catch (ServiceException | IOException ex) {
            Logger.getLogger(GoogleSpreadSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private SortedMap<String, String> rowToMap(ListEntry row) {
        SortedMap<String, String> rowValues = new TreeMap<>();
        for (String tag : row.getCustomElements().getTags()) {
            System.out.print("[" + tag + "=" + row.getCustomElements().getValue(tag) + "]");
            rowValues.put(tag, row.getCustomElements().getValue(tag));
        }
        return rowValues;
    }

}
