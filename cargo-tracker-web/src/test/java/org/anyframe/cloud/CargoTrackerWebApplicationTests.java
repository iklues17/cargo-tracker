package org.anyframe.cloud;

import org.anyframe.web.CargoTrackerWebApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CargoTrackerWebApplication.class)
@WebAppConfiguration
public class CargoTrackerWebApplicationTests {

	@Test
	public void contextLoads() {
	}

}
