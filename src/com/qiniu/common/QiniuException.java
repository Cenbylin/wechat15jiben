package com.qiniu.common;

import java.io.IOException;

import com.qiniu.http.Response;

public class QiniuException extends IOException {

	private static final long serialVersionUID = -3941706552813708193L;
	public final Response response;

	public QiniuException(Response response) {
		this.response = response;
	}

	public QiniuException(Exception e) {
		super(e);
		this.response = null;
	}

	public String url() {
		return response.url();
	}

	public int code() {
		return response == null ? -1 : response.statusCode;
	}
}
