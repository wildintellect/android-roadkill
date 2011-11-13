package edu.ucdavis.cros.roadkill;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class MySqlHandler {
	String result = "";
	private ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	private InputStream is;

	MySqlHandler() {
		nameValuePairs.add(new BasicNameValuePair("name", "Sarah"));
		nameValuePairs.add(new BasicNameValuePair("age", "4"));
	}

	// http post
	void sendRecord() {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://munchits.com/roadkill.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
		} catch (Exception e) {
		}
		/*
		 * // parse json data try { JSONArray jArray = new JSONArray(result);
		 * for (int i = 0; i < jArray.length(); i++) { JSONObject json_data =
		 * jArray.getJSONObject(i);
		 * 
		 * }
		 * 
		 * } catch (JSONException e) { }
		 */
	}

}
