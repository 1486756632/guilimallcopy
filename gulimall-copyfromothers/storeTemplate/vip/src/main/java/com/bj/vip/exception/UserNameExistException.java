package com.bj.vip.exception;

/**
 * <p>Title: UserNameExistException</p>
 * Description：
 * date：2020/6/25 19:17
 */
public class UserNameExistException extends RuntimeException {
	public UserNameExistException() {
		super("用户名存在");
	}
}
