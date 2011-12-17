package edu.ucdavis.cros.roadkill;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class MySqlHandler {
	String result = "";
	private ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	private ArrayList<NameValuePair> userData = new ArrayList<NameValuePair>();

	private DefaultHttpClient client = new DefaultHttpClient();
	private HttpPost post;
	private HttpGet request;
	private HttpEntity entity;
	private HttpResponse response;
	List<Cookie> cookies;
	private InputStream is;

	CookieStore cookieStore = new BasicCookieStore();
	HttpContext httpContext = new BasicHttpContext();

	MySqlHandler() {
		try {
			//Log in
			post = new HttpPost("http://www.wildlifecrossing.net/california/");
			userData.add(new BasicNameValuePair("form_id", "user_login_block"));
			userData.add(new BasicNameValuePair("name", "whereisjoey"));
			userData.add(new BasicNameValuePair("pass", "just4you"));
			post.setEntity(new UrlEncodedFormEntity(userData));
			response = client.execute(post);
			entity = response.getEntity();
			is = entity.getContent();

			//Log in confirmation
			request = new HttpGet("http://www.wildlifecrossing.net/california/");
			response = client.execute(request);
			entity = response.getEntity();
			is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = reader.read()) != -1) {
                baf.append((byte) current);
            }
            FileOutputStream fos = new FileOutputStream("/data/data/edu.ucdavis.cros.roadkill/afterlogin.txt");
            fos.write(baf.toByteArray());
            fos.close();
			
            //form page
			request = new HttpGet("http://www.wildlifecrossing.net/california/node/add/roadkill");
			response = client.execute(request);
			entity = response.getEntity();
			is = entity.getContent();
            reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
            
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			
			Integer index = result.indexOf("form-token");
			String formToken = result.substring(index+19, index+51);
			Log.d("MySqlHandler", "formToken: " + formToken);
			
			nameValuePairs.add(new BasicNameValuePair("form_id", "roadkill_node_form"));
			nameValuePairs.add(new BasicNameValuePair("form_token", formToken));
			nameValuePairs.add(new BasicNameValuePair("taxonomy[1]", "7"));
			nameValuePairs.add(new BasicNameValuePair("field_id_confidence[value]", "100% Certain"));
			nameValuePairs.add(new BasicNameValuePair("field_geography[0][street]", "Fake Street"));
			nameValuePairs.add(new BasicNameValuePair("field_geography[0][locpick][user_longitude]", "40.1452892956766"));
			nameValuePairs.add(new BasicNameValuePair("field_geography[0][locpick][user_latitude]", "-126.73828125"));
			nameValuePairs.add(new BasicNameValuePair("field_date_observation[0][value][date]", "2011-11-01"));
			nameValuePairs.add(new BasicNameValuePair("field_date_observation[0][value][time]", "12:30"));
			nameValuePairs.add(new BasicNameValuePair("field_decay_duration[value]", "0"));
			nameValuePairs.add(new BasicNameValuePair("field_observer[0][value]", "Joey"));
			nameValuePairs.add(new BasicNameValuePair("op", "Save"));
			
			post = new HttpPost(
					"http://www.wildlifecrossing.net/california/node/add/roadkill");
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = client.execute(post);
			entity = response.getEntity();
			is = entity.getContent();
			 reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			
		 baf = new ByteArrayBuffer(50);
             current = 0;
            while ((current = reader.read()) != -1) {
                baf.append((byte) current);
            }
           fos = new FileOutputStream("/data/data/edu.ucdavis.cros.roadkill/afterform.txt");
            fos.write(baf.toByteArray());
            fos.close();
			
			
		} catch (Exception e) {
		}
	
	}

	// http post
	void sendRecord() {
		try {
			post = new HttpPost(
					"http://www.wildlifecrossing.net/california/node/add/roadkill");
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = client.execute(post, httpContext);
			entity = response.getEntity();
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
