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

package com.ibm.watson.developer_cloud.question_and_answer.v1.model;

import com.google.gson.GsonBuilder;

/**
 * The Class Synonym.
 */
public class Synonym {

	/** The is chosen. */
	private boolean isChosen;
	
	/** The value. */
	private String value;
	
	/** The weight. */
	private int weight;

	/**
	 * Gets the value.
	 * 
	 * 
	 * @return The value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Gets the weight.
	 * 
	 * 
	 * @return The weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Checks if is checks if is chosen.
	 * 
	 * 
	 * @return The isChosen
	 */
	public boolean isIsChosen() {
		return isChosen;
	}

	/**
	 * Sets the checks if is chosen.
	 * 
	 * @param isChosen
	 *            The isChosen
	 */
	public void setIsChosen(boolean isChosen) {
		this.isChosen = isChosen;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            The value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Sets the weight.
	 * 
	 * @param weight
	 *            The weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
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
