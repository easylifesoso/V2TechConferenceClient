package com.v2tech.logic;

import java.util.HashMap;
import java.util.Map;

import com.v2tech.util.V2Log;

import android.os.Handler;
import android.os.Message;

public abstract class AbstractHandler extends Handler {

	protected static final int REQUEST_TIME_OUT = 0;

	protected static final int MONITOR_TYPE_CONFERENCE = 0X01000000;

	protected static final int MONITOR_TYPE_DEVICE = 0X02000000;

	protected static final int MONITOR_TYPE_CONTACT = 0X03000000;

	private Map<Integer, Meta> metaHolder = new HashMap<Integer, Meta>();

	protected Message initTimeoutMessage(int mointorMessageID, Object obj,
			long timeOutSec, Message caller) {
		Message msg = Message.obtain(this, REQUEST_TIME_OUT, mointorMessageID,
				0, obj);
		metaHolder.put(Integer.valueOf(mointorMessageID), new Meta(
				mointorMessageID, caller, msg));
		this.sendMessageDelayed(msg, timeOutSec * 1000);
		return msg;
	}
	
	
	protected Message removeTimeoutMessage(int mointorMessageID) {
		Meta meta = metaHolder.get(Integer.valueOf(mointorMessageID));
		metaHolder.remove(Integer.valueOf(mointorMessageID));
		if (meta != null) {
			this.removeMessages(REQUEST_TIME_OUT, meta.timeoutMessage);
			return meta.caller;
		} else {
			return null;
		}
	}

	class Meta {
		int mointorMessageID;
		Message caller;
		Message timeoutMessage;

		public Meta(int mointorMessageID, Message caller, Message timeoutMessage) {
			super();
			this.mointorMessageID = mointorMessageID;
			this.caller = caller;
			this.timeoutMessage = timeoutMessage;
		}

	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case REQUEST_TIME_OUT:
			Meta meta = metaHolder.get(Integer.valueOf(msg.arg1));
			if (meta != null && meta.caller != null) {
				meta.caller.obj = new AsynResult(AsynResult.AsynState.TIME_OUT, null);
				meta.caller.sendToTarget();
			} else {
				V2Log.w("Doesn't find time message in the queue :"+ msg.arg1);
			}
			break;
		}
	}

}