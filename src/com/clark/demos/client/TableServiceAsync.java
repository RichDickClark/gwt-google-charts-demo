package com.clark.demos.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TableServiceAsync {
	void getTable( AsyncCallback<String> callback );
}
