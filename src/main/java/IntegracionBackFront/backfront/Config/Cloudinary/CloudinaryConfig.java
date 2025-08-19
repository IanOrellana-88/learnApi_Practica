package IntegracionBackFront.backfront.Config.Cloudinary;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig
{

    //variables
    private String cloudName;
    private String apiKey;
    private String apiSecret;

    @Bean
    public  Cloudinary cloudinary()
    {
        //Creamos objeto tipo Dotenv
        Dotenv dotenv = Dotenv.load();

        //Creamos un mapa para guardar los valores



        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", dotenv.get("CLOUDINARY_CLOUD_NAME"));
        config.put("api_key", dotenv.get("CLOUDINARY_API_KEY"));
        config.put("api_secret", dotenv.get("CLOUDINARY_API_SECRET"));

        //retornamos un elemento
        return new Cloudinary(config);

    }
}
