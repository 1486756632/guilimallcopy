package com.bj.dto.mq;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>Title: StockLockedTo</p>
 * Description：
 * date：2020/7/3 20:37
 */
@Data
public class StockLockedTo implements Serializable {

	/**
	 * 库存工作单id
	 */
	private Long id;

	/**
	 * 工作详情id
	 */
	private StockDetailTo detailTo;
}
