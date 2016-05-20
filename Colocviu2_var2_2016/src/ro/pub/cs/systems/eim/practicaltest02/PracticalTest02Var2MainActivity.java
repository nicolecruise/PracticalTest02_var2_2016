package ro.pub.cs.systems.eim.practicaltest02;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.networking.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.networking.ServerThread;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02Var2MainActivity extends Activity {
	
	// Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;
    
    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText urlEditText = null;
    private Button viewPageButton = null;
    private TextView informationTextView = null;
	
    
    private ServerThread serverThread = null;
    private ClientThread clientThread = null;
    
    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }

        }
    }

    private GetWebInfoButtonClickListener getWebInfoButtonClickListener = new GetWebInfoButtonClickListener();
    private class GetWebInfoButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort    = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            String url = urlEditText.getText().toString();
            //String content = informationTypeSpinner.getSelectedItem().toString();
            if (url == null || url.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Parameters from client (city / information type) should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            informationTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress,
                    Integer.parseInt(clientPort),
                    url,
                    informationTextView);
            clientThread.start();
        }
    }
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_var2_main);
		
		 	serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
	        connectButton = (Button)findViewById(R.id.connect_button);
	        connectButton.setOnClickListener(connectButtonClickListener);

	        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
	        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
	        urlEditText = (EditText)findViewById(R.id.url_edit_text);
	        viewPageButton = (Button)findViewById(R.id.button_view_page);
	        viewPageButton.setOnClickListener(getWebInfoButtonClickListener);
	        informationTextView = (TextView)findViewById(R.id.htmlResult);
		
	}
	
	@Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test02_var2_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
