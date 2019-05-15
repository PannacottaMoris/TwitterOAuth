package com.example.demo.exception;

public class ExceptionCommon extends RuntimeException {
	private String comment;

	public ExceptionCommon(String comment) {
		this.setComment(comment);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
