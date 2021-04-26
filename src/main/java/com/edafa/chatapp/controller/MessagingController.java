package com.edafa.chatapp.controller;

import com.edafa.chatapp.dao.jpaRepository.UserJpaRepository;
import com.edafa.chatapp.entity.User;
import com.edafa.chatapp.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.util.*;


/**
 * Created by doaa1 on 4/25/2021.
 */
@RestController
@RequestMapping("/message")
public class MessagingController {
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    IUserService iUserService;

    private List<User> users = new ArrayList<>();
    private HashMap<String, Integer> userWordsCount;
    private HashMap<String, Integer> allWordsCount;
    private User loggedInUser;


    @PostConstruct
    public void init(){
        userWordsCount = new HashMap<>();
        allWordsCount = new HashMap<>();
        loggedInUser = null;
    }

    @GetMapping("/userLogin")
    public String login(@RequestParam String userName, @RequestParam String password) {

        if (userAuthentication(userName, password)) {
            return "Hello " + userName;
        } else {
            return "please Enter valid user name and password";
        }
    }

    private boolean userAuthentication(String userName, String password) {
        try {
            loggedInUser = iUserService.searchByUserNameAndPassword(userName, password);
            if (loggedInUser != null)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            List<User> users = userJpaRepository.findAll();
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    if (userName.equals(user.getUserName()) && password.equals(user.getPassword())) {
                        loggedInUser = user;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @PostMapping("/sendMessage")
    private String createFileAndWriteMessages(@RequestBody String message) {
        if (getLoggedInUser() == null)
            return "please login first";
        try {
            File userFile = new File(getLoggedInUser().getUserName() + "Chat.txt");
            if (userFile.createNewFile()) {
                System.out.println("File created: " + userFile.getName());
            } else {
                System.out.println("File already exists.");
            }

            FileWriter myWriter = new FileWriter(getLoggedInUser().getUserName() + "Chat.txt", true);
            myWriter.write(message + "\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");

            if (message.equals("Bye Bye")) {
                loggedInUser = null;
                return "Conversation ended";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Hello " + getLoggedInUser().getUserName();
    }

    @GetMapping("/countUserChat")
    public String countWordsForUserName(String userName) {

        if (loggedInUser == null)
            return "Please login first";
        if (userName == null)
            userName = loggedInUser.getUserName();
        userWordsCount = new HashMap<>();

        StringBuilder result = new StringBuilder("File Name : \t");
        result.append(userName + "Chat.txt \n");
        try {
            Scanner userFile = new Scanner(new File(userName + "Chat.txt"));

            while (userFile.hasNext()) {
                String word = userFile.next();
                Integer count = userWordsCount.get(word);
                if (count != null)
                    count++;
                else
                    count = 1;
                userWordsCount.put(word, count);
            }

            for (Map.Entry<String, Integer> entry : userWordsCount.entrySet()) {
                result.append(entry.getKey()).append(" :\t");
                result.append(entry.getValue()).append(" \n");
            }

        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "Not found a chat for this user";
        }
        return result.toString();
    }

    @GetMapping("/countAllUsersChats")
    public String countWordsForAllUsers() {

        if (loggedInUser == null)
            return "Please login first";

        allWordsCount = new HashMap<>();
        StringBuilder result = new StringBuilder();

        List<User> users = userJpaRepository.findAll();
        //loop on all users and send user name to countWordsForUserName
        for (User user : users) {
            countWordsForUserName(user.getUserName());
            for (Map.Entry<String, Integer> entry : userWordsCount.entrySet()) {
                Integer count = userWordsCount.get(entry.getKey());
                if (count != null)
                    count += allWordsCount.get(entry.getKey()) != null ? allWordsCount.get(entry.getKey()) : 0;
                else
                    count = 1;
                allWordsCount.put(entry.getKey(), count);
            }
        }

        for (Map.Entry<String, Integer> entry : allWordsCount.entrySet()) {
            result.append(entry.getKey()).append(" :\t");
            result.append(entry.getValue()).append(" \n");
        }

        if (result == null || (result != null && result.toString().trim().isEmpty()))
            result.append("Chats not found");
        return result.toString();
    }


    //    @RequestMapping(value = "/loginJson", method = RequestMethod.GET)
    private boolean userAuthenticationUsingJson() throws JSONException {
        User[] users;
        try {
            users = new ObjectMapper()
                    .readValue(new File("../../resources/data/loginData.json")
                            , User[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public HashMap<String, Integer> getUserWordsCount() {
        return userWordsCount;
    }

    public void setUserWordsCount(HashMap<String, Integer> userWordsCount) {
        this.userWordsCount = userWordsCount;
    }

    public HashMap<String, Integer> getAllWordsCount() {
        return allWordsCount;
    }

    public void setAllWordsCount(HashMap<String, Integer> allWordsCount) {
        this.allWordsCount = allWordsCount;
    }

    //
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
