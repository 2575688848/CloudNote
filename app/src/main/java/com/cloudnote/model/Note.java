package com.cloudnote.model;
/**

 */
public class Note {
	
	private int id;
	private String title;
	private String content;
	private String createTime;
	private int userId;
	
	public Note(int id, String title, String content, String createTime, int userId) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.createTime = createTime;
		this.userId = userId;
	}
	
	public Note() {
		super();
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return "Note [id=" + id + ", title=" + title + ", content=" + content + ", createTime=" + createTime
				+ ", userId=" + userId + "]";
	}
}
