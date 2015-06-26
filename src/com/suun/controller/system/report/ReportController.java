package com.suun.controller.system.report;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fr.base.FRContext;
import com.fr.base.core.json.JSONException;
import com.fr.base.core.json.JSONObject;
import com.fr.log.FRLogManager;
import com.fr.report.core.RegistEditionException;
import com.fr.report.script.Calculator;
import com.fr.web.Browser;
import com.fr.web.core.ErrorHandlerHelper;
import com.fr.web.core.ReportDispatcher;
import com.fr.web.core.WebUtils;
import com.fr.web.core.gzip.GZIPResponseWrapper;
import com.fr.web.platform.exception.RedirectException;
import com.suun.publics.controller.BaseController;

@Controller
public class ReportController extends BaseController {
	public static String APP_NAME = null;
	@RequestMapping
	@ResponseBody
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) {	
		if(APP_NAME == null)
        {
            APP_NAME = request.getContextPath();
            if(APP_NAME.startsWith("/"))
                APP_NAME = APP_NAME.substring(1);
        }
        Calculator.saveRequestContext(request);
        GZIPResponseWrapper gzipresponsewrapper = null;
        try{
        	String s = request.getHeader("accept-encoding");
            if(FRContext.getConfigManager().isSupportGzip() && !"false".equals(request.getParameter("gzip")) && s != null && s.indexOf("gzip") != -1 && Browser.resolve(request).supportGzip())
                gzipresponsewrapper = new GZIPResponseWrapper(response);
            if(gzipresponsewrapper != null)
            	request = (HttpServletRequest) gzipresponsewrapper;
            ((HttpServletResponse) request).addHeader("P3P", "CP=CAO PSA OUR");
            ReportDispatcher.dealWithRequest(request, response);
        }catch (RedirectException localRedirectException)
        {
        	FRContext.getLogger().error(localRedirectException.getMessage());
        }catch (RegistEditionException localRegistEditionException) {
        	if(localRegistEditionException.isAjax())
            {
                PrintWriter printwriter;

                JSONObject jsonobject = new JSONObject();
                try
                {
                	printwriter = WebUtils.createPrintWriter(response);
                    jsonobject.put("exception", "failpass");
                    if(localRegistEditionException.getFUNC() != null)
                        jsonobject.put("func", localRegistEditionException.getFUNC().toString());
                    printwriter.write(jsonobject.toString());
                    printwriter.flush();
                    printwriter.close();
                }
                catch(JSONException jsonexception)
                {
                    jsonexception.printStackTrace();
                } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }
            FRContext.getLogger().errorWithServerLevel(localRegistEditionException.getMessage(), localRegistEditionException);
            ErrorHandlerHelper.getErrorHandler().error(request, response, localRegistEditionException);
        }catch (Exception localException) {
        	FRContext.getLogger().errorWithServerLevel(localException.getMessage(), localException);
            ErrorHandlerHelper.getErrorHandler().error(request, response, localException);
        }catch (OutOfMemoryError localOutOfMemoryError){
        	FRContext.getLogger().errorWithServerLevel(localOutOfMemoryError.getMessage(), localOutOfMemoryError);
            ErrorHandlerHelper.getErrorHandler().error(request, response, localOutOfMemoryError);
            System.gc();
        }finally{
        	if(gzipresponsewrapper != null)
                gzipresponsewrapper.finishResponse();
            Calculator.clearThreadSaved();
            FRLogManager.clearThreadSaved();
        }
		
		
		
		
		
		
		ModelAndView modelandview=new ModelAndView();
		modelandview.getModel().put("firstMenu","<b>粗体</b>");
		
		modelandview.getModel().put("menuItems","[{text:\"<b>粗体</b>\",handler:handlerMenu},"+
		                   "{text:\"<i>斜体<i>\",handler:handlerMenu},{text:\"<u>下划线</u>\",handler:handlerMenu}," +
		                   "{text:\"<font color=red>红色字体</font>\",handler:handlerMenu}]");		
		return modelandview;
	}

}
