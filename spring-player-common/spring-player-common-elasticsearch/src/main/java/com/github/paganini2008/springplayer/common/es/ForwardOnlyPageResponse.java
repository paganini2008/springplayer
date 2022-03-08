package com.github.paganini2008.springplayer.common.es;

import java.util.Iterator;
import java.util.List;

import com.github.paganini2008.devtools.jdbc.Countable;
import com.github.paganini2008.devtools.jdbc.PageRequest;
import com.github.paganini2008.devtools.jdbc.PageResponse;
import com.github.paganini2008.devtools.jdbc.ResultSetSlice;

/**
 * 
 * ForwardOnlyPageResponse
 *
 * @author Fred Feng
 * @version 1.0.0
 */
public class ForwardOnlyPageResponse<T> implements PageResponse<T> {

	private final int pageNumber;
	private final int totalPages;
	private final int totalRecords;
	private final PageRequest pageRequest;
	private final ResultSetSlice<T> resultSetSlice;

	public ForwardOnlyPageResponse(PageRequest pageRequest, ResultSetSlice<T> resultSetSlice) {
		this(pageRequest, resultSetSlice, () -> {
			return resultSetSlice.rowCount();
		});
	}

	public ForwardOnlyPageResponse(PageRequest pageRequest, ResultSetSlice<T> resultSetSlice, Countable countable) {
		this.pageNumber = pageRequest.getPageNumber();
		this.totalRecords = countable.rowCount();
		this.totalPages = (totalRecords + pageRequest.getPageSize() - 1) / pageRequest.getPageSize();
		this.pageRequest = pageRequest;
		this.resultSetSlice = resultSetSlice;
	}

	public boolean isEmpty() {
		return totalRecords == 0;
	}

	public boolean isLastPage() {
		return pageNumber == getTotalPages();
	}

	public boolean isFirstPage() {
		return pageNumber == 1;
	}

	public boolean hasNextPage() {
		return pageNumber < getTotalPages();
	}

	public boolean hasPreviousPage() {
		return pageNumber > 1;
	}

	public Iterator<PageResponse<T>> iterator() {
		return new PageIterator<T>(this);
	}

	/**
	 * 
	 * PageIterator
	 * 
	 * @author Fred Feng
	 *
	 * @since 2.0.1
	 */
	static class PageIterator<T> implements Iterator<PageResponse<T>> {

		private final PageResponse<T> pageResponse;
		private PageResponse<T> current;

		PageIterator(PageResponse<T> pageResponse) {
			this.pageResponse = pageResponse;
		}

		public boolean hasNext() {
			return current == null || current.hasNextPage();
		}

		public PageResponse<T> next() {
			if (current == null) {
				current = pageResponse;
			} else {
				current = current.nextPage();
			}
			return current;
		}
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public int getOffset() {
		return pageRequest.getOffset();
	}

	public int getPageSize() {
		return pageRequest.getPageSize();
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public PageResponse<T> setPage(int pageNumber) {
		return new ForwardOnlyPageResponse<T>(pageRequest.set(pageNumber), resultSetSlice);
	}

	public PageResponse<T> lastPage() {
		throw new UnsupportedOperationException("lastPage");
	}

	public PageResponse<T> firstPage() {
		throw new UnsupportedOperationException("firstPage");
	}

	public PageResponse<T> nextPage() {
		return hasNextPage() ? new ForwardOnlyPageResponse<T>(pageRequest.next(), resultSetSlice) : this;
	}

	public PageResponse<T> previousPage() {
		throw new UnsupportedOperationException("previousPage");
	}

	public List<T> getContent() {
		return resultSetSlice.list(pageRequest.getPageSize(), pageRequest.getOffset());
	}
}
