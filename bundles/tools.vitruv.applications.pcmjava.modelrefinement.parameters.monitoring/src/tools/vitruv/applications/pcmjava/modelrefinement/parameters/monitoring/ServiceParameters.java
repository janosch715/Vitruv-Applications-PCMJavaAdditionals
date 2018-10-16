package tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring;

public class ServiceParameters {
	
	public static ServiceParameters EMPTY = new ServiceParameters();
	
	private StringBuilder stringBuilder;
	
	public ServiceParameters() {
		this.stringBuilder = new StringBuilder();
	}
	
	public void addInt(String name, int value) {
		this.stringBuilder.append("\"").append(name).append("\":").append(value).append(",");
	}
	
	public void addFloat(String name, double value) {
		this.stringBuilder.append("\"").append(name).append("\":").append(value).append(",");
	}
	
	public String toString() {
		return "{" + this.stringBuilder.toString() + "}";
	}
}
