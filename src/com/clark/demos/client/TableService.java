/**
 * 
 */
package com.clark.demos.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author ri18384
 *
 */
@RemoteServiceRelativePath("table")
public interface TableService extends RemoteService {
	String getTable(); 
}
