package com.suun.publics.jCaptcha;


import java.awt.Font;  
import java.awt.Color; 
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.engine.GenericCaptchaEngine;  
import com.octo.captcha.image.gimpy.GimpyFactory; 
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;  
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;  
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;  
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;  
import com.octo.captcha.component.image.color.SingleColorGenerator;  
import com.octo.captcha.component.image.textpaster.NonLinearTextPaster; 
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class CaptchaService {
	/**/
    private static suunGenericManageableCaptchaService instance;
    
    public static void removeInstance(){
    	instance=null;
    }
    
    public static suunGenericManageableCaptchaService getInstance(){
    	if (instance==null){
    	   Font[] fs = new Font[1];/*Font.BOLD*/
           fs[0] = new Font("Arial Black",Font.BOLD, 14);
           
           CaptchaFactory[] cf=new CaptchaFactory[1];        
           cf[0]=new GimpyFactory(
        		 new RandomWordGenerator("ADEFHKLMNPQRTUVWXY34678"),
        		 new ComposedWordToImage( 
      				   /*验证码字体最小值 ,验证码字体最大值,font*/ 
        		      new RandomFontGenerator(16,16,fs), 
        			          /*验证码图片宽度,高*/
        			  new GradientBackgroundGenerator(82,27,//64,25,  
        			    new SingleColorGenerator(new Color(255, 0, 0)),  
        			    new SingleColorGenerator(new Color(255, 200, 0))  
        			  ),  
        			          /*验证码个数最小值,最大值,验证码颜色*/
        			  new NonLinearTextPaster(4,4,new Color(255, 255, 255))        			      
        		   )
        		 );
           
    		instance=new suunGenericManageableCaptchaService(
    				    new GenericCaptchaEngine(cf),  
    	        	    180, // minGuarantedStorageDelayInSeconds  
    	        	    180000,// maxCaptchaStoreSize 
    	        	    10//captchaStoreLoadBeforeGarbageCollection
    	        	);	
    	}
        return instance;
    }
    
	private static Color getRandColor(int fc, int bc) { // 取得给定范围随机颜色

		   Random random = new Random();

		   if (fc > 255) {
		    fc = 255;
		   }

		   if (bc > 255) {
		    bc = 255;
		   }
		   int r = fc + random.nextInt(bc - fc);
		   int g = fc + random.nextInt(bc - fc);
		   int b = fc + random.nextInt(bc - fc);

		   return new Color(r, g, b);
	}

    public  static void generateImageCode(HttpServletRequest request,
	                              HttpServletResponse response) throws Exception {
    	  byte[] captchaChallengeAsJpeg = null;
		  ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		             
		  String captchaId = request.getSession().getId();


		   //生成验证码图片，在这里我们只需要在spring的bean配置文件中更改jcaptchaService的实现，就可以生成不同的验证码图片
        //jcaptchaService.
		  BufferedImage tag = getInstance().getImageChallengeForID(captchaId,request.getLocale());
		  //拉伸
		  BufferedImage challenge= new BufferedImage(80,28,BufferedImage.TYPE_INT_RGB); 
		  challenge.getGraphics().drawImage(tag,0,0,80,28,null);
		  // 干扰线
		  Graphics g =challenge.getGraphics();// challenge.getGraphics(); 
		  //生成随机类  
		  /*Random random = new Random();  
		  // 随机产生10条干扰线，使图象中的认证码不易被其它程序探测到 	
		  
		  for (int i=0;i<10;i++) {  
		       int x = random.nextInt(challenge.getWidth());  
		       int y = random.nextInt(challenge.getHeight());  
		       int xl = random.nextInt(challenge.getWidth()/2);  
		       int yl = random.nextInt(challenge.getHeight()/2); 
		       g.setColor(getRandColor(10,100));
		       g.drawLine(x,y,x+xl,y+yl);  
		  } */ 
		  
		  for (int i=0;i<2;i++) {  
		       int x = (int) (Math.random()*challenge.getWidth()/4);  
		       int y = (int) (Math.random()*challenge.getHeight()/2); 
		       int xl =(int) (Math.random()*challenge.getWidth()/4+challenge.getWidth()/4); 
		       int yl =(int) (Math.random()*challenge.getHeight()/2);
		       g.setColor(getRandColor(10,100));
		       g.drawLine(x,y,x+xl,y+yl);  
		  }
		  /*for (int i=0;i<2;i++) {  
		       int x = (int) (Math.random()*challenge.getWidth()/4);  
		       int y = (int) (Math.random()*challenge.getHeight()/2+challenge.getHeight()/2); 
		       int xl =(int) (Math.random()*challenge.getWidth()/4+challenge.getWidth()/4); 
		       int yl =(int) (Math.random()*challenge.getHeight()/2+challenge.getHeight()/2);
		       g.setColor(getRandColor(10,100));
		       g.drawLine(x,y,x+xl,y+yl);  
		  }*/
		  for (int i=0;i<2;i++) {  
		       int x = (int) (Math.random()*challenge.getWidth()/2+challenge.getWidth()/4);  
		       int y = (int) (Math.random()*challenge.getHeight()/2); 
		       int xl =(int) (Math.random()*challenge.getWidth()*3/4+challenge.getWidth()/4); 
		       int yl =(int) (Math.random()*challenge.getHeight()/2);
		       g.setColor(getRandColor(10,100));
		       g.drawLine(x,y,x+xl,y+yl);  
		  }
		  /*for (int i=0;i<2;i++) {  
		       int x = (int) (Math.random()*challenge.getWidth()*3/4+challenge.getWidth()/4);  
		       int y = (int) (Math.random()*challenge.getHeight()/2+challenge.getHeight()/2); 
		       int xl =(int) (Math.random()*challenge.getWidth()*3/4+challenge.getWidth()/4); 
		       int yl =(int) (Math.random()*challenge.getHeight()/2+challenge.getHeight()/2);
		       g.setColor(getRandColor(10,100));
		       g.drawLine(x,y,x+xl,y+yl);  
		  }*/
		  g.dispose(); 
		  JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
		  jpegEncoder.encode(challenge);

		  captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		 
		  response.setHeader("Cache-Control", "no-store");
		  response.setHeader("Pragma", "no-cache");
		  response.setDateHeader("Expires", 0);
		  response.setContentType("image/jpeg");
		  ServletOutputStream responseOutputStream = response.getOutputStream();
		  responseOutputStream.write(captchaChallengeAsJpeg);
		  responseOutputStream.flush();
		  responseOutputStream.close();
		 
		  return ; 	
    }
    
    public  static Boolean isImageCodeCorrect(HttpServletRequest request,
    		            String j_captcha_response) throws Exception {
    	// 验证码是否正确flag
		Boolean isResponseCorrect = Boolean.FALSE;
		// 取session用来验证是否在同一session中
	//	String captchaId = request.getSession().getId();
		// 取得验证对象，并检验session和输入验证码是否正确
		try{
		   isResponseCorrect = CaptchaService.getInstance()
		          .validateResponseForID(request.getSession().getId(), j_captcha_response.toUpperCase());
	    } catch (Exception e) {
	
		}
	    return isResponseCorrect;
    }
   
    public  static void removeCaptcha(HttpServletRequest request) {
    	CaptchaService.getInstance().removeCaptchaForId(request.getSession().getId());		
    }    
    
}
