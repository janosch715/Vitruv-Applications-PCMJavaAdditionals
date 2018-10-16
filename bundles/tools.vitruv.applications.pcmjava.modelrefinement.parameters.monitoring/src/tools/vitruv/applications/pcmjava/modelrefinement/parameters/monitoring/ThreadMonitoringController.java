package tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.timer.ITimeSource;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.BranchRecord;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.LoopRecord;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.ResponseTimeRecord;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.ServiceCallRecord;

public class ThreadMonitoringController {
	
	private static final int INITIAL_SERVICE_DEPTH_COUNT = 10;
	
	private static final IMonitoringController MONITORING_CONTROLLER =
			MonitoringController.getInstance();
	
	private static final ITimeSource TIME_SOURCE = 
			MONITORING_CONTROLLER.getTimeSource();
	
	private static final ThreadLocal<ThreadMonitoringController> CONTROLLER = 
			ThreadLocal.withInitial(() -> new ThreadMonitoringController(Thread.currentThread().getId(), INITIAL_SERVICE_DEPTH_COUNT));
	
	private static volatile String sessionId;
	
	public static ThreadMonitoringController getInstance() {
		return CONTROLLER.get();
	}
	
	public static String getSessionId() {
		return sessionId;
	}

	public static void setSessionId(String id) {
		sessionId = id;
	}
	
	private long threadId;
	private List<ServiceMonitoringController> serviceControllers;
	private int currentServiceIndex;
	private ServiceMonitoringController currentServiceController;
	
	private ThreadMonitoringController(long threadId, int initialServiceDepthCount) {
		this.threadId = threadId;
		this.serviceControllers = new ArrayList<ServiceMonitoringController>(initialServiceDepthCount);
		for (int i = 0; i < initialServiceDepthCount; i++) {
			this.serviceControllers.add(new ServiceMonitoringController());
		}
		this.currentServiceIndex = -1;
		this.currentServiceController = null;
	}
	
	public void enterService(String serviceId) {
		this.enterService(serviceId, ServiceParameters.EMPTY);
	}
	
	public void enterService(String serviceId, ServiceParameters serviceParameters) {
		String currentServiceExecutionId = null;
		String currentCallerId = null;
		if (this.currentServiceController != null) {
			currentServiceExecutionId = this.currentServiceController.getServiceExecutionId();
			currentCallerId = this.currentServiceController.getCurrentCallerId();
		}
		
		this.currentServiceIndex += 1;
		ServiceMonitoringController newService;
		if (this.currentServiceIndex >= this.serviceControllers.size()) {
			newService = new ServiceMonitoringController();
			this.serviceControllers.add(new ServiceMonitoringController());
		} else {
			newService = this.serviceControllers.get(this.currentServiceIndex);
		}
		
		newService.enterService(serviceId, this.threadId, sessionId, serviceParameters, currentCallerId, currentServiceExecutionId);
		
		this.currentServiceController = newService;
	}
	
	public void exitService() {
		this.currentServiceController.exitService();
		this.currentServiceIndex -= 1;
		if (this.currentServiceIndex >= 0) {
			this.currentServiceController = 
					this.serviceControllers.get(this.currentServiceIndex);
		} else {
			this.currentServiceController = null;
		}
	}
	
	public void setCurrentCallerId(String currentCallerId) {
		this.currentServiceController.setCurrentCallerId(currentCallerId);
	}
	
	public void logBranchExecution(String branchId, String executedBranchId) {
		this.currentServiceController.logBranchExecution(branchId, executedBranchId);
	}
	
	public void logLoopIterationCount(String loopId, long loopIterationCount) {
		this.currentServiceController.logLoopIterationCount(loopId, loopIterationCount);
	}
	
	public void logResponseTime(String internalActionId, String resourceId, long startTime) {
		this.currentServiceController.logResponseTime(internalActionId, resourceId, startTime);
	}
	
	public long getTime() {
		return TIME_SOURCE.getTime();
	}
	
	private static class ServiceMonitoringController {
		
		private long serviceStartTime;
		
		private String serviceId;
		private long threadId;
		private ServiceParameters serviceParameters;
		private String serviceExecutionId;
		private String sessionId;
		private String callerServiceExecutionId;
		private String callerId;
		private String currentCallerId;
		
		public void enterService(
				String serviceId, 
				long threadId, 
				String sessionId, 
				ServiceParameters serviceParameters, 
				String callerId, 
				String callerServiceExecutionId) {
			this.serviceId = serviceId;
			this.threadId = threadId;
			this.sessionId = sessionId;
			this.serviceParameters = serviceParameters;
			this.callerServiceExecutionId = callerServiceExecutionId;
			this.callerId = callerId;
			this.serviceStartTime = TIME_SOURCE.getTime();
			this.serviceExecutionId = UUID.randomUUID().toString();
			this.currentCallerId = null;
		}
		
		public void exitService() {
			final long stopTime = TIME_SOURCE.getTime();
			
			ServiceCallRecord e = new ServiceCallRecord(
					this.sessionId,
					this.serviceExecutionId,
					this.serviceId,
					this.serviceParameters.toString(),
					this.callerServiceExecutionId,
					this.callerId,
					this.serviceStartTime,
					stopTime);
			
			MONITORING_CONTROLLER.newMonitoringRecord(e);
		}
		
		public String getServiceExecutionId() {
			return this.serviceExecutionId;
		}
		
		public void setCurrentCallerId(String currentCallerId) {
			this.currentCallerId = currentCallerId;
		}
		
		public String getCurrentCallerId() {
			return this.currentCallerId;
		}
		
		public void logBranchExecution(String branchId, String executedBranchId) {
			BranchRecord record = new BranchRecord(
					this.sessionId, 
					this.serviceExecutionId, 
					branchId,
					executedBranchId);
			
			MONITORING_CONTROLLER.newMonitoringRecord(record);
		}
		
		public void logLoopIterationCount(String loopId, long loopIterationCount) {
			LoopRecord record = new LoopRecord(
					this.sessionId, 
					this.serviceExecutionId, 
					loopId,
					loopIterationCount);
			
			MONITORING_CONTROLLER.newMonitoringRecord(record);
		}
		
		public void logResponseTime(String internalActionId, String resourceId, long startTime) {
			long currentTime = TIME_SOURCE.getTime();
			
			ResponseTimeRecord record = new ResponseTimeRecord(
					this.sessionId, 
					this.serviceExecutionId, 
					internalActionId,
					resourceId,
					startTime,
					currentTime);
			
			MONITORING_CONTROLLER.newMonitoringRecord(record);
		}
	}
}
