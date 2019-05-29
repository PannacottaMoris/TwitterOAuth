package com.example.demo.model;

import java.io.Serializable;

public class SearchQueryModel implements Serializable {
	private int id;
	private String name;
	private long maxPrice;
	private long minPrice;
	private String description;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public long getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(long minPrice) {
		this.minPrice = minPrice;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public long getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(long maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	//クエリパラメータが全て存在しない場合は不適切とする
	public boolean isValidQuery() {
		if (this.name == ""
				&& this.description == ""
				&& this.maxPrice == 0
				&& this.minPrice == 0) {
			return false;
		}
		return true;
	}
}
