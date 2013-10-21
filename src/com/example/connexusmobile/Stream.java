package com.example.connexusmobile;

import java.util.Date;
import java.util.List;
import java.util.LinkedList;
import java.util.HashSet;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import java.util.Queue;
import com.google.common.base.Joiner;
import com.googlecode.objectify.ObjectifyFactory;

@Entity
public class Stream implements Comparable<Stream> {

/*	static {
		 ObjectifyService.register(Stream.class);
	} */
	// id is set by the datastore for us
	@Id
	public Long id;
	@Index public String name;
	@Index public String owner;
//	public String tags;
	public Date createDate;
	public Date viewDate;
	public Date lastAdded;
	public Date lastViewed;
	public String coverImageUrl;
	
	public Long views = 0L; 
	public int numPics = 0;
	@Index public LinkedList<String> subscribers = null;
	@Index public LinkedList<String> pictures= null; 
	@Index public HashSet<String> tags = null;
	@Index public LinkedList<Date> stream_view_dates = null;
//	@Index public Queue<Date> stream_view_dates = null;
  
	// TODO: figure out why this is needed
	@SuppressWarnings("unused")
	private Stream() {
	}
	
	@Override
	public String toString() {
		Joiner joiner = Joiner.on(":");
		return joiner.join(id.toString(), name, createDate.toString(), owner);
//		return joiner.join(id.toString(), name, tags, createDate.toString());
 	}

//	public Stream(String name, String owner, String tags, String coverImageUrl) {
	public Stream(String name, String owner, String coverImageUrl) {
		this.name = name;
//		this.tags = tags;
		this.owner = owner;
		this.coverImageUrl = coverImageUrl;
		this.createDate = new Date();
		this.stream_view_dates = new LinkedList<Date>();
	}

	@Override
	public int compareTo(Stream other) {
		if (views < other.views) {
			return 1;
		} else if (views > other.views) {
			return -1;
		}
		/*
		if (createDate.after(other.createDate)) {
			return 1;
		} else if (createDate.before(other.createDate)) {
			return -1;
		}*/
		return 0;
	}
}
