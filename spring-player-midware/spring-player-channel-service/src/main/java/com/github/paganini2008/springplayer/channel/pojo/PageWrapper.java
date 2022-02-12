package com.github.paganini2008.springplayer.channel.pojo;

import java.util.List;

import lombok.Data;

/**
 * 
 * PageWrapper
 *
 * @author Fred Feng
 * @version 1.0.0
 */
@Data
public class PageWrapper<T> {

	private List<T> content;
	private int page;;
	private int size;
	private int totalPages;
	private int totalRecords;

}
