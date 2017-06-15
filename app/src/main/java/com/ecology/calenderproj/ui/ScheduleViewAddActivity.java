package com.ecology.calenderproj.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ecology.calenderproj.R;
import com.ecology.calenderproj.base.BorderTextView;
import com.ecology.calenderproj.bean.ScheduleDAO;
import com.ecology.calenderproj.bean.ScheduleDateTag;
import com.ecology.calenderproj.calender.LunarCalendar;
import com.ecology.calenderproj.constants.CalendarConstant;
import com.ecology.calenderproj.util.AlarmHelper;
import com.ecology.calenderproj.util.ObjectPool;
import com.ecology.calenderproj.vo.ScheduleVO;


public class ScheduleViewAddActivity extends Activity {

	private LunarCalendar lc = null;
	private ScheduleDAO dao = null;
	private BorderTextView scheduleType,dateText,scheduleTop = null;
	private EditText scheduleText = null;
	private Button scheduleSave,scheduleQuit = null;
	private static int hour = -1;
	private static int minute = -1;
	private static ArrayList<String> scheduleDate = null;
	private ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
	private String scheduleYear = "";
	private String scheduleMonth = "";
	private String scheduleDay = "";
	private String week = "";
	private ScheduleVO scheduleVO;
	private String tempMonth;
	private String tempDay;

	private String[] sch_type = CalendarConstant.sch_type;
	private String[] remind = CalendarConstant.remind;
	private int sch_typeID = 0;
	private int remindID = 0;
	private int mSelectedItem=0;
	
	private static String schText = "";
    int schTypeID = 0;
	public ScheduleViewAddActivity() {
		lc = new LunarCalendar();
		dao = new ScheduleDAO(this);
	}
	private Calendar mCalendar = Calendar.getInstance();
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.scheduleadd);
		ObjectPool.mAlarmHelper = new AlarmHelper(this);
		scheduleType = (BorderTextView) findViewById(R.id.scheduleType);
		scheduleSave = (Button) findViewById(R.id.save);
		scheduleQuit=(Button)this.findViewById(R.id.btn_quit);
		scheduleType.setBackgroundColor(Color.WHITE);
		scheduleType.setText(sch_type[0]);
		
		dateText = (BorderTextView) findViewById(R.id.scheduleDate);
		dateText.setBackgroundColor(Color.WHITE);
		scheduleText = (EditText) findViewById(R.id.scheduleText);
		scheduleText.setBackgroundColor(Color.WHITE);
		if(schText != null){
			scheduleText.setText(schText);
			schText = "";
		}

		Date date = new Date();
		if(hour == -1 && minute == -1){
			hour = date.getHours();
			minute = date.getMinutes();
		}
		dateText.setText(getScheduleDate());
		scheduleType.setOnClickListener(new OnClickListener() {
			  
			
			public void onClick(View v) {

				schText = scheduleText.getText().toString();
				AlertDialog mDialog = new AlertDialog.Builder(ScheduleViewAddActivity.this)  
				.setTitle("Type")
                   .setIcon(android.R.drawable.ic_dialog_alert)  
                    .setSingleChoiceItems(sch_type, 0,  
                            new DialogInterface.OnClickListener() {  

                               public void onClick(DialogInterface dialog,  
                                       int which) {  
                            	   mSelectedItem = which;
                            	   scheduleType.setText(sch_type[mSelectedItem]);
                              } 
                          })
				
				
				   .setPositiveButton("Oui",
                           new DialogInterface.OnClickListener() {  
                               public void onClick(DialogInterface dialog,  
                                       int which) {  
                               	
                               }  
                           })   .create(); 

				
				mDialog.show();
			}
				
			});

		dateText.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {

				new TimePickerDialog(ScheduleViewAddActivity.this, new OnTimeSetListener() {
					
					
					public void onTimeSet(TimePicker view, int hourOfDay, int min) {

						hour = hourOfDay;
						minute = min;
						dateText.setText(getScheduleDate());
					}
				}, hour, minute, true).show();
			}
		});
		
		
		scheduleQuit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				ScheduleViewAddActivity.this.finish();
				
			}
		});
		
		scheduleSave.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				if(TextUtils.isEmpty(scheduleText.getText().toString())){
					new AlertDialog.Builder(ScheduleViewAddActivity.this).setTitle("Entrer quelques choses").setMessage("Il ne faut pas etre vide").setPositiveButton("Oui", null).show();
				}else{
				
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-d H:m:s");
					String start=Integer.parseInt(scheduleYear)+"-"+Integer.parseInt(tempMonth)+"-"+Integer.parseInt(tempDay)+" "+hour+":"+minute+":"+"0";
					long timeStart = 0;
					try {
						timeStart = sdf.parse(start).getTime();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i("_____2",String.format("%tF %<tT",timeStart ));
					String showDate = handleInfo(Integer.parseInt(scheduleYear), Integer.parseInt(tempMonth), Integer.parseInt(tempDay), hour, minute, week, remindID);
	                ScheduleVO schedulevo = new ScheduleVO();
	                schedulevo.setScheduleTypeID(mSelectedItem);
	                schedulevo.setRemindID(remindID);
	                schedulevo.setScheduleDate(showDate);
	                schedulevo.setTime(hour+":"+minute);
	                schedulevo.setScheduleContent(scheduleText.getText().toString());
	                schedulevo.setAlartime(timeStart);
					int scheduleID = dao.save(schedulevo);
					String [] scheduleIDs = new String[]{String.valueOf(scheduleID)};
					finish();
					setScheduleDateTag(remindID, scheduleYear, tempMonth, tempDay, scheduleID);
					Toast.makeText(ScheduleViewAddActivity.this, "Save", 0).show();
					setAlart(ScheduleViewAddActivity.this);
				}
			}
		});
		
	}


	public void setScheduleDateTag(int remindID, String year, String month, String day,int scheduleID){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
		String d = year+"-"+month+"-"+day;
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(format.parse(d));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(remindID >= 0 && remindID <= 3){
			ScheduleDateTag dateTag = new ScheduleDateTag();
			dateTag.setYear(Integer.parseInt(year));
			dateTag.setMonth(Integer.parseInt(month));
			dateTag.setDay(Integer.parseInt(day));
			dateTag.setScheduleID(scheduleID);
			dateTagList.add(dateTag);
		}else if(remindID == 4){
			for(int i =0; i <= (2049-Integer.parseInt(year))*12*4*7; i++){
				if( i==0 ){
					cal.add(Calendar.DATE, 0);
				}else{
				    cal.add(Calendar.DATE, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 5){
			for(int i =0; i <= (2049-Integer.parseInt(year))*12*4; i++){
				if( i==0 ){
					cal.add(Calendar.WEEK_OF_MONTH, 0);
				}else{
				    cal.add(Calendar.WEEK_OF_MONTH, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 6){
			for(int i =0; i <= (2049-Integer.parseInt(year))*12; i++){
				if( i==0 ){
					cal.add(Calendar.MONTH, 0);
				}else{
				    cal.add(Calendar.MONTH, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 7){
			for(int i =0; i <= 2049-Integer.parseInt(year); i++){
				if( i==0 ){
					cal.add(Calendar.YEAR, 0);
				}else{
				    cal.add(Calendar.YEAR, 1);
				}
				handleDate(cal,scheduleID);
			}
		}
		dao.saveTagDate(dateTagList);
	}
	

	public void handleDate(Calendar cal, int scheduleID){
		ScheduleDateTag dateTag = new ScheduleDateTag();
		dateTag.setYear(cal.get(Calendar.YEAR));
		dateTag.setMonth(cal.get(Calendar.MONTH)+1);
		dateTag.setDay(cal.get(Calendar.DATE));
		dateTag.setScheduleID(scheduleID);
		dateTagList.add(dateTag);
	}
	

	public String handleInfo(int year, int month, int day, int hour, int minute, String week, int remindID){
		String remindType = remind[remindID];
		String show = "";
		if(0 <= remindID && remindID <= 4){
			show = year+"-"+month+"-"+day+"\t"+hour+":"+minute+"\t"+week+"\t\t"+remindType;
		}else if(remindID == 5){
			show = "everyweek"+week+"\t"+hour+":"+minute;
		}else if(remindID == 6){
			show = "everymonth"+day+"号"+"\t"+hour+":"+minute;
		}else if(remindID == 7){
			show = "everyyear"+month+"-"+day+"\t"+hour+":"+minute;
		}
		return show;
	}
	

	public String getScheduleDate() {
		Intent intent = getIntent();
		if(intent.getStringArrayListExtra("scheduleDate") != null){
			scheduleDate = intent.getStringArrayListExtra("scheduleDate");
		}else if(intent.getExtras().getInt("from")==1){
			scheduleVO=(ScheduleVO) intent.getExtras().getSerializable("scheduleVO");
		}
		int [] schType_remind = intent.getIntArrayExtra("schType_remind");  //从ScheduleTypeView中传来的值(包含日程类型和提醒次数信息)
		
		if(schType_remind != null){
			sch_typeID = schType_remind[0];
			remindID = schType_remind[1];
			scheduleType.setText(sch_type[sch_typeID]+"\t\t\t\t"+remind[remindID]);
		}
		scheduleYear = scheduleDate.get(0);
		scheduleMonth = scheduleDate.get(1);
		tempMonth = scheduleMonth;
		if (Integer.parseInt(scheduleMonth) < 10) {
			scheduleMonth = "0" + scheduleMonth;
		}
		scheduleDay = scheduleDate.get(2);
		tempDay = scheduleDay;
		if (Integer.parseInt(scheduleDay) < 10) {
			scheduleDay = "0" + scheduleDay;
		}
		week = scheduleDate.get(3);
		String hour_c = String.valueOf(hour);
		String minute_c = String.valueOf(minute);
		if(hour < 10){
			hour_c = "0"+hour_c;
		}
		if(minute < 10){
			minute_c = "0"+minute_c;
		}
		String scheduleLunarDay = getLunarDay(Integer.parseInt(scheduleYear),
				Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
		String scheduleLunarMonth = lc.getLunarMonth();
		StringBuffer scheduleDateStr = new StringBuffer();
		scheduleDateStr.append(scheduleDay).append("-").append(scheduleMonth)
				.append("-").append(scheduleYear).append(" ").append(hour_c).append(":").append(minute_c).append("\n")
				.append(" ").append(week);
		return scheduleDateStr.toString();
	}


	public String getLunarDay(int year, int month, int day) {
		String lunarDay = lc.getLunarDate(year, month, day, true);
		if (lunarDay.substring(1, 2).equals("月")) {
			lunarDay = "初一";
		}
		return lunarDay;
	}
	 public static void setAlart(Context context){
		 ScheduleDAO dao1=new ScheduleDAO(context);
		ArrayList<ScheduleVO> arrSch=dao1.getAllSchedule();
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		long time;
		String content=arrSch.get(0).getScheduleContent();
		time=arrSch.get(0).getAlartime();
		for (ScheduleVO vo : arrSch) {
			if(vo.getAlartime()>mCalendar.getTimeInMillis()){
				if(time<mCalendar.getTimeInMillis()){
					time=vo.getAlartime();
					content=vo.getScheduleContent();
				if(time>vo.getAlartime()){
					time=vo.getAlartime();
					content=vo.getScheduleContent();
				}
				}else{
					if(time>vo.getAlartime()){
						time=vo.getAlartime();
						content=vo.getScheduleContent();
					}
				}
			}
		}
		if(time>mCalendar.getTimeInMillis()){
		ObjectPool.mAlarmHelper.openAlarm(32,content,time);
		}
	}
}
