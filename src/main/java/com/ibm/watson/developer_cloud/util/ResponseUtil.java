/**
 * Copyright 2015 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.watson.developer_cloud.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Utility class to manage service responses.
 *
 * @author German Attanasio Ruiz (germanatt@us.ibm.com)
 * @see org.apache.http.HttpResponse HttpResponse
 */
public class ResponseUtil {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ResponseUtil.class
			.getName());
	
	/** The Constant BUFFER_SIZE. */
	public static final int BUFFER_SIZE = 8192; // 8 kb

	/**
	 * Returns a Json {@link String} in human-readable form.
	 * 
	 * @param json
	 *            the Json string
	 * @return the human-readable string
	 */
	public static String formatJSON(String json) {
		try {
			JsonParser parser = new JsonParser();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			return gson.toJson(parser.parse(json));
		} catch (Exception e) {
			log.log(Level.SEVERE,json + " is not valid", e);
			return json;
		}
	}

	/**
	 * Return a {@link JsonElement} representation of the response.
	 * 
	 * @param response
	 *            the HttpResponse
	 * @return the content body as JSON
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static JsonElement getJsonElement(HttpResponse response) throws IOException {
		final String json = getString(response);
		if (json == null || json.length() == 0)
			throw new IOException("JSON response is empty");

		JsonElement element = new JsonParser().parse(json);
		return element;
	}

	/**
	 * Returns a {@link JsonArray} representation of the response.
	 *
	 * @param response            an HTTP response
	 * @return the content body as JsonArray
	 * @throws IOException  If an input or output
	 * 						exception occurred
	 */
	public static JsonArray getJsonArray(HttpResponse response)
			throws IOException {
		final JsonElement json = getJsonElement(response);
		return json.getAsJsonArray();
	}


	/**
	 * Returns a {@link JsonObject} representation of the response.
	 *
	 * @param response            an HTTP response
	 * @return the content body as JSONArray
	 * @throws IOException  If an input or output
	 * 						exception occurred
	 */
	public static JsonObject getJsonObject(HttpResponse response) throws IOException {
		return getJsonElement(response).getAsJsonObject();
	}

	/**
	 * Returns a String representation of the response.
	 * 
	 * @param response
	 *            an HTTP response
	 * @return the content body as String
	 * @throws IOException
	 *             network error
	 * */
	public static String getString(HttpResponse response) throws IOException {
		InputStream is;
		try {
			is = response.getEntity().getContent();
			if (is == null)
				return null;

			int length = BUFFER_SIZE;
			Header contentLength = response.getFirstHeader(HTTP.CONTENT_LEN);

			if (contentLength != null) {
				try {
					length = Integer.parseInt(contentLength.getValue());
				} catch (NumberFormatException e) {
					log.log(Level.SEVERE,contentLength.getValue() + " is not a number", e);
					throw new RuntimeException(e);
				}
			}

			final StringBuilder sb = new StringBuilder(length);
			int n;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((n = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, n));
			}
			return sb.toString();
		} catch (IOException e) {
			log.log(Level.SEVERE,"Could not read service response", e);
			throw new IOException("Could not read service response:"
					+ e.getMessage());
		}
	}
	/**
	 * Returns a String representation of the response.
	 * 
	 * @param response
	 *            an HTTP response
	 * @return the content body as String
	 * @throws IOException
	 *             network error
	 * */
	public static InputStream getInputStream(HttpResponse response) throws IOException {
		InputStream is;
		try {
			is = response.getEntity().getContent();
			if (is == null)
				return null;
		} catch (IOException e) {
			log.log(Level.SEVERE,"Could not read service response", e);
			throw new IOException("Could not read service response:"
					+ e.getMessage());
		}
		return is;
	}
}
