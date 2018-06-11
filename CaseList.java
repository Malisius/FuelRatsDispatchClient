package com.cmdrsforhire;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class CaseList extends ArrayList<Case> {
	public CaseList() {
		super();
	}
	
	public boolean contains(Case c) {
		for(Case d : this) {
			if(c.getCasenum() == d.getCasenum() && c.getCmdr().equalsIgnoreCase(d.getCmdr())) {
				return true;
			}
		}
		return false;
	}
}
