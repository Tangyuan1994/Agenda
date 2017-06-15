package com.ecology.calenderproj.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ecology.calenderproj.R;
import com.ecology.calenderproj.base.BorderTextView;
import com.ecology.calenderproj.calender.LunarCalendar;


public class CalendarConvertTrans extends Activity {
	//Modified By Wanyanyuan Tang

	private LunarCalendar lc = null;
	private BorderTextView convertDate = null;
	private BorderTextView convertBT = null;
	private TextView lunarDate = null;
	
	private int year_c;
	private int month_c;
	private int day_c;
	
	public CalendarConvertTrans(){
		lc = new LunarCalendar();
	}
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.convert);

		convertDate = (BorderTextView) findViewById(R.id.convertDate);
		convertBT = (BorderTextView) findViewById(R.id.convert);
		lunarDate = (TextView) findViewById(R.id.convertResult);
		
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	String currentDate = sdf.format(date);
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
		convertDate.setText(day_c+"-"+month_c+"-"+year_c);
		
		convertDate.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {

				new DatePickerDialog(CalendarConvertTrans.this, new OnDateSetListener() {
					
					
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {

						if(year < 1901 || year > 2049){
							new AlertDialog.Builder(CalendarConvertTrans.this).setTitle("Date Invalide").setMessage("Date Valide(1901/1/1-2049/12/31)").setPositiveButton("Oui", null).show();
						}else{
							year_c = year;
							month_c = monthOfYear+1;
							day_c = dayOfMonth;
							convertDate.setText(day_c+"/"+month_c+"/"+year_c);
						}
					}
				}, year_c, month_c-1, day_c).show();
			}
		});
		
		convertBT.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {

				String lunarDay = getLunarDay(year_c,month_c,day_c);
				String lunarYear = String.valueOf(lc.getYear());
				String lunarMonth = lc.getLunarMonth();
				
				lunarDate.setText(lunarDay+"-"+lunarMonth+lunarYear);
			}
		});
		
	}
	

	public String getLunarDay(int year, int month, int day) {
		String lunarDay = lc.getLunarDate(year, month, day, true);
		return lunarDay;
	}
}
