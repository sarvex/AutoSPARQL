package org.autosparql.client.controller;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.autosparql.client.AppEvents;
import org.autosparql.client.AutoSPARQLServiceAsync;
import org.autosparql.client.view.ApplicationView;
import org.autosparql.client.widget.ErrorDialog;
import org.autosparql.client.widget.WaitDialog;
import org.autosparql.shared.Example;
import org.openjena.atlas.logging.Log;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ApplicationController extends Controller
{
	final AutoSPARQLServiceAsync service;
	private ApplicationView appView;
	private Logger log = Logger.getLogger(ApplicationController.class.toString());

	public ApplicationController(AutoSPARQLServiceAsync service)
	{
		this.service = service;
		registerEventTypes(AppEvents.Init);
		registerEventTypes(AppEvents.Error);
	}

	public void handleEvent(AppEvent event)
	{
		EventType type = event.getType();
		if (type == AppEvents.Init) {
			onInit(event);
		} else if (type == AppEvents.Error) {
			onError((Throwable)event.getData());
		}
	}
	
//	public void learn()
//	{
//		
//	}
	
	public void initialize()
	{
		String query = com.google.gwt.user.client.Window.Location.getParameter("query");
		if(query==null||query.isEmpty())
		{
			
		}
		else
		{
			//appView.display(Collections.<Example>emptyList());
			final WaitDialog wait = new WaitDialog("Creating table");
			wait.show();
			log.info("Getting initial examples...");
			service.getExamples(query, new AsyncCallback<List<Example>>() {				
				@Override
				public void onSuccess(List<Example> examples)
				{
					//new Example("testuri", "testlabel", "testimageurl", "testcomment");
					wait.hide();
					log.info("successfully gotten the initial examples, displaying them now.");
					appView.display(examples);
					log.info("successfully displayed the initial examples");
				}
			
				@Override
				public void onFailure(Throwable arg0)
				{
					log.severe(arg0.getMessage());
					//log.severe(arg0.getCause().toString());
					wait.hide();
					com.google.gwt.user.client.Window.alert(arg0.getMessage());
					
					//appView.showError("could not get examples");
					// TODO Auto-generated method stub
					
				}
			});
			        	
		}
		appView = new ApplicationView(this);
	}

	protected void onError(Throwable throwable) {
		ErrorDialog dialog = new ErrorDialog(throwable);
		dialog.showDialog();
	}

	private void onInit(AppEvent event) {
		forwardToView(appView, event);
	}

}
