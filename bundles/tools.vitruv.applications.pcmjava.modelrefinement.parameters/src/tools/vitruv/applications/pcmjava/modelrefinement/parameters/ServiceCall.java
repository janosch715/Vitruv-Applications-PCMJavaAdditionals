package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

public interface ServiceCall {

	ServiceParameters getParameters();
	
	String getServiceExecutionId();
	
	String getServiceId();
	
	String getCallerServiceExecutionId();
	
	String getCallerId();
	
	long getEntryTime();
	
	long getExitTime();
	
	long getResponseTime();
	
	double getResponseTimeSeconds();
}
