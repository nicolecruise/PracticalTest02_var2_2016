package ro.pub.cs.systems.eim.practicaltest02.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread {

	private String address;
	private int port;
	private String url;

	private TextView pageDisplayTextView;

	private Socket socket;

	// constructor
	public ClientThread(String address, int port, String urele,
			TextView pageDisplayTextView) {
		this.address = address;
		this.port = port;
		this.url = urele;

		this.pageDisplayTextView = pageDisplayTextView;
	}

	@Override
	public void run() {
		try {
			socket = new Socket(address, port);
			if (socket == null) {
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
			}

			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter printWriter = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(url);
				printWriter.flush();

				String webInformation;
				// informatia ce vine de la server
				while ((webInformation = bufferedReader.readLine()) != null) {
					// creez string final si scriu in el
					final String finalizedWebInformation = webInformation;
					// facem thread nou runnable pe care scriu in controlul
					// grafic
					pageDisplayTextView.post(new Runnable() {
						@Override
						public void run() {
							pageDisplayTextView
									.append(finalizedWebInformation + "\n");
						}
					});
				}
			} else {
				Log.e(Constants.TAG,
						"[CLIENT THREAD] BufferedReader / PrintWriter are null!");
			}
			socket.close();
		} catch (IOException ioException) {
			Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: "
					+ ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
	}

}