package com.jack.tab;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

public class MyTab0Activity extends Activity implements Runnable{
	
	private TabHost tabHost;
	private EditText SEND_ET;
	private Button   SEND_BT;
	private EditText RECV_ET;
	private Button   RECV_BT;
	serial com3 = new serial();
	String tag = "serial test";
	private Thread   thread_recv;
	private Thread   thread_send;
	private Thread   thread_myView;
	
	private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private Spinner spinner5;
    private Button  SET_BT;
    private int     spinner1Selected;
    private int     spinner2Selected;
    private int     spinner3Selected;
    private int     spinner4Selected;
    private int     spinner5Selected;
    private boolean openFlag=false;	
    private boolean sendEnableFlag=false;
    private int     comPort[]={1,2,3,4,5,6,7,8,9};
    private int     comBaud[]={110,300,1200,2400,4800,9600,19200,38400,
    		             57600,115200,230400,460800,921600};
    
    private int     rmx,rmy;
    private String  rmxs,rmys;
    private static final int PORT = 9999;  
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tabHost=(TabHost)findViewById(R.id.tabhost);
        tabHost.setup();
        LayoutInflater inflater=LayoutInflater.from(this);
        inflater.inflate(R.layout.tab0, tabHost.getTabContentView());
        inflater.inflate(R.layout.tab1, tabHost.getTabContentView());
        inflater.inflate(R.layout.tab2, tabHost.getTabContentView());
        tabHost.addTab(tabHost.newTabSpec("tab0")
        		.setIndicator("串口设置")
        		.setContent(R.id.LinearLayout00));
        tabHost.addTab(tabHost.newTabSpec("tab1")
        		.setIndicator("串口通信")
        		.setContent(R.id.LinearLayout01));
        tabHost.addTab(tabHost.newTabSpec("tab2")
        		.setIndicator("监控界面")
        		.setContent(R.id.LinearLayout02));
        //串口设置界面        
        spinner1=(Spinner)findViewById(R.id.Spinner1);
        spinner2=(Spinner)findViewById(R.id.Spinner2);
        spinner3=(Spinner)findViewById(R.id.Spinner3);
        spinner4=(Spinner)findViewById(R.id.Spinner4);
        spinner5=(Spinner)findViewById(R.id.Spinner5);       
        spinner1.setSelection(2);
        spinner2.setSelection(9);
        spinner3.setSelection(3);
        spinner4.setSelection(0);
        spinner5.setSelection(0);  
        SET_BT=(Button)findViewById(R.id.LL00_button1);
        SET_BT.setOnClickListener(new serialSet());
        //串口收发界面
        SEND_ET=(EditText)findViewById(R.id.LL01_editText1);    
        SEND_BT=(Button)findViewById(R.id.LL01_button1);  
        RECV_ET=(EditText)findViewById(R.id.LL01_editText2);    
        RECV_BT=(Button)findViewById(R.id.LL01_button2); 
//        com3.Open(3, 115200);
        SEND_BT.setOnClickListener(new serialManager());
        RECV_BT.setOnClickListener(new serialManager());       
        //监控界面
        final FrameLayout FL=(FrameLayout)findViewById(R.id.LinearLayout02);      
        FL.addView(new MyView(this));
        //发送模块
        Thread sendModle=new Thread(MyTab0Activity.this);
        sendModle.start();
    }
    
    class serialSet implements OnClickListener{

    	private int Port=0;
    	private int Rate=0;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			spinner1Selected=spinner1.getSelectedItemPosition();
			spinner2Selected=spinner2.getSelectedItemPosition();
			spinner3Selected=spinner3.getSelectedItemPosition();
			spinner4Selected=spinner4.getSelectedItemPosition();
			spinner5Selected=spinner5.getSelectedItemPosition(); 
			Port=comPort[spinner1Selected];
			Rate=comBaud[spinner2Selected];
						
			
			if(!openFlag){
				SET_BT.setText("关闭串口");
				openFlag=true;
				sendEnableFlag=true;
				System.out.print("串口选择了"+spinner1Selected);	
				com3.Open(Port,Rate);
		//		thread_recv.start();
		   	}
			else{
				SET_BT.setText("打开串口");
				openFlag=false;
				sendEnableFlag=false;
				com3.Close();
			//	thread_send.stop();
			//	thread_recv.stop();
				if(thread_send!=null){
					thread_send.interrupt();
					thread_send=null;
				//	thread_send.stop();
				}
				if(thread_recv!=null){
					thread_recv.interrupt();
					thread_recv=null;
				//	thread_recv.stop();
				}
			}
		}
    	
    }
    
    class serialManager implements OnClickListener{

		@Override
		public void onClick(View v) {
 			
 			if(!openFlag){
 				Toast.makeText(MyTab0Activity.this, "请打开串口", Toast.LENGTH_SHORT).show();
 			}else{
 				switch (v.getId()) {
 	 			case R.id.LL01_button1:   //Send
 	 				
 	               thread_send = new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int i;
						while(sendEnableFlag){
							Log.d(tag,"send start ..."); 
		 	 				CharSequence tx = SEND_ET.getText();				
		 	 				int[] text = new int[tx.length()];				
		 	                for (i=0; i<tx.length(); i++) 
		 	                {
		 	                        text[i] = tx.charAt(i);
		 	                }
							com3.Write(text, tx.length()); 	
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
 	            	   
 	               });
 	              thread_send.start();	 							
// 	 				SEND_ET.setText("");
 	 				break;				
 	 			case R.id.LL01_button2:  //RECV 	
 	 				
 	 				thread_recv=new Thread(new Runnable(){

 	 					@Override
 	 					public void run() {
 	 						// TODO Auto-generated method stub
 	 						while(sendEnableFlag){
 	 							Log.d(tag,"recv start ...");	 			
 	 		 	 				int[] RX = com3.Read();
 	 		 	 				if(RX != null)
 	 		 	 				{
	 	 		 	 				try {
	 	 		 	 		//			RECV_ET.setText("");	 	 		 	 					
	 	 		 	 					RECV_ET.append(new String(RX, 0, RX.length));
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} 	 		 	 					
 	 		 	 					RX = null;
 	 		 	 				} 	 		 					
 	 		 	 			//	System.out.println("" + RX);
 	 		 	 			//	RECV_ET.append("" + RX);
 	 		 					try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
 	 						}						
 	 					}
 	 				});
 	 					
 	 				thread_recv.start();
 	 				
 	 				break;  
 				}
 			}  	
    }
    }   
    public class MyView extends View{
    	int    counter = 0;
    	int    rdmx,rdmy;
    	int    widthDisplay,heightDisplay;
    	public MyView(Context context) {
    		super(context);
    		// TODO Auto-generated constructor stub
    	}
    	@Override
    	protected void onDraw(Canvas canvas){
			Paint paint=new Paint();
    		paint.setAntiAlias(true);
    		paint.setStrokeWidth(3);
    		paint.setStyle(Style.STROKE);
    		paint.setColor(Color.BLUE);       	    
    	    //获取屏幕尺寸信息
    	    DisplayMetrics dm=new DisplayMetrics();
    	    getWindowManager().getDefaultDisplay().getMetrics(dm);
    	    widthDisplay=(dm.widthPixels)/2;   //宽度
    	    heightDisplay=(dm.heightPixels-120)/2; //高度
    	    rmx=(int)(Math.random()*200);
    	    rmy=(int)(Math.random()*200);
    	    rdmx=rmx+widthDisplay-100;  //512-100
    	    rdmy=rmy+heightDisplay-100;  //220-100
    	    canvas.drawCircle(widthDisplay, heightDisplay, 5, paint);
    	    canvas.drawCircle(widthDisplay, heightDisplay, 50, paint);
    	    canvas.drawCircle(widthDisplay, heightDisplay, 100, paint);
    	    canvas.drawCircle(widthDisplay, heightDisplay, 150, paint);
    	    canvas.drawCircle(widthDisplay, heightDisplay, 200, paint);
    	    paint.setColor(Color.RED);
    		canvas.drawCircle(rdmx, rdmy, 5, paint);
    		canvas.drawPoint(rdmx, rdmy, paint);
    		invalidate();
    	}
    }
    
    static {
        System.loadLibrary("serialtest");
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub		
		 try {  
	            // 实例化服务器套接字 设置端口号9999  
	            ServerSocket server = new ServerSocket(PORT);  
	            while (true) {  
	                Socket socket = server.accept();  
	                // 获取输出流  
	                BufferedWriter writer = new BufferedWriter(  
	                        new OutputStreamWriter(socket.getOutputStream()));  
	                rmxs=Integer.toString(rmx);
	                writer.write(rmxs);
	                writer.write(" "); 
	                rmys=Integer.toString(rmy);
	                writer.write(rmys);              
	                writer.flush();  
	                writer.close();  
	            }  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } 
	} 
}



