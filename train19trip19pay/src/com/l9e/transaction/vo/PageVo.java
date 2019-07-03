package com.l9e.transaction.vo;

/**
 * 
 * 封装分页参数的对象
 * 
 */
public class PageVo {

	//总页数和总记录数是动态计算出来的
	protected int pageIndex;//当前页号

	protected int everyPageRecordCount;//每页记录数

	protected int totalPageCount;//总页数
	
	protected int totalRecordCount;//总记录数

	public int getTotalRecordCount() {
		return totalRecordCount;
	}

	/**
	 * 
	 * @param pageIndex
	 * @param everyPageRecordCount
	 * @param totalRecordCount
	 */
	public PageVo(int pageIndex, int everyPageRecordCount, int totalRecordCount) {
		this.pageIndex = pageIndex;
		this.everyPageRecordCount = everyPageRecordCount;
		this.totalRecordCount=totalRecordCount;

		if (this.pageIndex < 1)
			this.pageIndex = 1;
		if (this.everyPageRecordCount < 1)
			this.everyPageRecordCount = 1;
		this.totalPageCount = totalRecordCount / this.everyPageRecordCount
			+ (totalRecordCount % this.everyPageRecordCount == 0 ? 0 : 1);
	}

	/**
	 * <p>Desc: </p>
	 * @return
	 * boolean
	*/
	public boolean isQueryResultWouldEmpty() {
		return this.pageIndex > this.totalPageCount;
	}

	/**
	 * <p>Desc: </p>
	 * @return
	 * int
	*/
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * <p>Desc: </p>
	 * @param pageIndex
	 * void
	*/
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	//分页函数的setFirstResult的入参,从0开始计数
	public int getFirstResultIndex() {
		return (this.pageIndex - 1) * this.everyPageRecordCount;
	}

	public boolean isHasPrevPage() {
		return this.pageIndex > 1;
	}

	public boolean isHasNextPage() {
		return this.pageIndex < this.totalPageCount;
	}

	public int getEveryPageRecordCount() {
		return everyPageRecordCount;
	}

	public void setEveryPageRecordCount(int everyPageRecordCount) {
		this.everyPageRecordCount = everyPageRecordCount;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	
	public int getBeginPage(){
		return this.getFirstResultIndex()+1;
	}

	public int getEndPage(){
		return this.getFirstResultIndex() + this.getEveryPageRecordCount();
	}
}
