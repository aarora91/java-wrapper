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
package com.ibm.watson.developer_cloud.dialog.v1.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.ibm.watson.developer_cloud.dialog.v1.DialogService;

/**
 * Conversation used by the {@link DialogService}.
 *
 * @author German Attanasio Ruiz (germanatt@us.ibm.com)
 * 
 */
public class Conversation {

	/** The client id. */
	@SerializedName("client_id")
	private int clientId;

	/** The confidence. */
	private double confidence;

	/** The id. */
	@SerializedName("conversation_id")
	private int id;

	/** The input. */
	private String input;

	/** The response. */
	private List<String> response = new ArrayList<String>();

	/**
	 * Gets the client id.
	 * 
	 * @return The clientId
	 */
	public int getClientId() {
		return clientId;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return The confidence
	 */
	public double getConfidence() {
		return confidence;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the input.
	 * 
	 * @return The input
	 */
	public String getInput() {
		return input;
	}

	/**
	 * Gets the response.
	 * 
	 * @return The response
	 */
	public List<String> getResponse() {
		return response;
	}

	/**
	 * Sets the client id.
	 * 
	 * @param clientId
	 *            The client_id
	 */
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            The confidence
	 */
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * Sets the input.
	 * 
	 * @param input
	 *            The input
	 */
	public void setInput(final String input) {
		this.input = input;
	}

	/**
	 * Sets the response.
	 * 
	 * @param response
	 *            The response
	 */
	public void setResponse(final List<String> response) {
		this.response = response;
	}

	/**
	 * With client id.
	 * 
	 * @param clientId
	 *            the client id
	 * @return the conversation
	 */
	public Conversation withClientId(final int clientId) {
		this.clientId = clientId;
		return this;
	}

	/**
	 * With confidence.
	 * 
	 * @param confidence
	 *            the confidence
	 * @return the conversation
	 */
	public Conversation withConfidence(final double confidence) {
		this.confidence = confidence;
		return this;
	}

	/**
	 * With conversation id.
	 * 
	 * @param id
	 *            the id
	 * @return the conversation
	 */
	public Conversation withId(final int id) {
		this.id = id;
		return this;
	}

	/**
	 * With input.
	 * 
	 * @param input
	 *            the input
	 * @return the conversation
	 */
	public Conversation withInput(final String input) {
		this.input = input;
		return this;
	}

	/**
	 * With response.
	 * 
	 * @param response
	 *            the response
	 * @return the conversation
	 */
	public Conversation withResponse(final List<String> response) {
		this.response = response;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getName() + " "
				+ new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}
}
