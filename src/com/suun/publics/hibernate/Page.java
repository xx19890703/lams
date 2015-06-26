package com.suun.publics.hibernate;

import java.util.List;

import com.suun.publics.hibernate.Condition;


/**
 * 封装分页和排序参数及分页查询结果.
 * 
 * @param <T> Page中的记录类型
 */
public class Page<T> {

	private long pageNo = 1;

	private int pageSize = -1;

	private long totalCount = -1;
	//当前页记录所在的位置
	private long curPagePos = -1;

	private boolean autoCount = false;
	
	private Condition condition=null;
	
	private T PosionRes = null;
	
	private List<T> result = null;

	public Page() {
		// TODO Auto-generated constructor stub
	}

	public Page(int pageSize, boolean autoCount) {
		this.autoCount = autoCount;
		this.pageSize = pageSize;
	}

	/**
	 * 每页的记录数量，无默认值.
	 */
	public int getPageSize() {
		return pageSize;
	}

	public boolean isPageSizeSetted() {
		return pageSize > -1;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 当前页的页号,序号从1开始.
	 */
	public long getPageNo() {
		return pageNo;
	}

	public void setPageNo(long rTotalpage) {
		this.pageNo = rTotalpage;
	}

	/**
	 * 第一条记录在结果集中的位置,序号从0开始.
	 */
	public long getFirst() {
		if (pageNo < 1 || pageSize < 1)
			return -1;
		else
			return ((pageNo - 1) * pageSize);
	}

	public boolean isFirstSetted() {
		return (pageNo > 0 && pageSize > 0);
	}


	/**
	 * 是否自动获取总页数,默认为false,query by HQL时本属性无效.
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}

	/**
	 * 页内的数据列表.
	 */
	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	/**
	 * 总记录数.
	 */
	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long l) {
		this.totalCount = l;
	}

	/**
	 * 计算总页数.
	 */
	public long getTotalPages() {
		if (totalCount == -1)
			return -1;

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 是否还有下一页.
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * 返回下页的页号,序号从1开始.
	 */
	public long getNextPage() {
		if (isHasNext())
			return pageNo + 1;
		else
			return pageNo;
	}

	/**
	 * 是否还有上一页. 
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 返回上页的页号,序号从1开始.
	 */
	public long getPrePage() {
		if (isHasPre())
			return pageNo - 1;
		else
			return pageNo;
	}

	public void setPosionRes(T posionRes) {
		PosionRes = posionRes;
	}

	public T getPosionRes() {
		return PosionRes;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}

	public long getCurPagePos() {
		return curPagePos;
	}

	public void setCurPagePos(long curPagePos) {
		this.curPagePos = curPagePos;
	}
}