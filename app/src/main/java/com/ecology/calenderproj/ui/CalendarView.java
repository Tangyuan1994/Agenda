package com.ecology.calenderproj.ui;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecology.calenderproj.R;
import com.ecology.calenderproj.bean.ScheduleDAO;
import com.ecology.calenderproj.bean.ScheduleDateTag;
import com.ecology.calenderproj.calender.LunarCalendar;
import com.ecology.calenderproj.calender.SpecialCalendar;


public class CalendarView extends BaseAdapter {
	
	private static final String Tag="CalendarView";

	private ScheduleDAO dao = null;
	private boolean isLeapyear = false;
	private int daysOfMonth = 0;
	private int firstDayOfMonth = 0;
	private int lastDaysOfMonth = 0;
	private Context context;
	private String[] dayNumber = new String[49];
	private static String week[] = {"Dim","Lun","Mar","Mer","Jeu","Ven","Sam"};
	private SpecialCalendar specialCalendar = null;
	private LunarCalendar lunarCalendar = null; 
	private Resources res = null;
	private Drawable drawable = null;
	
	private String currentYear = "";
	private String currentMonth = "";
	private String currentDay = "";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
	private int currentFlag = -1;
	private int[] schDateTagFlag = null;
	
	private String showYear = "";
	private String showMonth = "";
	private String animalsYear = ""; 
	private String leapMonth = "";
	private String cyclical = "";
	private String sysDate = "";  
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";
	

	private String sch_year = "";
	private String sch_month = "";
	private String sch_day = "";
	
	public CalendarView(){
		Date date = new Date();
		sysDate = sdf.format(date);
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];
		
	}
	
	public CalendarView(Context context,Resources rs,int jumpMonth,int jumpYear,int year_c,int month_c,int day_c){
		this();
		this.context= context;
		specialCalendar = new SpecialCalendar();
		lunarCalendar = new LunarCalendar();
		this.res = rs;
		
		int stepYear = year_c+jumpYear;
		int stepMonth = month_c+jumpMonth ;
		if(stepMonth > 0){

			if(stepMonth%12 == 0){
				stepYear = year_c + stepMonth/12 -1;
				stepMonth = 12;
			}else{
				stepYear = year_c + stepMonth/12;
				stepMonth = stepMonth%12;
			}
		}else{
			stepYear = year_c - 1 + stepMonth/12;
			stepMonth = stepMonth%12 + 12;
			if(stepMonth%12 == 0){
				
			}
		}
	
		currentYear = String.valueOf(stepYear);;
		currentMonth = String.valueOf(stepMonth);
		currentDay = String.valueOf(day_c);
		
		getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));
		
	}
	
	public CalendarView(Context context,Resources rs,int year, int month, int day){
		this();
		this.context= context;
		specialCalendar = new SpecialCalendar();
		lunarCalendar = new LunarCalendar();
		this.res = rs;
		currentYear = String.valueOf(year);
		currentMonth = String.valueOf(month);
		currentDay = String.valueOf(day);
		
		getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return dayNumber.length;
	}

	
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.calendar, null);
		 }
		TextView textView = (TextView) convertView.findViewById(R.id.tvtext);
		String d = dayNumber[position].split("\\.")[0];
		String dv = dayNumber[position].split("\\.")[1];
		Log.i("calendarview", d+","+dv);
		SpannableString sp = new SpannableString(d+"\n"+dv);
		
		Log.i(Tag, "SpannableString---"+sp);
		
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new RelativeSizeSpan(1.2f) , 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new TypefaceSpan("monospace"), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(dv != null || dv != ""){

            sp.setSpan(new RelativeSizeSpan(0.75f), d.length()+1, dayNumber[position].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		textView.setText(sp);
		textView.setTextColor(Color.RED);
		

		if (position < daysOfMonth + firstDayOfMonth+7 && position >= firstDayOfMonth+7) {
			
			
			
			textView.setTextColor(Color.BLACK);
			drawable = res.getDrawable(R.drawable.item);
			textView.setBackgroundDrawable(drawable);
			if(position%7==0||position%7==6){
				textView.setTextColor(Color.rgb(255,120,20));
			}

			
		}else {


			if(position<7){
				
				textView.setTextColor(Color.BLACK);
				textView.setTextSize(14.0f);

				drawable = res.getDrawable(R.drawable.week_top);
				textView.setBackgroundDrawable(drawable);
			}

			else{
		
				textView.setTextColor(Color.rgb(200, 195, 200));
			}
		}
		if(schDateTagFlag != null && schDateTagFlag.length >0){
			for(int i = 0; i < schDateTagFlag.length; i++){
				if(schDateTagFlag[i] == position){

					
					textView.setBackgroundResource(R.drawable.mark);
				}
			}
		}

		if(currentFlag == position){ 
			
			drawable = res.getDrawable(R.drawable.current_day_bgc);
			textView.setBackgroundDrawable(drawable);
			textView.setTextColor(Color.WHITE);
		}

		Calendar calendar=Calendar.getInstance();
		if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
			//textView.setTextColor(Color.rgb(255, 145, 90));
		}
		
		return convertView;
	}
	

	public void getCalendar(int year, int month){
		isLeapyear = specialCalendar.isLeapYear(year);
		daysOfMonth = specialCalendar.getDaysOfMonth(isLeapyear, month);
		firstDayOfMonth = specialCalendar.getWeekdayOfMonth(year, month);
		lastDaysOfMonth = specialCalendar.getDaysOfMonth(isLeapyear, month-1);
		
		Log.d("DAY", isLeapyear+" ======  "+daysOfMonth+"  ============  "+firstDayOfMonth+"  =========   "+lastDaysOfMonth);
		getweek(year,month);
	}
	

	private void getweek(int year, int month) {
		int j = 1;
		int flag = 0;
		String lunarDay = "";
		

		dao = new ScheduleDAO(context);
		ArrayList<ScheduleDateTag> dateTagList = dao.getTagDate(year,month);
		if(dateTagList != null && dateTagList.size() > 0){
			schDateTagFlag = new int[dateTagList.size()];
		}
		
		for (int i = 0; i < dayNumber.length; i++) {

			if(i<7){
				dayNumber[i]=week[i]+"."+" ";
			}
			else if(i < firstDayOfMonth+7){
				int temp = lastDaysOfMonth - firstDayOfMonth+1-7;

				lunarDay = lunarCalendar.getLunarDate(year, month-1, temp+i,false);
				dayNumber[i] = (temp + i)+"."+lunarDay;
			}else if(i < daysOfMonth + firstDayOfMonth+7){
				String day = String.valueOf(i-firstDayOfMonth+1-7); 

				lunarDay = lunarCalendar.getLunarDate(year, month, i-firstDayOfMonth+1-7,false);
				dayNumber[i] = i-firstDayOfMonth+1-7+"."+lunarDay;

				if(sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)){

					currentFlag = i;
				}
				

				if(dateTagList != null && dateTagList.size() > 0){
					for(int m = 0; m < dateTagList.size(); m++){
						ScheduleDateTag dateTag = dateTagList.get(m);
						int matchYear = dateTag.getYear();
						int matchMonth = dateTag.getMonth();
						int matchDay = dateTag.getDay();
						if(matchYear == year && matchMonth == month && matchDay == Integer.parseInt(day)){
							schDateTagFlag[flag] = i;
							flag++;
						}
					}
				}
				
				setShowYear(String.valueOf(year));
				setShowMonth(String.valueOf(month));
				setAnimalsYear(lunarCalendar.animalsYear(year));
				setLeapMonth(lunarCalendar.leapMonth == 0?"":String.valueOf(lunarCalendar.leapMonth));
				setCyclical(lunarCalendar.cyclical(year));
			}else{
				lunarDay = lunarCalendar.getLunarDate(year, month+1, j,false);
				dayNumber[i] = j+"."+lunarDay;
				j++; 
			}
		}
        
        String dayStr = "";
        for(int i = 0; i < dayNumber.length; i++){
        	dayStr = dayStr+dayNumber[i]+":";
        }
        Log.d("calendarview",dayStr);


	}
	
	
	public void matchScheduleDate(int year, int month, int day){
		
	}

	public String getDateByClickItem(int position){
		return dayNumber[position];
	}
	

	public int getStartPositon(){
		return firstDayOfMonth+7;
	}
	

	public int getEndPosition(){
		return  (firstDayOfMonth+daysOfMonth+7)-1;
	}
	
	public String getShowYear() {
		return showYear;
	}

	public void setShowYear(String showYear) {
		this.showYear = showYear;
	}

	public String getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(String showMonth) {
		this.showMonth = showMonth;
	}
	
	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}
	
	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}
	
	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}
}
