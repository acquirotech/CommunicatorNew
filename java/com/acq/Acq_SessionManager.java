package com.acq;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.acq.web.dto.impl.ServiceDto;

public class Acq_SessionManager {
	
	
	private static final SessionFactory sessionFactory = buildSessionFactory();
	 
    private static SessionFactory buildSessionFactory() {
        try {                  	
           return new AnnotationConfiguration().configure().buildSessionFactory(); 
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    } 
    
    
    public static void shutdown() {
    	// Close caches and connection pools
    	getSessionFactory().close();
    }
    
    public static ServiceDto<String> isSessionRunning(HttpServletRequest request,String userId){
		ServiceDto response = new ServiceDto();
		try {
			HttpSession session = request.getSession();
				if ((String) session.getAttribute("userid") == null) {
				response.setMessage(Acq_Status_Definations.InvalidSession.getDescription());
				response.setStatus(Acq_Status_Definations.InvalidSession.getId());
			} else {						
				String userIdd = (String) session.getAttribute("userid");
				if (userId.equals(userIdd)) {
					response.setMessage(Acq_Status_Definations.OK.getDescription());
					response.setStatus(Acq_Status_Definations.OK.getId());
				}else{
					response.setMessage(Acq_Status_Definations.InvalidSession.getDescription());
					response.setStatus(Acq_Status_Definations.InvalidSession.getId());
				}
			}
		} catch (Exception e) {
			System.out.println("error is generating............. ");
			response.setMessage(Acq_Status_Definations.UnexpectedServerError.getDescription());
			response.setStatus(Acq_Status_Definations.UnexpectedServerError.getId());
		}
		return response;
	}
    
}

