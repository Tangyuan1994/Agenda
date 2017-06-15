package com.ecology.calenderproj.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ecology.calenderproj.R;
import com.ecology.calenderproj.base.BorderText;
import com.ecology.calenderproj.base.BorderTextView;
import com.ecology.calenderproj.bean.ScheduleDAO;
import com.ecology.calenderproj.calender.LunarCalendar;
import com.ecology.calenderproj.vo.ScheduleVO;

import static com.ecology.calenderproj.ui.ScheduleViewAddActivity.setAlart;


public class CalendarActivity extends Activity implements OnGestureListener,OnClickListener {

	private static final String Tag="CalendarActivity";
	private LunarCalendar lcCalendar = null;
	private LunarCalendar calendar;
	private ViewFlipper flipper = null;
	private GestureDetector gestureDetector = null;
	private CalendarView calV = null;
	private GridView gridView = null;
	private BorderText topText = null;
	private Drawable draw = null;
	private static int jumpMonth = 0;
	private static int jumpYear = 0;
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private ScheduleDAO dao = null;
	private String[] scheduleIDs;
	private  ArrayList<String> scheduleDate;
	private Dialog builder;
	private ScheduleVO scheduleVO_del;
	private String scheduleitems[];
	private BorderTextView schdule_tip;
	private Button add;
	private Button check;
	private Button quit;
	private TextView day_tv;
	private TextView launarDay;
	private ListView listView;
	private TextView weekday;
	private TextView lunarTime;
	private ListView list;
	private String dateInfo;
	private LayoutInflater inflater;
	ScheduleVO scheduleVO;

	public CalendarActivity() {

		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
    	
    	
    	
    	dao = new ScheduleDAO(this);
	}

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.calender_main);
		gestureDetector = new GestureDetector(this);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();
        calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
        
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView,0);
        
		topText = (BorderText) findViewById(R.id.schedule_toptext);
		addTextToTopTextView(topText);
	}
	
	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gvFlag = 0;
		if (e1.getX() - e2.getX() > 50) {
			addGridView();
			jumpMonth++;
			
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView, gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
			this.flipper.showNext();
			flipper.removeViewAt(0);
			return true;
		} else if (e1.getX() - e2.getX() < -50) {

			addGridView();
			jumpMonth--;
			
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        gvFlag++;
	        addTextToTopTextView(topText);
	        flipper.addView(gridView,gvFlag);
	        
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			return true;
		}
		return false;
	}

	
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, menu.FIRST, menu.FIRST, "Today");
		menu.add(0, menu.FIRST+1, menu.FIRST+1, "TO");
		menu.add(0, menu.FIRST+2, menu.FIRST+2, "Date");
		return super.onCreateOptionsMenu(menu);
	}

	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
        case Menu.FIRST:

        	int xMonth = jumpMonth;
        	int xYear = jumpYear;
        	int gvFlag =0;
        	jumpMonth = 0;
        	jumpYear = 0;
        	addGridView();
        	year_c = Integer.parseInt(currentDate.split("-")[0]);
        	month_c = Integer.parseInt(currentDate.split("-")[1]);
        	day_c = Integer.parseInt(currentDate.split("-")[2]);
        	calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView,gvFlag);
	        if(xMonth == 0 && xYear == 0){
	        }else if((xYear == 0 && xMonth >0) || xYear >0){
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
				this.flipper.showNext();
	        }else{
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
				this.flipper.showPrevious();
	        }
			flipper.removeViewAt(0);
        	break;
        case Menu.FIRST+1:
        	
        	new DatePickerDialog(this, new OnDateSetListener() {
				
				
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {

					if(year < 1901 || year > 2049){
						new AlertDialog.Builder(CalendarActivity.this).setTitle("wrong data").setMessage("choose data in(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
					}else{
						int gvFlag = 0;
						addGridView();
			        	calV = new CalendarView(CalendarActivity.this, CalendarActivity.this.getResources(),year,monthOfYear+1,dayOfMonth);
				        gridView.setAdapter(calV);
				        addTextToTopTextView(topText);
				        gvFlag++;
				        flipper.addView(gridView,gvFlag);
				        if(year == year_c && monthOfYear+1 == month_c){

				        }
				        if((year == year_c && monthOfYear+1 > month_c) || year > year_c ){
				        	CalendarActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_left_in));
				        	CalendarActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_left_out));
				        	CalendarActivity.this.flipper.showNext();
				        }else{
				        	CalendarActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_right_in));
				        	CalendarActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_right_out));
				        	CalendarActivity.this.flipper.showPrevious();
				        }
				        flipper.removeViewAt(0);
				        year_c = year;
						month_c = monthOfYear+1;
						day_c = dayOfMonth;
						jumpMonth = 0;
						jumpYear = 0;
					}
				}
			},year_c, month_c-1, day_c).show();
        	break;
        	
        case Menu.FIRST+2:
        	Intent mIntent=new Intent(CalendarActivity.this, CalendarConvertTrans.class);
        startActivity(mIntent);
        	
        	break;
        	
        }
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	public boolean onTouchEvent(MotionEvent event) {

		return this.gestureDetector.onTouchEvent(event);
	}

	
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	

	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		draw = getResources().getDrawable(R.drawable.schedule_title_bg);
		view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowMonth()).append("-").append(
				calV.getShowYear()).append(" ").append("\t");

		view.setText(textDate);
		view.setTextColor(Color.WHITE);
		view.setTextSize(15.0f);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	

	public void addLunarDayInfo(TextView text){
		StringBuffer textDate = new StringBuffer();
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("闰").append(calV.getLeapMonth()).append("月")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("年").append("(").append(
				calV.getCyclical()).append("年)");
		text.setText(textDate);
	}

	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth(); 
        int Height = display.getHeight();
        
        Log.d(Tag, "pixel=="+"height*weight"+Height+Width);
        
		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.setColumnWidth(46);
		if(Width == 480 && Height == 800){
			gridView.setColumnWidth(69);
		}else if(Width==800&&Height==1280){
			gridView.setColumnWidth(69);
		}
		
		
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
        gridView.setBackgroundResource(R.drawable.gridview_bk);
		gridView.setOnTouchListener(new OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return CalendarActivity.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView中的每一个item的点击事件
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				  int startPosition = calV.getStartPositon();
				  int endPosition = calV.getEndPosition();
				  if(startPosition <= position  && position <= endPosition){
					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];
	                  String scheduleYear = calV.getShowYear();
	                  String scheduleMonth = calV.getShowMonth();
	                  String week = "";
	                 
	                  Log.i("history", scheduleDay);

	                  scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));

	                  switch(position%7){
	                  case 0:
	                	  week = "Dimench";
	                	  break;
	                  case 1:
	                	  week = "Lundi";
	                	  break;
	                  case 2:
	                	  week = "Mardi";
	                	  break;
	                  case 3:
	                	  week = "Mercredi";
	                	  break;
	                  case 4:
	                	  week = "Jeudi";
	                	  break;
	                  case 5:
	                	  week = "Vendredi";
	                	  break;
	                  case 6:
	                	  week = "Samedi";
	                	  break;
	                  }
					 
	                  scheduleDate = new ArrayList<String>();
	                  scheduleDate.add(scheduleYear);
	                  scheduleDate.add(scheduleMonth);
	                  scheduleDate.add(scheduleDay);
	                  scheduleDate.add(week);
	                  

                	   LayoutInflater inflater=getLayoutInflater();
	              		View linearlayout= inflater.inflate(R.layout.schedule_details, null);
	              		 add=(Button)linearlayout.findViewById(R.id.btn_add);
					     check=(Button)linearlayout.findViewById(R.id.btn_check);
	              		 quit=(Button) linearlayout.findViewById(R.id.btn_back);
	              	 day_tv=(TextView) linearlayout.findViewById(R.id.todayDate);
	              		launarDay=(TextView)linearlayout.findViewById(R.id.tv_launar);
	                  schdule_tip=(com.ecology.calenderproj.base.BorderTextView)linearlayout.findViewById(R.id.schdule_tip);
	              	 listView=(ListView)linearlayout.findViewById(R.id.schedulelist);
	              		 weekday=(TextView)linearlayout.findViewById(R.id.dayofweek);
	              		 lunarTime=(TextView)linearlayout.findViewById(R.id.lunarTime);
	              		list=(ListView)linearlayout.findViewById(R.id.schedulelist);
	              	
	              	 dateInfo=scheduleDay+"/"+scheduleMonth+"/"+scheduleYear;
	              	String scheduleLunarDay = getLunarDay(Integer.parseInt(scheduleYear),
	        				Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
	              	
	              	Log.i("LunarDay", scheduleLunarDay);
	              		day_tv.setText(dateInfo);
	              		weekday.setText(week);
	              		addLunarDayInfo(lunarTime);
	              		launarDay.setText( scheduleLunarDay);
	              		
	              		Log.i("scheduleDate", "scheduleDate："+scheduleDate);

	              		add.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(builder!=null&&builder.isShowing()){
									builder.dismiss();
									Intent intent = new Intent();
					                  intent.putStringArrayListExtra("scheduleDate", scheduleDate);
					                  intent.setClass(CalendarActivity.this, ScheduleViewAddActivity.class);
					                  startActivity(intent);
								}
							}
						});
	              		//Modified By Wanyanyuan Tang
	              		quit.setOnClickListener(new OnClickListener() {
							
						public void onClick(View v) {
								// TODO Auto-generated method stub
							if(builder!=null&&builder.isShowing()){
									builder.dismiss();
								}
							}
						});


					  check.setOnClickListener(new OnClickListener() {
						  //Add By Wanyanyuan Tang
						  public void onClick(View v) {
							  //Add By Wanyanyuan Tang
							  // TODO Auto-generated method stub
								  Intent intent = new Intent();
								  intent.setClass(CalendarActivity.this, CalendarConvertTrans.class);
								  startActivity(intent);
							  }
					  });
	                  

                  if(scheduleIDs != null && scheduleIDs.length > 0){
		              		View inflate=getLayoutInflater().inflate(R.layout.schedule_detail_item, null);

		              		ArrayList<HashMap<String,String>> Data = new ArrayList<HashMap<String, String>>();  
							ScheduleDAO dao=new ScheduleDAO(CalendarActivity.this);
							 String time="";
		                	  String content="";
	                	  for(int i=0;i<scheduleIDs.length;i++){
	                	  scheduleVO=dao.getScheduleByID(CalendarActivity.this,Integer.parseInt(scheduleIDs[i]));
	                	 time="";
	                	 content="";
	                	  
	                	  time=dateInfo+" "+scheduleVO.getTime();
	                	  content=scheduleVO.getScheduleContent();
	                		
	                	 
	                	 
	                		  HashMap<String, String> map=new HashMap<String, String>();
	                		  map.put("date", time);
	                		  map.put("content", content);
          	              	  Data.add(map);
          	              	  
	                	  }
	                	 String  from[]={"date","content"};
	                	  int to[]={R.id.itemTime,R.id.itemContent};
	                	  
	                	  SimpleAdapter adapter=new SimpleAdapter(CalendarActivity.this, Data, R.layout.schedule_detail_item, from, to);
	                	  
	                	  list.setAdapter(adapter);


	                	  
	                  }else{
	                 
	                	  
	                	  schdule_tip.setText("No Planning");
	                	  listView.setVisibility(View.INVISIBLE);

	                  }


            	  builder =	new Dialog(CalendarActivity.this,R.style.FullScreenDialog);
            	  builder.setContentView(linearlayout);
            	  WindowManager windowManager = getWindowManager();
            	  Display display = windowManager.getDefaultDisplay();
            	  WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
            	  lp.width = (int)(display.getWidth());
            	  lp.height=display.getHeight();
            	  builder.getWindow().setAttributes(lp); 
            	  builder.setCanceledOnTouchOutside(true);
            	  builder.show();


					  list.setOnItemClickListener(new OnItemClickListener(){
						  //Add By Wanyanyuan Tang
						  public void onItemClick(AdapterView<?> adapterview, View view, int position, long id) {;
                              final int position1=position;
							  new AlertDialog.Builder(CalendarActivity.this).setTitle("Suppreimez Ce Message").setMessage("Vous etre sur？").setPositiveButton("Oui", new DialogInterface.OnClickListener() {

								  public void onClick(DialogInterface dialog, int which) {

									   // dao.delete(scheduleVO.getScheduleID());
									      dao.delete(Integer.parseInt(scheduleIDs[position1]));
										  Toast.makeText(CalendarActivity.this, "Delete", 0).show();
										  Intent intent = new Intent();
										  intent.setClass(CalendarActivity.this, CalendarActivity.class);
										  startActivity(intent);
									  }
								  }).setNegativeButton("Noooooooo", null).show();

							  }


					  });
	                  
				  }
			}
		});
		gridView.setLayoutParams(params);
	}
	

	private class CalendarMarkedAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			inflater=LayoutInflater.from(CalendarActivity.this);
			ViewHolder holder=new ViewHolder();
			if(convertView==null){
				convertView=getLayoutInflater().inflate(R.layout.schedule_detail_item, null);
				holder.itemTime=(TextView) convertView.findViewById(R.id.itemTime);
				holder.itemContent=(TextView) convertView.findViewById(R.id.itemContent);
				
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}

			if(scheduleIDs != null && scheduleIDs.length > 0){

	              		View inflate=getLayoutInflater().inflate(R.layout.schedule_detail_item, null);
                  		ArrayList<HashMap<String,String>> Data = new ArrayList<HashMap<String, String>>();
						ScheduleDAO dao=new ScheduleDAO(CalendarActivity.this);
						 String time="";
	                	  String content="";
              	  for(int i=0;i<scheduleIDs.length;i++){
              	  scheduleVO=dao.getScheduleByID(CalendarActivity.this,Integer.parseInt(scheduleIDs[i]));
              	 time="";
              	 content="";
              	  
              	  time=dateInfo+" "+scheduleVO.getTime();
              	  content=scheduleVO.getScheduleContent();
              		
              	 
              	 
              		  HashMap<String, String> map=new HashMap<String, String>();
              		  map.put("date", time);
              		  map.put("content", content);
    	              	  Data.add(map);
    	              	  
              	  }
              	 String  from[]={"date","content"};
              	  int to[]={R.id.itemTime,R.id.itemContent};
              	  
              	  SimpleAdapter adapter=new SimpleAdapter(CalendarActivity.this, Data, R.layout.schedule_detail_item, from, to);
              	  
              	  list.setAdapter(adapter);
              	  
                }else{
               
              	  
              	  schdule_tip.setText("No Planing");
              	  listView.setVisibility(View.INVISIBLE);

                }
                
			
			return convertView;
		}
		
	}
	
	 public static class ViewHolder {
			TextView itemTime;
			TextView itemContent;
		}

	
	 @Override
		protected void onRestart() {
			int xMonth = jumpMonth;
	    	int xYear = jumpYear;
	    	int gvFlag =0;
	    	jumpMonth = 0;
	    	jumpYear = 0;
	    	addGridView();
	    	year_c = Integer.parseInt(currentDate.split("-")[0]);
	    	month_c = Integer.parseInt(currentDate.split("-")[1]);
	    	day_c = Integer.parseInt(currentDate.split("-")[2]);
	    	calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView,gvFlag);
			flipper.removeViewAt(0);
			super.onRestart();
		}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
//Add By Wanyanyuan Tang
		if(keyCode == KeyEvent.KEYCODE_BACK){
			builder = new AlertDialog.Builder(CalendarActivity.this)
					.setIcon(R.drawable.images)
					.setTitle("Ding~~~~~~~~~~！")
					.setMessage("Sure de Quitter？")
					.setPositiveButton("Oui",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int whichButton) {
									Intent intent = new Intent();
									intent.setAction(Intent.ACTION_MAIN);
									intent.addCategory(Intent.CATEGORY_HOME);
									startActivity(intent);
								}
							})
					.setNegativeButton("Non",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int whichButton) {
									builder.dismiss();
								}
							}).show();
		}
		return true;
	}


	public String getLunarDay(int year, int month, int day) {
		lcCalendar=new LunarCalendar();
		String lunar = lcCalendar.getLunarDate(year, month, day, true);
		return lunar;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}