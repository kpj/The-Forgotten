import java.io.IOException;


public class eclipse_run_jar {
	public eclipse_run_jar() {
		try {
			System.out.println("Creating jar file...");
			Runtime.getRuntime().exec("./create_jar.sh").waitFor();
			System.out.println("Executing jar file...");
			Runtime.getRuntime().exec("java -jar The-Forgotten.jar").waitFor();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args) {
		eclipse_run_jar startup = new eclipse_run_jar();
    }
}
