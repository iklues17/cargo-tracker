package com.sds.fsf.cmm.web.rest.util;

import org.springframework.data.domain.Pageable;

public class MyBatisPage<T> {
	
	public MyBatisPage(Pageable page, T prm) {
		super();
		this.page = page;
		this.prm = prm;
	}

	private Pageable page;
	
	private T prm;

	
	
	public Pageable getPage() {
		return page;
	}

	public void setPage(Pageable page) {
		this.page = page;
	}

	public T getPrm() {
		return prm;
	}

	public void setPrm(T prm) {
		this.prm = prm;
	}
	
	
}
