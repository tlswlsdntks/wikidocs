package kr.wikidocs.config.vo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Result {

	private int rstCd = 0;
	private String rstMsg;
	private Object rstData;

	public Result(Object rstData) {
		this(0, null, rstData);
	}

	public Result(int rstCd, String rstMsg) {
		this(rstCd, rstMsg, null);
	}

	public Result(Object rstData, String rstMsg) {
		this(0, rstMsg, rstData);
	}

	public Result(int rstCd, String rstMsg, Object rstData) {
		this.rstCd = rstCd;
		this.rstMsg = rstMsg;
		this.rstData = rstData;
	}

	public void setData(Object rstData) {
		this.rstCd = 0;
		this.rstMsg = null;
		this.rstData = rstData;
	}

	public void setData(Object rstData, String rstMsg) {
		this.rstCd = 0;
		this.rstMsg = rstMsg;
		this.rstData = rstData;
	}

	public void setError(int rstCd, String rstMsg) {
		this.rstCd = rstCd;
		this.rstMsg = rstMsg;
		this.rstData = null;
	}
}
