package ro.pub.cs.systems.eim.practicaltest02.networking;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import android.util.Log;
import android.widget.Toast;

public class CommunicationThread extends Thread {

	private ServerThread serverThread;
	private Socket       socket;

	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket       = socket;
	}

	@Override
	public void run() {
		if (socket != null) {
			try {
				
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
					String url = bufferedReader.readLine();

					HashMap<String, String> data = serverThread.getData();
					String info = null;
					
					if (url.contains("bad")) {
						Log.i(Constants.TAG, "[COMMUNICATION THREAD] RESTRICTED");
						info="RESTRICTED";
//						Toast.makeText(
//								getApplicationContext(),
//								"Restricted!",
//								Toast.LENGTH_SHORT
//							).show();
//							return;
						
					} else {
					
						if (url != null && !url.isEmpty()) {
							if (data.containsKey(url)) {
								Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
								info = data.get(url);
							} else {
								// interoghez serverul http
								Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice..."+url);
								HttpClient httpClient = new DefaultHttpClient();
	
								HttpGet httpGet=new HttpGet(url);
								ResponseHandler<String> responseHandler = new BasicResponseHandler();
	
								String response = httpClient.execute(httpGet,responseHandler);
	
								Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
								if (response != null) {
									Log.i(Constants.TAG, "Response not null " + response);
	
	
									serverThread.setData(url, response);
									printWriter.println(response);
									printWriter.flush();
	
	
								}
	
	
							}
						}
					}
					
					
				}

				socket.close();
			} catch (IOException ioException) {
				Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}
			} 
		} else {
			Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
		}
	}
}
