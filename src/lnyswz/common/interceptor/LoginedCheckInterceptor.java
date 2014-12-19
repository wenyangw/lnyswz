package lnyswz.common.interceptor;

import javax.servlet.http.HttpServletResponse;

import lnyswz.jxc.bean.User;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoginedCheckInterceptor extends AbstractInterceptor {

	@Override
    public String intercept(ActionInvocation ai) throws Exception {
        String url = ServletActionContext.getRequest().getRequestURL().toString();
        HttpServletResponse response=ServletActionContext.getResponse();
        response.setHeader("Pragma","No-cache");          
        response.setHeader("Cache-Control","no-cache");   
        response.setHeader("Cache-Control", "no-store");   
        response.setDateHeader("Expires",0);
        User user = null;
        if (url.indexOf("login.action")!=-1 || url.indexOf("logout.action")!=-1){
            return ai.invoke();
        }else{
            if(!ServletActionContext.getRequest().isRequestedSessionIdValid()){
                return "tologin";
            }else{
                user = (User)ServletActionContext.getRequest().getSession().getAttribute("user");
                if (user == null){
                    return "tologin";
                }else{                    
                    return ai.invoke();
                }                
            }            
        }
    }
}
