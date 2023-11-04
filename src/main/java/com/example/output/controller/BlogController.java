package com.example.output.controller;

import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Controller
public class BlogController {

    private final String apiKey = "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"; // Replace with your actual API key
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String showTopicForm() {
        return "topicForm";
    }

    @PostMapping("/generateBlog")
    public String generateBlog(String topic, Model model) {
        String blogContent = getBlogFromOpenAI(topic);
        model.addAttribute("blogContent", blogContent);
        return "blog";
    }

    private String getBlogFromOpenAI(String topic) {
        String openaiEndpoint = "https://api.openai.com/v1/engines/davinci/completions";
        String prompt = "Write a blog about " + topic;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);


        String requestBody = "{\"prompt\": \"" + prompt + "\", \"max_tokens\": 200}";

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

//        try {
//            ResponseEntity<String> response = restTemplate.exchange(openaiEndpoint, HttpMethod.POST, request, String.class);
//            if (response.getStatusCode().is2xxSuccessful()) {
//                return response.getBody(); // Return the blog content from the OpenAI API response
//            } else {
//                return "Error occurred while retrieving the blog content";
//            }
//        } catch (HttpClientErrorException.Unauthorized unauthorized) {
//            return "Unauthorized access to the OpenAI API. Please check your API key.";
//        } catch (Exception e) {
//            return "Error occurred while communicating with the OpenAI API.";
//        }
//    }
        try {
            ResponseEntity<String> response = restTemplate.exchange(openaiEndpoint, HttpMethod.POST, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody(); // Return the blog content from the OpenAI API response
            } else {
                return "Error occurred while retrieving the blog content. Status code: " + response.getStatusCodeValue();
            }
        } catch (HttpClientErrorException.Unauthorized unauthorized) {
            return "Unauthorized access to the OpenAI API. Please check your API key.";
        } catch (Exception e) {
            return "Error occurred while communicating with the OpenAI API: " + e.getMessage();
        }
    }
}


