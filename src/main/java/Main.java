import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.*;
import java.net.URL;


public class Main {
    public static final String URI = "https://api.nasa.gov/planetary/apod?api_key=IODTgE3TSeAmGeEcpbmBZcFRvP86mk2hwHfuNbGI";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
       saveImageFile();
    }

    public static CloseableHttpClient httpClient(){
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
    }

    public static CloseableHttpResponse getRequest(String uri) throws IOException {
        HttpGet request = new HttpGet(uri);
        return httpClient().execute(request);
    }

    public static ResponseNasa parseRequestToObject () throws IOException {
        return mapper.readValue(getRequest(URI).getEntity().getContent(), new TypeReference<ResponseNasa>() {});
    }

    public static void saveImageFile() throws IOException {
        ResponseNasa responseNasa = parseRequestToObject();
        String file = new File(new URL(responseNasa.getUrl()).getPath()).getName();
        try(FileOutputStream outputStream = new FileOutputStream(file)) {
            getRequest(responseNasa.getUrl()).getEntity().writeTo(outputStream);
        }
    }
}
