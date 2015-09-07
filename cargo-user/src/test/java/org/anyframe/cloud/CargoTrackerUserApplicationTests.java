package org.anyframe.cloud;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CargoTrackerUserApplication.class)
@WebAppConfiguration
public class CargoTrackerUserApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void encryption() {
		String password = "1234";
		String after = "";
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(password.getBytes()); 
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			after = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		System.out.println(after);
	}
}
