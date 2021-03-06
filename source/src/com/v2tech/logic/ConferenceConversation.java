package com.v2tech.logic;

public class ConferenceConversation extends Conversation {

	private Group g;

	public ConferenceConversation(Group g) {
		super();
		this.g = g;
		if (g != null) {
			super.mExtId = g.getmGId();
			super.mType = TYPE_CONFERNECE;
			super.mNotiFlag = NONE;
		}
	}

	@Override
	public String getName() {
		if (g != null) {
			return g.getName();
		}
		return super.getName();
	}

	@Override
	public String getMsg() {
		if (g != null) {
			User u = g.getOwnerUser();
			return u == null ? g.getOwner() + "" : u.getName();
		}
		return super.getMsg();
	}

	@Override
	public String getDate() {
		if (g != null) {
			return g.getCreateDate();
		}
		return super.getDate();
	}

}
