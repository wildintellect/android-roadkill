package edu.ucdavis.cros.roadkill;

import java.io.BufferedReader;
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
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.util.Log;

public class CopyOfMySqlHandler {
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

	CopyOfMySqlHandler(Context ctx) {
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		/*
		try{
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("name", "test"));
		nvps.add(new BasicNameValuePair("age", "3"));
		post = new HttpPost("http://munchits.com/roadkill.php");
		post.setEntity(new UrlEncodedFormEntity(nvps));
		
		response = client.execute(post);
		} catch(Exception e) {
		
		}
		*/
		/*
		try{
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("post", "test"));
		nvp.add(new BasicNameValuePair("submit", "submit"));
		post = new HttpPost("http://boyscantbetrusted.com/submit.php");
		post.setEntity(new UrlEncodedFormEntity(nvp));

		
		response = client.execute(post);
		} catch(Exception e) {
		
		}
		*/
		request = new HttpGet("http://www.wildlifecrossing.net/california/");
		try {
			response = client.execute(request, httpContext);
		} catch (Exception e) {
		} 
		entity = response.getEntity();
		Log.d("MySqlHandler", "Login form get: " + response.getStatusLine());
		if(entity != null)
	    {
	        try {
				entity.consumeContent();
			} catch (Exception e) {
			}
	    }
	    Log.d("MySqlHandler", "Initial set of cookies:");
	    cookies = client.getCookieStore().getCookies();
	    httpContext.setAttribute(ClientContext.COOKIE_STORE, client.getCookieStore());
	    if (cookies.isEmpty())
	    {
	        Log.d("MySqlHandler", "None");
	    }
	    else
	    {
	        for(int i = 0; i<cookies.size(); i++)
	        {
	            Log.d("MySqlHandler", "- " + cookies.get(i));
	        }
	    }
		
		post = new HttpPost("http://www.wildlifecrossing.net/california/");
        userData.add(new BasicNameValuePair("edit-name", "whereisjoey"));
		userData.add(new BasicNameValuePair("edit-password", "just4you"));
		//userData.add(new BasicNameValuePair("remember me", "on"));
		userData.add(new BasicNameValuePair("edit-submit", "Log in"));
		try {
			post.setEntity(new UrlEncodedFormEntity(userData, HTTP.UTF_8));
			response = client.execute(post, httpContext);
			entity = response.getEntity();
			//////////
			is = entity.getContent();
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
			Log.d("MySqlHandler", "Success??? " + result);
			///////////
			Log.d("MySqlHandler", "Login form get: " + response.getStatusLine());
		    if(entity != null){
		        entity.consumeContent();
		    }
		    Log.d("MySqlHandler", "Post logon cookies:");
		    cookies = client.getCookieStore().getCookies();
		    httpContext.setAttribute(ClientContext.COOKIE_STORE, client.getCookieStore());
		    if (cookies.isEmpty())
		    {
		        Log.d("MySqlHandler", "None");
		    } 
		    else
		    {
		        for (int i = 0; i < cookies.size(); i++) 
		        {
		            Log.d("MySqlHandler", "- " + cookies.get(i));
		        }
		    }
		    request = new HttpGet("http://www.wildlifecrossing.net/california/");

		    response = client.execute(request, httpContext);
		    ///////////
			entity = response.getEntity();
			is = entity.getContent();
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
			/////////////////
		    Log.d("MySqlHandler", "Check for login: " + result);
		    if(entity != null)
		    {
		        entity.consumeContent();
		    }
		}
		catch(Exception e) {
		}
		nameValuePairs.add(new BasicNameValuePair("edit-taxonomy-1", "7"));
		nameValuePairs.add(new BasicNameValuePair("edit-field-id-confidence-value", "100% Certain"));
		nameValuePairs.add(new BasicNameValuePair("edit-field-geography-0-street", "Fake Street"));
		nameValuePairs.add(new BasicNameValuePair("gmap-auto1map-locpick_latitude0", "40.1452892956766"));
		nameValuePairs.add(new BasicNameValuePair("gmap-auto1map-locpick_longitude0", "-126.73828125"));
		nameValuePairs.add(new BasicNameValuePair("edit-field-date-observation-0-value-datepicker-popup-0", "2011-11-01"));
		nameValuePairs.add(new BasicNameValuePair("edit-field-date-observation-0-value-timeEntry-popup-1", "12:30"));
		nameValuePairs.add(new BasicNameValuePair("edit-field-decay-duration-value", "0"));
		nameValuePairs.add(new BasicNameValuePair("edit-field-observer-0-value", "Joey"));
		nameValuePairs.add(new BasicNameValuePair("edit-submit", "Save"));
	}

	// http post
	void sendRecord() {
		try {
			post = new HttpPost("http://www.wildlifecrossing.net/california/node/add/roadkill");
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
