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
package com.ibm.watson.developer_cloud.dialog.v1;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibm.watson.developer_cloud.dialog.v1.model.Conversation;
import com.ibm.watson.developer_cloud.dialog.v1.model.Dialog;
import com.ibm.watson.developer_cloud.dialog.v1.model.DialogContent;
import com.ibm.watson.developer_cloud.dialog.v1.model.NameValue;
import com.ibm.watson.developer_cloud.dialog.v1.model.Session;
import com.ibm.watson.developer_cloud.service.Request;
import com.ibm.watson.developer_cloud.service.WatsonService;
import com.ibm.watson.developer_cloud.util.ResponseUtil;

/**
 * The IBM Watson Dialog service Dialogs enhances application by providing
 * chitchat for topics outside of a corpus and for giving context to a user's
 * questions. You can create various virtual agent (VA) applications. Users can
 * have natural, free-flowing, and human-like conversations with VAs that answer
 * questions, show personality, decide, provide guidance, and even perform
 * tasks.
 *
 * @author German Attanasio Ruiz (germanatt@us.ibm.com)
 * @version v1
 * @see <a
 *      href="http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/dialog.html">
 *      Dialog</a>
 */
public class DialogService extends WatsonService {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(DialogService.class
			.getName());

	/** The url. */
	private static String URL = "https://gateway.watsonplatform.net/dialog-experimental/api";

	/** The list dialog content type. */
	private Type listDialogContentType = new TypeToken<List<DialogContent>>() {
	}.getType();

	/** The list dialog type. */
	private Type listDialogType = new TypeToken<List<Dialog>>() {
	}.getType();

	/** The list session type. */
	private Type listSessionType = new TypeToken<List<Session>>() {
	}.getType();

	/** The sdf date. */
	private SimpleDateFormat sdfDate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/** The list name value type. */
	private Type listNameValueType = new TypeToken<List<NameValue>>() {
	}.getType();

	/**
	 * Instantiates a new Dialog service.
	 */
	public DialogService() {
		setEndPoint(URL);
	}

	/**
	 * Starts or continue conversations.
	 * 
	 * @param dialogId
	 *            the dialog id
	 * @param conversation
	 *            the conversation
	 * @param input
	 *            the user input message
	 * @return the conversation
	 */
	public Conversation converse(String dialogId, Conversation conversation,
			String input) {
		return converse(dialogId, conversation.getClientId(),
				conversation.getId(), input);
	}

	/**
	 * Starts or continue conversations.
	 * 
	 * @param dialogId
	 *            the dialog id
	 * @param clientId
	 *            A client Id number generated by the Dialog service. If not
	 *            specified a new client Id will be issued.
	 * @param conversationId
	 *            the conversation id. If not specified, a new conversation will
	 *            be started.
	 * @param input
	 *            the user input message
	 * @return the conversation with the response
	 */
	public Conversation converse(final String dialogId, final Integer clientId,
			final Integer conversationId, final String input) {
		if (dialogId == null || dialogId.isEmpty())
			throw new IllegalArgumentException(
					"dialogId can not be null or empty");

		if (conversationId == null) {
			log.info("Creating a new conversation with for dialog: " + dialogId);
		}

		if (clientId == null) {
			log.info("Creating a new client id with for dialog: " + dialogId);
		}

		String path = String.format("/v1/dialogs/%s/conversation", dialogId);

		HttpRequestBase request = Request
				.Post(path)
				.withForm("conversation_id", conversationId, "client_id",
						clientId, "input", input).build();

		try {
			HttpResponse response = execute(request);
			String conversationAsJson = ResponseUtil.getString(response);
			Conversation conversation = getGson().fromJson(conversationAsJson,
					Conversation.class);
			return conversation;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Starts or continue conversations.
	 * 
	 * @param dialogId
	 *            the dialog id
	 * @return a new conversation
	 */
	public Conversation createConversation(final String dialogId) {
		return converse(dialogId, null, null, null);
	}

	/**
	 * Creates a dialog.
	 *
	 * @param name            The dialog name
	 * @param dialogFile            The dialog file created by using the Dialog service Applet.
	 * @return the created dialog
	 * @see Dialog
	 */
	public Dialog createDialog(final String name, final File dialogFile) {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("name can not be null or empty");

		if (dialogFile == null || !dialogFile.exists())
			throw new IllegalArgumentException(
					"dialogFile can not be null or empty");

		try {
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("file", new FileBody(dialogFile));
			reqEntity.addPart("name", new StringBody(name));

			HttpRequestBase request = Request.Post("/v1/dialogs")
					.withEntity(reqEntity).build();

			HttpResponse response = execute(request);
			String DialogAsJson = ResponseUtil.getString(response);
			Dialog dialog = getGson().fromJson(DialogAsJson, Dialog.class);
			return dialog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Deletes a dialog.
	 *
	 * @param dialogId            the dialog id
	 * @see DialogService
	 */
	public void deleteDialog(final String dialogId) {
		if (dialogId == null || dialogId.isEmpty())
			throw new IllegalArgumentException(
					"dialogId can not be null or empty");

		HttpRequestBase request = Request.Delete("/v1/dialogs/" + dialogId)
				.build();
		execute(request);
	}

	/**
	 * Get content for nodes.
	 * 
	 * @param dialogId
	 *            the dialog id
	 * @return the {@link DialogContent} for nodes
	 */
	public List<DialogContent> getContent(final String dialogId) {
		if (dialogId == null || dialogId.isEmpty())
			throw new IllegalArgumentException(
					"dialogId can not be null or empty");

		HttpRequestBase request = Request.Get(
				"/v1/dialogs/" + dialogId + "/content").build();

		try {
			HttpResponse response = execute(request);
			JsonObject jsonObject = ResponseUtil.getJsonObject(response);
			List<DialogContent> content = new Gson().fromJson(
					jsonObject.get("items"), listDialogContentType);
			return content;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retrieves the list of Dialogs for the user.
	 *
	 * @return the {@link Dialog} list
	 */
	public List<Dialog> getDialogs() {
		HttpRequestBase request = Request.Get("/v1/dialogs").build();

		try {
			HttpResponse response = execute(request);
			JsonObject jsonObject = ResponseUtil.getJsonObject(response);
			List<Dialog> dialogs = new Gson().fromJson(
					jsonObject.get("dialogs"), listDialogType);
			return dialogs;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns a list of name-value pars associated with a client id.
	 *
	 * @param dialogId            The dialog identifier
	 * @param clientId the client id
	 * @return the created dialog
	 * @see Dialog
	 */
	public List<NameValue> getProfile(String dialogId, Integer clientId) {
		if (dialogId == null || dialogId.isEmpty())
			throw new IllegalArgumentException(
					"dialogId can not be null or empty");

		if (clientId == null)
			throw new IllegalArgumentException(
					"clientId can not be null");

		HttpRequestBase request = Request.Get(
				"/v1/dialogs/" + dialogId + "/profile")
				.withQuery("client_id",clientId)
				.build();
		try {
			HttpResponse response = execute(request);
			JsonObject jsonObject = ResponseUtil.getJsonObject(response);
			List<NameValue> content = new Gson().fromJson(
					jsonObject.get("name_values"), listNameValueType);
			return content;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns chat session data dump for a given date rage.
	 * 
	 * @param dialogId
	 *            the dialog id
	 * @param from
	 *            Date from where to start the data dump
	 * @param to
	 *            Date to where to end the data dump
	 * 
	 * @return The session
	 */
	public List<Session> getSession(final String dialogId, final Date from,
			final Date to) {
		return getSession(dialogId, from, to, null, null);
	}

	/**
	 * Returns chat session data dump for a given date rage.
	 * 
	 * @param dialogId
	 *            the dialog id
	 * @param from
	 *            Date from where to start the data dump
	 * @param to
	 *            Date to where to end the data dump
	 * @param offset
	 *            the offset from where to return conversations
	 * @param limit
	 *            the number of conversations to return
	 * @return the classification of a phrase with a given Dialog
	 */
	public List<Session> getSession(final String dialogId, final Date from,
			final Date to, final Integer offset, final Integer limit) {
		if (dialogId == null || dialogId.isEmpty())
			throw new IllegalArgumentException(
					"dialogId can not be null or empty");

		if (from == null)
			throw new IllegalArgumentException("from can not be null");

		if (to == null)
			throw new IllegalArgumentException("to can not be null");

		if (from.after(to))
			throw new IllegalArgumentException("'from' is greater than 'to'");

		String fromString = sdfDate.format(from);
		String toString = sdfDate.format(to);

		String path = String.format("/v1/dialogs/%s/conversation", dialogId);

		Request requestBuilder = Request.Get(path).withQuery("date_from",
				fromString, "date_to", toString);

		if (offset != null)
			requestBuilder.withQuery("offset", offset);
		if (limit != null)
			requestBuilder.withQuery("limit", limit);

		HttpRequestBase request = requestBuilder.build();

		try {
			HttpResponse response = execute(request);
			JsonObject jsonObject = ResponseUtil.getJsonObject(response);
			List<Session> sessions = new Gson().fromJson(
					jsonObject.get("conversations"), listSessionType);
			return sessions;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Dialog [getEndPoint()=");
		builder.append(getEndPoint());
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Updates a dialog 
	 *
	 * @param dialogId the dialog id
	 * @param dialogFile the dialog file
	 */
	public void updateContent(final String dialogId, final File dialogFile) {
		if (dialogId == null || dialogId.isEmpty())
			throw new IllegalArgumentException(
					"dialogId can not be null or empty");

		if (dialogFile == null || !dialogFile.exists())
			throw new IllegalArgumentException(
					"dialogFile can not be null or empty");

		try {
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("file", new FileBody(dialogFile));
			reqEntity.addPart("dialog_id", new StringBody(dialogId));

			HttpRequestBase request = Request
					.Put("/v1/dialogs/" + dialogId + "/content")
					.withEntity(reqEntity).build();
			execute(request);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Updates a dialog.
	 *
	 * @param dialogId            The dialog identifier
	 * @param dialogFile            The dialog file
	 * @return the created dialog
	 * @see Dialog
	 */
	public Dialog updateDialog(final String dialogId, final File dialogFile) {
		if (dialogId == null || dialogId.isEmpty())
			throw new IllegalArgumentException(
					"dialogId can not be null or empty");

		if (dialogFile == null || !dialogFile.exists())
			throw new IllegalArgumentException(
					"dialogFile can not be null or empty");

		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("file", new FileBody(dialogFile));

		HttpRequestBase request = Request.Put("/v1/dialogs/" + dialogId)
				.withEntity(reqEntity).build();
		try {
			HttpResponse response = execute(request);
			String dialogAsJson = ResponseUtil.getString(response);
			Dialog dialog = getGson().fromJson(dialogAsJson, Dialog.class);
			return dialog;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Updates dialog profile
	 * 
	 * @param dialogId
	 *            The dialog identifier
	 * @param clientId
	 *            the client id
	 * @param nameValues
	 *            the name values to update
	 * @see Dialog
	 */
	public void updateProfile(final String dialogId, final Integer clientId, final List<NameValue> nameValues) {
		if (dialogId == null || dialogId.isEmpty())
			throw new IllegalArgumentException(
					"dialogId can not be null or empty");

		if (clientId == null)
			throw new IllegalArgumentException(
					"clientId can not be null");

		if (nameValues == null || nameValues.isEmpty())
			throw new IllegalArgumentException(
					"nameValues can not be null or empty");

		JsonObject contentJson = new JsonObject();

		contentJson.addProperty("client_id", clientId);
		contentJson.add("name_values", getGson().toJsonTree(nameValues));

		HttpRequestBase request = Request
				.Put("/v1/dialogs/" + dialogId + "/profile")
				.withContent(contentJson).build();
		execute(request);
	}

}
