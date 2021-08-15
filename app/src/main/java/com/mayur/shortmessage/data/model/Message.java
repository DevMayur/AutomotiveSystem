package com.mayur.shortmessage.data.model;

import java.io.Serializable;

public class Message implements Serializable{
	public long threadId = -1;
	public long date = -1;
	public long msgCount = -1;
	public boolean read = false;
	public Contact contact = null;
	public String snippet = null;
	
	public Message() {
		threadId = -1;
		date = -1;
		msgCount = -1;
		read = false;
		contact = null;
	}
	
//	@Override
//	public boolean equals(Object o) {
//		if(o instanceof Message) {
//			Message c = (Message) o;
//			return threadId == c.threadId;
//		}
//		return super.equals(o);
//	}
//
//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Message[");
//		sb.append("name=" + contact.name);
//		sb.append(" thread_id=" + threadId);
//		sb.append(" msg_count=" + msgCount);
//		sb.append(" number=" + contact.number);
//		sb.append("]");
//
//		return sb.toString();
//	}
}