package com.ratemytechnion;

import java.util.HashSet;

import android.text.format.Time;

public class CourseComment {
	private Long id;
	private Long courseID;
	private Long studentID;
	private String comment;
	private Time datetime;
	private int likes;
	private HashSet<String> studentsWhoLikedThisComment ;

	public CourseComment(Long courseID1, Long studentID1, String comment1,
			Time datetime1, int likes1) {
		courseID = courseID1;
		studentID = studentID1;
		comment = comment1;
		datetime = datetime1;
		likes = likes1;
		studentsWhoLikedThisComment = new HashSet<String>();
	}

	CourseComment() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id1) {
		id = id1;
	}

	public Long getCourseID() {
		return courseID;
	}

	public void setCourseNumber(Long courseID1) {
		courseID = courseID1;
	}

	public Long getStudentID() {
		return studentID;
	}

	public void setStudentID(Long studentID1) {
		studentID = studentID1;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment1) {
		comment = comment1;
	}

	public Time getDatetime() {
		return datetime;
	}

	public void setDatetime(Time datetime1) {
		datetime = datetime1;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes1) {
		likes = likes1;
	}

	public void incrementLikes() {
		likes++;
	}

	/**
	 * @return the studentsWhoLikedThisComment
	 */
	 public HashSet<String> getStudentsWhoLikedThisComment() {
		 if (studentsWhoLikedThisComment == null) {
			 return new HashSet<String>();
		 }
		 return studentsWhoLikedThisComment;
	 }

	 /**
	  * @param studentsWhoLikedThisComment the studentsWhoLikedThisComment to set
	  */
	 public void addStudentsWhoLikedThisComment(
			 String studentWhoLikedThisComment) {
		 if (studentsWhoLikedThisComment == null) {
			 studentsWhoLikedThisComment = new HashSet<String>();
		 }
		 this.studentsWhoLikedThisComment.add(studentWhoLikedThisComment);
	 }
	 
		/**
		 * @param studentsWhoLikedThisComment the studentsWhoLikedThisComment to set
		 */
		public void setStudentsWhoLikedThisComment(
				HashSet<String> studentsWhoLikedThisComment) {
			this.studentsWhoLikedThisComment = studentsWhoLikedThisComment;
		}
}