package com.clark.demos.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
//import com.google.gwt.visualization.client.visualizations.PieChart;
//import com.google.gwt.visualization.client.visualizations.PieChart.Options;

public class Google_Visualization___GWT implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";	
	
  public void onModuleLoad() {
	 
	/**
	 *  Create the popup dialog box for displaying errors
	 */
	final DialogBox dialogBox = new DialogBox();
	dialogBox.setText("Remote Procedure Call");
	dialogBox.setAnimationEnabled(true);
	final Button closeButton = new Button("Close");
	// We can set the id of a widget by accessing its Element
	closeButton.getElement().setId("closeButton");
	final Label textToServerLabel = new Label();
	final HTML serverResponseLabel = new HTML();
	VerticalPanel dialogVPanel = new VerticalPanel();
	dialogVPanel.addStyleName("dialogVPanel");
	dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
	dialogVPanel.add(textToServerLabel);
	dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
	dialogVPanel.add(serverResponseLabel);
	dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
	dialogVPanel.add(closeButton);
	dialogBox.setWidget(dialogVPanel);

	// Add a handler to close the DialogBox
	closeButton.addClickHandler(new ClickHandler() {
		public void onClick(ClickEvent event) {
			dialogBox.hide();
		}
	});
	  
    // Create a callback to be called when the visualization API
    // has been loaded.
    Runnable onLoadCallback = new Runnable() {
        public void run() {
            final Panel panel = RootPanel.get();
     
            tableService.getTable(new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					AbstractDataTable data = toDataTable(result);
			        BarChart pie = new BarChart(data, createOptions());

			        pie.addSelectHandler(createSelectHandler(pie));
			        panel.add(pie);				
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					dialogBox
							.setText("Remote Procedure Call - Failure");
					serverResponseLabel
							.addStyleName("serverResponseLabelError");
					serverResponseLabel.setHTML(SERVER_ERROR);
					dialogBox.center();
					closeButton.setFocus(true);				
				}
			});
          }
        };

    // Load the visualization api, passing the onLoadCallback to be called
    // when loading is done.
    VisualizationUtils.loadVisualizationApi(onLoadCallback, BarChart.PACKAGE);
    
  }
  
	/**
	 * Create a remote service proxy to talk to the server-side Table service.
	 */
	private final TableServiceAsync tableService = GWT
			.create(TableService.class);  
	
	public static native DataTable toDataTable(String json) /*-{
	  return new $wnd.google.visualization.DataTable(eval("(" + json + ")"));
	}-*/;	

  private Options createOptions() {
    Options options = Options.create();
    options.setWidth(400);
    options.setHeight(240);
//    options.set3D(true);
    options.setTitle("My Daily Activities");
    return options;
  }

  private SelectHandler createSelectHandler(final BarChart chart) {
    return new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        String message = "";
        
        // May be multiple selections.
        JsArray<Selection> selections = chart.getSelections();

        for (int i = 0; i < selections.length(); i++) {
          // add a new line for each selection
          message += i == 0 ? "" : "\n";
          
          Selection selection = selections.get(i);

          if (selection.isCell()) {
            // isCell() returns true if a cell has been selected.
            
            // getRow() returns the row number of the selected cell.
            int row = selection.getRow();
            // getColumn() returns the column number of the selected cell.
            int column = selection.getColumn();
            message += "cell " + row + ":" + column + " selected";
          } else if (selection.isRow()) {
            // isRow() returns true if an entire row has been selected.
            
            // getRow() returns the row number of the selected row.
            int row = selection.getRow();
            message += "row " + row + " selected";
          } else {
            // unreachable
            message += "Pie chart selections should be either row selections or cell selections.";
            message += "  Other visualizations support column selections as well.";
          }
        }
        
        Window.alert(message);
      }
    };
  }

//  private AbstractDataTable createTable() {
//    DataTable data = DataTable.create();
//    data.addColumn(ColumnType.STRING, "Task");
//    data.addColumn(ColumnType.NUMBER, "Hours per Day");
//    data.addRows(2);
//    data.setValue(0, 0, "Work");
//    data.setValue(0, 1, 14);
//    data.setValue(1, 0, "Sleep");
//    data.setValue(1, 1, 10);
//    return data;
//  }
}
