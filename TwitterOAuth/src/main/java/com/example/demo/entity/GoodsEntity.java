package com.example.demo.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "goods")
public class GoodsEntity {
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "price")
	private long price;
	@Column(name = "description")
	private String description;

	//禁止文字確認
	//商品名は１単語（半角英文字）のみ許可
	public boolean isValidNameRequest () {
		if (this.name.matches("^[a-zA-Z]*$")){
			return true;
		}
		return false;
	}
	//説明は半角英文字にカンマとピリオド、半角SPのみ許可
	public boolean isValidDescRequest () {
		if (this.description.matches("^[a-zA-Z,\\. ]*$")){
			return true;
		}
		return false;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
