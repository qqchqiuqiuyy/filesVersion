package cn.bb;

import net.sf.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import static cn.bb.common.Util.PATH;

@SpringBootApplication
@EnableAutoConfiguration
public class FileversionApplication {

	public static void main(String[] args) {
		if (args.length > 0) {
			PATH = args[0];
		}
		SpringApplication.run(FileversionApplication.class, args);
	}
	@Bean
	@Scope("prototype")
	public JSONObject jsonObject(){
		return new JSONObject();
	}
}
