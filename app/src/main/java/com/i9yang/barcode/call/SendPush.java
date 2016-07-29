package com.i9yang.barcode.call;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendPush extends AsyncTask<String, Void, Void> {
	protected Void doInBackground(String... params) {
		executeClient(params[0]);
		return null;
	}

	public void executeClient(String number) {
		String accessToken = "o.OOQG9C67rK4tr2MVPbe6JW8oz2dQPjE8";
		String pushUrl = "https://api.pushbullet.com/v2/pushes";
		String deviceIdn = "ujBdZ7MWczQsjAnjr1gpPw";

		try{
			URL url = new URL(pushUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setRequestProperty("Content-Type", "application/json");
			urlConnection.setRequestProperty("Access-Token", accessToken);
			OutputStreamWriter os = new OutputStreamWriter( urlConnection.getOutputStream());
			String body =
					"{" +
					"\"body\" : \"" + number + "\"," +
					"\"title\" : \"Calling!!!!!!\"," +
					"\"device_iden\" : \"" + deviceIdn + "\"," +
					"\"type\" : \"note\"" +
					"}";

			os.write(body);
			os.flush();
			os.close();

			StringBuilder sb = new StringBuilder();
			int HttpResult = urlConnection.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				Log.d("test",sb.toString());
			} else {
				Log.d("test",urlConnection.getResponseMessage());
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
