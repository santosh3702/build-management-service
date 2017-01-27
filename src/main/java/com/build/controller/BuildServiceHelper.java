package com.build.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.build.model.BuildServiceRequest;
import com.build.model.BuildServiceResponse;



@Component
public class BuildServiceHelper {

	private final String LOCALREPO = "D:\\LocalRepo\\";
	
	private static File getEmptyFile(File file) throws IOException {
		FileOutputStream writer = new FileOutputStream(file);
		writer.write(("").getBytes());
		writer.close();
		return file;
	}

	public BuildServiceResponse cloneAndBuild(BuildServiceRequest request) {
		BuildServiceResponse buildServiceResponse = new BuildServiceResponse();
		File path = null;
		try {
			path = getLocalFolder(request.getProjectName());
			Git.cloneRepository()
					.setCredentialsProvider(
							new UsernamePasswordCredentialsProvider(request.getUser(), request.getPassword()))
					.setURI(request.getUrl()).setDirectory(path).setBranch(request.getBranch()).call().close();
			
			buildGradleProject(LOCALREPO+request.getProjectName());
			createBuildServiceResponse(LOCALREPO+request.getProjectName(),buildServiceResponse);
		} catch (Exception e) {
			e.printStackTrace();
			buildServiceResponse.setStatus("FAIL");
			buildServiceResponse.setStatus(e.getMessage());
		}
		System.out.println("buildServiceResponse "+buildServiceResponse);
		return buildServiceResponse;
	}

	private void createBuildServiceResponse(String path, BuildServiceResponse buildServiceResponse) throws FileNotFoundException, IOException {
		//File logFile = ResourceUtils.getFile(path:log.txt");"
				File logFile = new File(path+"\\log.txt");
				/*if(!logFile.exists()){
					logFile.createNewFile();	
				}*/
		boolean buildStatus = false;
		String buildLog = "";
		try {
			Scanner scanner = new Scanner(logFile);
		    while (scanner.hasNextLine()) {
		        String line = scanner.nextLine();
		        buildLog += line;
		        if(line.equals("BUILD SUCCESSFUL")) { 
		            System.out.println("BUILD SUCCESSFUL");
		            buildStatus = true;		       
		        }
		    }
		    scanner.close();
		} catch(FileNotFoundException e) { 
		   e.printStackTrace();
		}
		if(buildStatus){
			buildServiceResponse.setStatus("PASS");
		}else{
			buildServiceResponse.setStatus("FAIL");
		}
		buildServiceResponse.setLog(buildLog);
		
	}

	private void buildGradleProject(String path) throws Exception {
		System.out.println("buildProject build process start"+path);
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec("cmd /c  cd "+path+" && gradle build >>log.txt");
		//writeLogs(process);
		process.waitFor();
		System.out.println("BUILD DONE!");
		process.destroy();
		
	}

	private void writeLogs(Process process) throws Exception, IOException {
		File logFile = getEmptyFile(ResourceUtils.getFile("classpath:log.txt"));
		System.out.println(logFile.getPath()+" "+logFile.exists());
		InputStream inputStream = process.getInputStream();
		FileOutputStream fileOutputStream = new FileOutputStream(logFile);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(isr);
		try {
			while (br.read()!= -1) {
				char str = (char) br.read();
				System.out.println(str);
				/*if(str!= null){
					System.out.println(str);
	        		bw.write(str);
					bw.newLine();
				}*/
				
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		bw.close();
		isr.close();
		System.out.println("writeLogs Complete ====");
	}

	private File getLocalFolder(String projectName) throws IOException {
		File file = new File(LOCALREPO+projectName);
		//File file = new File("D:\\LocalRepo");
		if (file.exists()) {
			FileUtils.cleanDirectory(file);
		}
		if (file.mkdir()) {
			System.out.println("Directory is created!");
		} else {
			System.out.println("Directory exhist!");
		}

		return file;
	}

}
