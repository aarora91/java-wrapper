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
package com.ibm.watson.developer_cloud.speech_to_text.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.service.Request;
import com.ibm.watson.developer_cloud.service.WatsonService;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SessionStatus;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechModel;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechModelSet;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechSession;
import com.ibm.watson.developer_cloud.util.ResponseUtil;

/**
 * The Speech to Text service uses IBM's speech recognition capabilities to
 * convert English speech into text. The transcription of incoming audio is
 * continuously sent back to the client with minimal delay, and it is corrected
 * as more speech is heard.
 *
 * @author German Attanasio Ruiz (germanatt@us.ibm.com)
 * @version v1
 * @see <a
 *      href="http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/speech-to-text.html">
 *      Speech to Text</a>
 */
public class SpeechToText extends WatsonService {

	/** The url. */
	private static String URL = "https://stream.watsonplatform.net/speech-to-text/api";

	/**
	 * Instantiates a new speech to text.
	 */
	public SpeechToText() {
		setEndPoint(URL);
	}

	/**
	 * Get the recognize status.
	 *
	 * @param session the session
	 * @return the recognize status
	 * @see #getRecognizeStatus(String)
	 */
	public SessionStatus getRecognizeStatus(final SpeechSession session) {
		if (session == null)
			throw new IllegalArgumentException("session was not specified");
		return getRecognizeStatus(session.getSessionId());
	}

	/**
	 * Gets the session status. Concurrent recognition tasks during the same
	 * session are not allowed. This method offers a way to check whether the
	 * session can accept another recognition task. The returned state must be
	 * "initialized" to call {@link #recognize(File, String)}.
	 * 
	 * @param sessionId
	 *            the session id
	 * @return the model
	 */
	public SessionStatus getRecognizeStatus(final String sessionId) {
		if (sessionId == null)
			throw new IllegalArgumentException("sessionId was not specified");

		HttpRequestBase request = Request.Get(
				"/v1/sessions/" + sessionId + "/recognize").build();
		try {
			HttpResponse response = execute(request);
			String resultJson = ResponseUtil.getString(response);
			SessionStatus sessionStatus = new Gson().fromJson(resultJson,
					SessionStatus.class);
			return sessionStatus;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the speech model.
	 * 
	 * @param name
	 *            the name
	 * @return the model
	 */
	public SpeechModel getModel(final String name) {
		if (name == null)
			throw new IllegalArgumentException("name was not specified");

		HttpRequestBase request = Request.Get("/v1/models/" + name).build();
		try {
			HttpResponse response = execute(request);
			String resultJson = ResponseUtil.getString(response);
			SpeechModel model = new Gson().fromJson(resultJson,
					SpeechModel.class);
			return model;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create a session to lock an engine to the session. You can use the
	 * session for multiple recognition requests, so that each request is
	 * processed with the same speech-to-text engine. Use the cookie that is
	 * returned from this operation in the Set-Cookie header for each request
	 * that uses this session. The session expires after 15 minutes of
	 * inactivity.
	 *
	 * @param model the model
	 * @return the session id
	 */
	public SpeechSession createSession(final String model) {
		String path = "/v1/sessions";

		if (model != null && !model.isEmpty())
			path += "?model=" + model;

		HttpRequestBase request = Request.Post(path).build();
		try {
			HttpResponse response = execute(request);
			String sessionString = ResponseUtil.getString(response);
			SpeechSession speechSession = getGson().fromJson(sessionString,
					SpeechSession.class);
			speechSession.setCookieSession(response
					.getFirstHeader("set-cookie").getValue());
			return speechSession;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Create a session to lock an engine to the session. You can use the
	 * session for multiple recognition requests, so that each request is
	 * processed with the same speech-to-text engine. Use the cookie that is
	 * returned from this operation in the Set-Cookie header for each request
	 * that uses this session. The session expires after 15 minutes of
	 * inactivity.
	 * 
	 * @return the session id
	 */
	public String createSession() {
		HttpRequestBase request = Request.Post("/v1/sessions").build();
		try {
			HttpResponse response = execute(request);
			JsonObject jsonObject = ResponseUtil.getJsonObject(response);
			if (response.getStatusLine().getStatusCode() != 201) {
				String error = jsonObject.get("error").getAsString();
				throw new RuntimeException("Cound't create a session:" + error);
			}
			String sessionId = jsonObject.get("session_id").getAsString();
			return sessionId;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Delete a session.
	 * 
	 * @param session
	 *            the speech session
	 */
	public void deleteSession(final SpeechSession session) {
		if (session == null)
			throw new IllegalArgumentException("session was not specified");
		deleteSession(session.getSessionId());
	}

	/**
	 * Delete a session.
	 * 
	 * @param sessionId
	 *            the session id
	 */
	public void deleteSession(final String sessionId) {
		if (sessionId == null)
			throw new IllegalArgumentException("sessionId was not specified");

		HttpRequestBase request = Request.Delete("/v1/sessions/" + sessionId)
				.build();
		HttpResponse response = execute(request);
		if (response.getStatusLine().getStatusCode() != 204)
			throw new RuntimeException("Cound't delete session");
	}

	/**
	 * Gets the models.
	 * 
	 * @return the models
	 */
	public List<SpeechModel> getModels() {
		HttpRequestBase request = Request.Get("/v1/models").build();
		try {
			HttpResponse response = execute(request);
			String speechModelsJson = ResponseUtil.getString(response);
			SpeechModelSet speechModels = new Gson().fromJson(speechModelsJson,
					SpeechModelSet.class);
			return speechModels.getModels();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Recognize.
	 * 
	 * @param audio
	 *            the audio file
	 * @param contentType
	 *            the content type
	 * @return the speech results
	 */
	public SpeechResults recognize(final File audio, final String contentType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("audio", audio);
		params.put("content_type", contentType);
		return recognize(params);
	}

	/**
	 * Recognize.
	 * 
	 * @param params
	 *            the parameters to use for the recognition. for example:
	 *            "word_confidence", "continuous", "max_alternatives",
	 *            "timestamps", "inactivity_timeout", "model", "session_id",
	 *            content_type
	 * @return the speech results
	 */
	public SpeechResults recognize(Map<String, Object> params) {

		File audio = (File) params.get("audio");
		if (audio == null || !audio.exists() || !audio.isFile())
			throw new IllegalArgumentException(
					"audio is not a valid audio file");

		String contentType = (String) params.get("content_type");
		if (contentType == null)
			throw new IllegalArgumentException("contentType was not specified");

		// Build the recognize url
		StringBuilder urlBuider = new StringBuilder();
		urlBuider.append("/v1");
		urlBuider.append(params.containsKey("session_id") ? "/sessions/"
				+ params.get("session_id") : "");
		urlBuider.append("/recognize");

		Request request = Request.Post(urlBuider.toString());
		request.withHeader("Content-Type", contentType);

		String[] queryParameters = new String[] { "word_confidence",
				"continuous", "max_alternatives", "timestamps",
				"inactivity_timeout", "model" };

		for (String param : queryParameters) {
			if (params.containsKey(param))
				request.withQuery(param, params.get(param));
		}

		InputStreamEntity reqEntity = null;
		try {
			reqEntity = new InputStreamEntity(new FileInputStream(audio), -1);

			reqEntity.setContentType(contentType);
			reqEntity.setChunked(true);
			request.withEntity(reqEntity);

			HttpResponse response = execute(request.build());
			String speechResultJson;

			speechResultJson = ResponseUtil.getString(response);
			SpeechResults speechResults = new Gson().fromJson(speechResultJson,
					SpeechResults.class);
			return speechResults;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the model.
	 * 
	 * @param watsonModel
	 *            the watson model
	 * @return the model
	 */
	public SpeechModel getModel(final SpeechModel watsonModel) {
		return getModel(watsonModel.getName());
	}
}
