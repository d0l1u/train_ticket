package com.l9e.transaction.vo;

import java.util.LinkedHashMap;
import java.util.Map;

public class QuestionVo {

	private String question_id;
	private String question_order_id;
	private String question_theme;
	private String question_desc;
	private String question_status;
	private String question_seq;
	private String question_answer;
	private String question_answer_time;
	private String question_solve;
	private String question_solve_time;
	private String question_assigner;
	private String question_pic;
	
	
	public String getQuestion_seq() {
		return question_seq;
	}

	public void setQuestion_seq(String questionSeq) {
		question_seq = questionSeq;
	}

	public String getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(String questionId) {
		question_id = questionId;
	}

	public String getQuestion_order_id() {
		return question_order_id;
	}

	public void setQuestion_order_id(String questionOrderId) {
		question_order_id = questionOrderId;
	}

	public String getQuestion_theme() {
		return question_theme;
	}

	public void setQuestion_theme(String questionTheme) {
		question_theme = questionTheme;
	}

	public String getQuestion_desc() {
		return question_desc;
	}

	public void setQuestion_desc(String questionDesc) {
		question_desc = questionDesc;
	}

	public String getQuestion_answer() {
		return question_answer;
	}

	public void setQuestion_answer(String questionAnswer) {
		question_answer = questionAnswer;
	}

	public String getQuestion_answer_time() {
		return question_answer_time;
	}

	public void setQuestion_answer_time(String questionAnswerTime) {
		question_answer_time = questionAnswerTime;
	}

	public String getQuestion_solve() {
		return question_solve;
	}

	public void setQuestion_solve(String questionSolve) {
		question_solve = questionSolve;
	}

	public String getQuestion_solve_time() {
		return question_solve_time;
	}

	public void setQuestion_solve_time(String questionSolveTime) {
		question_solve_time = questionSolveTime;
	}

	public String getQuestion_assigner() {
		return question_assigner;
	}

	public void setQuestion_assigner(String questionAssigner) {
		question_assigner = questionAssigner;
	}

	public String getQuestion_pic() {
		return question_pic;
	}

	public void setQuestion_pic(String questionPic) {
		question_pic = questionPic;
	}

	public void setQuestion_status(String questionStatus) {
		question_status = questionStatus;
	}

	public static String QUESTION_BEGIN = "11";
	public static String QUESTION_FINISH = "22";
	public static String QUESTION_AGAIN = "33";
	public static String QUESTION_REPLY = "44";
	private static Map<String,String>QUESTION_ORDER = new LinkedHashMap<String,String>();
	
	public static Map<String,String>getQuestion_status(){
		if(QUESTION_ORDER.isEmpty()){
			QUESTION_ORDER.put(QUESTION_BEGIN, "新提交");
			QUESTION_ORDER.put(QUESTION_FINISH, "已解决");
			QUESTION_ORDER.put(QUESTION_AGAIN, "问题重现");
			QUESTION_ORDER.put(QUESTION_REPLY, "已答复");
		}
		return QUESTION_ORDER;
	}
}
