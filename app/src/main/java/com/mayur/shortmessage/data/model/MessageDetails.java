package com.mayur.shortmessage.data.model;

import java.io.Serializable;

public class MessageDetails implements Serializable {
	public static final int MESSAGE_TYPE_ALL    = 0;
	public static final int MESSAGE_TYPE_INBOX  = 1;
	public static final int MESSAGE_TYPE_SENT   = 2;
	public static final int MESSAGE_TYPE_DRAFT  = 3;
	public static final int MESSAGE_TYPE_OUTBOX = 4;
	public static final int MESSAGE_TYPE_FAILED = 5; // for failed outgoing messages
	public static final int MESSAGE_TYPE_QUEUED = 6; // for messages to send later
	public static final int MESSAGE_TYPE_SENDING  = 999; //on process sending, only use this project

	public long threadId = -1;
	public long id = -1;
	public int type;
	public String body;
	public long date;
	public String address;  // number
	public boolean read = false;

	// one of them will be null (the user)
	private Contact sender;
	private Contact receiver;

	public boolean fromMe(){
		if(type == MessageDetails.MESSAGE_TYPE_SENT || type == MessageDetails.MESSAGE_TYPE_SENDING) {
			return true;
		}
		return false;
	}

	public Message copyProperty(){
		Message msg = new Message();
		msg.threadId = this.threadId;
		msg.date = this.date;
		msg.msgCount = 1;
		msg.read = true;
		msg.snippet = this.body;
		return msg;
	}
}


