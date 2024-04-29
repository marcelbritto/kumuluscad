package br.com.marcelbritto.kumuluscad.producer;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.logging.log4j.*;

/** 
 * Logging producer for injectable log4j logger 
 * 
 * @author marcelbritto 
 */  

public class LoggerProducer {  
   /** 
    * @param injectionPoint 
    * @return logger 
    */  
    @Produces  
    public Logger produceLogger(InjectionPoint injectionPoint) {  
        return LogManager.getLogger(injectionPoint.getMember().getDeclaringClass().getName());  
    }  
}  

