package com.example.demo;


//import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;

import com.example.demo.entity.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class Client4 {
    private static Scanner scanner = new Scanner(System.in);
    private static String currentUser = null;
    private static WebClient w = WebClient.create("http://localhost:8080");

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            if (currentUser == null) {
                showLoginPage();
            } else {
                Integer x = w.get().uri("/connect/" + currentUser).retrieve().bodyToMono(Integer.class).block();
            }
            showHomePage();
        }
        System.out.println("\nExiting forum application...");

    }
    //TODO: Anonymous/privilege/logout
    private static void showLoginPage() {
        System.out.println("Hello, my friend! Welcome to CS307Hub!");
        System.out.println("============\t\t\t============\t\t\t===================");
        System.out.println("|   Login  |\t\t\t| Register |\t\t\t| View as Tourist |");
        System.out.println("============\t\t\t============\t\t\t===================");
        boolean loop = true;
        while (loop) {
            String s = scanner.nextLine();
            HashSet<String> login = new HashSet<>();
            login.add("log in");
            login.add("login");
            login.add("register");
            login.add("view as tourist");
            login.add("viewastourist");
            if (login.contains(s.toLowerCase())) {
                loop = false;
                if (s.equalsIgnoreCase("register")) {
                    registerUser();
                } else if (s.equalsIgnoreCase("view as tourist") || s.equalsIgnoreCase("viewastourist")) {
                    showHomePage();
                }
            } else System.out.println("Invalid request. Please enter 'login' or 'register'");
        }
        loginUser();
    }

    private static void showHomePage() {
        System.out.println("============ Home Page ================");
        System.out.println("We have recommended some posts to you !");
        boolean exit = false;
        while (!exit) {
            showRandomPosts();
            System.out.println("try following commands:");
            System.out.println("\t\t========\t\t=========\t\t===================\t\t================\t\t========\t\t\t==========");
            System.out.println("\t\t| view |\t\t| renew |\t\t| personal space  |\t\t|  HOT POST !  |\t\t| exit |\t\t\t| logout |");
            System.out.println("\t\t========\t\t=========\t\t===================\t\t================\t\t========\t\t\t==========");
            String option = scanner.nextLine();
            switch (option.toLowerCase()) {
                case "hot post":
                    boolean e = false;
                    while (!e) {
                        System.out.println("================ HOT POST =================");
                        showHotPosts();
                        System.out.println("try following commands:");
                        System.out.println("\t\t========\t\t=========\t\t===================\t\t==========");
                        System.out.println("\t\t| view |\t\t| exit  |\t\t| personal space  |\t\t| return |");
                        System.out.println("\t\t========\t\t=========\t\t===================\t\t==========");
                        String opt = scanner.nextLine();
                        switch (opt.toLowerCase()) {
                            case "view":
                                System.out.println("enter the post ID you want to check:");
                                int post_id = scanner.nextInt();
                                String s = scanner.nextLine();
                                if (checkPostExist(post_id)) {
                                    viewPost(post_id);
                                } else {
                                    System.out.println("Post does not exist");
                                }
                                break;
                            case "return":
                                e = true;
                                break;
                            case "personal space":
                                viewPersonalSpace();
                                break;
                            case "exit":
                                System.out.println("Are you sure to exit CS307 Forum ? [y/n]");
                                boolean loop = true;
                                while (loop) {
                                    switch (scanner.nextLine()) {
                                        case "y":
                                            System.out.println("\nExiting forum application...");
                                            System.exit(1);
                                            break;
                                        case "n":
                                            loop = false;
                                            break;
                                        default:
                                            System.out.println("Please enter 'y' or 'n");
                                    }
                                }
                                break;
                            default:
                                System.out.println("Invalid input. Try other commands:");
                                break;
                        }
                    }
                    break;
                case "view":
                    System.out.println("enter the post ID you want to check:");
                    int post_id = scanner.nextInt();
                    String s = scanner.nextLine();
                    if (checkPostExist(post_id)) {
                        viewPost(post_id);
                    } else {
                        System.out.println("Post does not exist");
                    }

                    break;
                case "renew":
                    showRandomPosts();
                    break;
                case "personal space":
                    viewPersonalSpace();
                    break;
                case "exit":
                    System.out.println("Are you sure to exit CS307 Forum ? [y/n]");
                    boolean loop = true;
                    while (loop) {
                        switch (scanner.nextLine()) {
                            case "y":
                                System.out.println("\nExiting forum application...");
                                System.exit(1);
                                break;
                            case "n":
                                loop = false;
                                break;
                            default:
                                System.out.println("Please enter 'y' or 'n");
                        }
                    }
                    break;
                case "logout":
                    System.out.println("Are you sure to log out ? [y/n]");
                    boolean l = true;
                    while (l) {
                        switch (scanner.nextLine()) {
                            case "y":
                                System.out.println("\nLog out successfully");
                                l = false;
                                exit = true;
                                currentUser = null;
                                break;
                            case "n":
                                l = false;
                                break;
                            default:
                                System.out.println("Please enter 'y' or 'n");
                        }
                    }
                    logoutUser();
                    break;
                default:
                    System.out.println("Invalid input. Try other commands:");
                    break;
            }
        }
        showLoginPage();
    }


    //-----------------------------------------
    //注册用户insert into account(name, registration_time) values (account_name,current_timestamp,random_id);check help

    private static boolean checkAccountExist(String name) {
        return w.get().uri("/checkAccountExist/" + name).retrieve().bodyToMono(Boolean.class).block();
    }

    private static void registerUser() {
        System.out.println("\n========== User Registration ===========");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        if (checkAccountExist(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        signUp(username, password);
        System.out.println("User registration successful.");
    }

    private static void signUp(String userName, String password) {
        Random r = new Random();
        int id = r.nextInt(999999999);
        w.get().uri("/signUp/" + userName + "/" + password + "/" + id).retrieve().bodyToMono(Integer.class).block();
    }

    private static void checkRequest(String opcode, HashMap<String, String> guidance) {
        if (!guidance.containsKey(opcode)) {
            System.out.print("An invalid request. Press 'h' to get detailed operation guidance \n");
            return;
        } else {
            System.out.print(guidance.get(opcode));
        }
    }

    private static boolean checkPassword(String name, String password) {
        return w.get().uri("/checkPassword/" + name + "/" + password).retrieve().bodyToMono(Boolean.class).block();
    }

    private static void loginUser() {
        System.out.println("\n===== User Login =====");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        if (!checkAccountExist(username)) {
            System.out.println("Invalid username. Please register or try again.");
            return;
        }
        System.out.print("Enter your password: ");
        //a safety measure required: visual protection
        String password = scanner.nextLine();
        if (!checkPassword(username, password)) {
            System.out.println("Incorrect password. Please enter password again [You still have 1 chance]:");
            String s = scanner.nextLine();
            if (!checkPassword(username, s)) {
                System.out.println("Failed to log in. You are sent back to entrance");
                return;
            }
        }
        currentUser = username;
        System.out.println("Welcome back, " + currentUser + "~ New posts are coming ! ");
    }

    private static void logoutUser() {
        currentUser = null;
        System.out.println("Logged out Successfully. See you next time !");
    }

    private static void listBlock() {
        Flux<Block> l = w.get().uri("/listBlock/" + currentUser).retrieve().bodyToFlux(Block.class);
        List<Block> list = l.collectList().block();
        System.out.println("The users you have blocked:");
        for (Block block : list) {
            System.out.println(block.getBlocked().getName());
        }
        boolean loop = true;
        while (loop) {
            System.out.println("Enter 'u' to unblock a author you have blocked (print '#' to quit at any time) ");
            String s = scanner.nextLine();
            switch (s.toLowerCase()) {
                case "u":
                    System.out.println("Enter the name of author you want to unblock:");
                    String name = scanner.nextLine();
                    unblock(name);
                    break;
                case "#":
                    loop = false;
                    break;
                default:
                    System.out.println("Invalid input. Try other commands");
                    break;
            }
        }
    }

    private static void block(String user) {
        if(!checkAccountExist(user)){
            System.out.println("The user you try to block doesn't exist");
            return;
        }
        if (!checkBlock(currentUser, user)) {
            int x = w.get().uri("/block/" + currentUser + "/" + user).retrieve().bodyToMono(Integer.class).block();
            System.out.println("You block user " + user);
        } else {
            System.out.println("You've already blocked " + user + "before !");
        }
    }

    private static void unblock(String user) {
        if(!checkAccountExist(user)){
            System.out.println("The user you try to block doesn't exist");
            return;
        }
        if (checkBlock(currentUser, user)) {
            int x = w.get().uri("/unblock/" + currentUser + "/" + user).retrieve().bodyToMono(Integer.class).block();
            System.out.println("You have cancel the block to user " + user);
        } else {
            System.out.println("The user " + user + " is not on your blocking list!");
        }
    }

    private static boolean checkAction(int post_id, String type) {
        return w.get().uri("/checkAction/" + post_id + "/" + currentUser + "/" + type).retrieve().bodyToMono(Boolean.class).block();
    }

    private static boolean checkBlock(String block, String blocked) {
        Flux<Block> l = w.get().uri("/listBlock/" + block).retrieve().bodyToFlux(Block.class);
        List<Block> list = l.collectList().block();
        List<String> li = new ArrayList<>();
        for (Block b : list) {
            li.add(b.getBlocked().getName());
        }
        return (li.contains(blocked));
    }
    // TODO: listBlock/block // unblock/checkAction/showHotPosts/updatePhone/updatePassword/checkPrivilege

    private static void showHotPosts() {
        int limit = 5;
        int offset = 0;
        Flux<Post> l = w.get().uri("/showHotPosts/" + limit + "/" + offset).retrieve().bodyToFlux(Post.class);
        List<Post> list = l.collectList().block();
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<list.size();i++) {
            int postID = list.get(i).getId();
            String title = list.get(i).getTitle();
            String author = list.get(i).getAuthor().getAuthor();
            int heat = list.get(i).getViewcount();
            String s = "Post ID: "+ postID+"\nTitle: " + title +"\nHeat: " + heat+"\nRank: "+(i+1);
            sb.append(s+"\n");
            if (!list.get(i).getAnonymous()) {
                sb.append("\n" + "Author:" + author+"\n");
            } else {
                sb.append("\n## This is an anonymous post\n");
            }
        }
        System.out.println(sb);
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    private static boolean checkAuthor() {
        return w.get().uri("/checkAuthor/" + currentUser).retrieve().bodyToMono(Boolean.class).block();
    }

    private static void updatePhone() {
        System.out.println("Enter 'q' at any time to quit this mode");
        System.out.println("Enter your phone number:");
        String phone = scanner.nextLine();
        if (phone.equals("q")) return;
        boolean loop = true;
        while (loop) {
            if (phone.equals("q")) return;
            if (phone.length() == 11 && isInteger(phone)) {
                int x = w.get().uri("/updatePhone/" + currentUser + "/" + phone).retrieve().bodyToMono(Integer.class).block();
                System.out.println("You hava update your phone number successfully !");
                return;
            } else {
                System.out.println("Invalid phone number. Please try again");
                System.out.println("Enter 'q' at any time to quit this mode");
                System.out.println("Enter your phone number:");
                phone = scanner.nextLine();
            }
        }
    }

    private static void updatePassword() {
        boolean loop = true;
        while (loop) {
            System.out.println("Enter '#' at any time to quit this mode");
            System.out.println("[Reminder]: Your password should be longer than 5 character ");
            System.out.println("Enter your original password:");
            String ori = scanner.nextLine();
            if (ori.equals("#")) return;
            System.out.println("Enter your new password:");
            String n = scanner.nextLine();
            if (n.equals("#")) return;

            if (checkPassword(currentUser, ori)) {
                if (n.length() > 5) {
                    int x = w.get().uri("/updatePassword/" + currentUser + "/" + n).retrieve().bodyToMono(Integer.class).block();
                    System.out.println("You hava update your phone number successfully !");
                    return;
                } else {
                    System.out.println("[Reminder]: Your password should be longer than 5 character ");
                }
            } else {
                System.out.println("Wrong original password. Please try again");
            }
        }
    }

    //liked/shared/favorite
    private static void clickPost(String opcode, int post_id, String currentAccount) {
        opcode = opcode.toLowerCase(Locale.ROOT);
        String type;
        switch (opcode) {
            case "like":
                type = "like";
                viewCount(post_id);
                break;
            case "favorite":
                type = "favorite";
                viewCount(post_id);
                viewCount(post_id);
                break;
            case "share":
                type = "share";
                viewCount(post_id);
                viewCount(post_id);
                viewCount(post_id);
                break;
            default:
                System.out.print("An invalid request. Press 'h' to get detailed operation guidance \n");
                return;
        }

        w.get().uri("/clickPost/" + post_id + "/" + currentAccount + "/" + type).retrieve().bodyToMono(Integer.class).block();

    }

    private static boolean checkPostExist(int post_id) {
        return w.get().uri("/checkPostExist/" + post_id).retrieve().bodyToMono(Boolean.class).block();

    }
    private static void viewCount(int post_id){
        int x = w.get().uri("/viewCount/"+post_id).retrieve().bodyToMono(Integer.class).block();
    }
    private static void viewPost(int post_id) {
        boolean inViewPost = true;
        viewCount(post_id);
        while (inViewPost) {
            Post rs = w.get().uri("/viewPost/" + post_id).retrieve().bodyToMono(Post.class).block();
            String title = rs.getTitle();
            String content = rs.getContent();
            String posting_time = String.valueOf(rs.getPostingTime());
            String posting_city = rs.getPostingCity();
            String author = rs.getAuthor().getAuthor();
            if (checkBlock(author, currentUser)) {
                System.out.println("The author of the post has blocked you from watching his/her posts");
                return;
            }
            String s = "Title:" + title + "\n" +
                    "Content:" + content + "\n" + "Posting_time:" + posting_time + "\n" + "Posting_city:" + posting_city;
            StringBuilder sb = new StringBuilder();
            sb.append(s);
            if (!rs.getAnonymous()) {
                sb.append("\n" + "Author:" + author);
            } else {
                sb.append("\n## This is an anonymous post");
            }
            System.out.println(sb);

            System.out.println("You have finished the post. Show us your opinion about it ! ");
            System.out.println("\t\t=============\t\t\t============\t\t\t\t==============");
            System.out.println("\t\t|   like    |\t\t\t|   share  |\t\t\t\t|  favorite  |");
            System.out.println("\t\t=============\t\t\t============\t\t\t\t==============");
            System.out.println("\t\t============\t\t\t=================\t\t\t=========");
            System.out.println("\t\t|  comment |\t\t\t| view comments |\t\t\t| block |");
            System.out.println("\t\t============\t\t\t=================\t\t\t=========");
            System.out.println("\t\t=============\t\t\t============\t\t\t\t============");
            System.out.println("\t\t|    exit   |\t\t\t|  return  |\t\t\t\t|  follow  |");
            System.out.println("\t\t=============\t\t\t============\t\t\t\t============");

            String option = scanner.nextLine();
            switch (option.toLowerCase()) {
                case "block":
                    block(rs.getAuthor().getAuthor());
                    break;
                case "view comments":
                    viewComments(post_id);
                    break;
                case "like":
                    if(currentUser==null){
                        System.out.println("Login first to unlock more function");
                    }else {
                        if (!checkAction(post_id, "like")) {
                            clickPost("like", post_id, currentUser);
                            System.out.println("Received your like ! Thx for your support");
                        } else {
                            System.out.println("You've already liked this post");
                        }
                    }
                    break;
                case "share":
                    if(currentUser==null){
                        System.out.println("Login first to unlock more function");
                    }else {
                        if (!checkAction(post_id, "share")) {
                            clickPost("share", post_id, currentUser);
                            System.out.println("You have shared this post so more people can see that");
                        } else {
                            System.out.println("You've already shared this post");
                        }
                    }
                    break;
                case "favorite":
                    if(currentUser==null){
                        System.out.println("Login first to unlock more function");
                    }else {
                        if (!checkAction(post_id, "favorite")) {
                            clickPost("favorite", post_id, currentUser);
                            System.out.println("This post is added to your favorite package~");
                        } else {
                            System.out.println("This post is already in your favorite package~");
                        }
                    }
                    break;
                case "return":
                    return;
                case "exit":
                    System.out.println("Are you sure to exit CS307 Forum ? [y/n]");
                    boolean loop = true;
                    while (loop) {
                        switch (scanner.nextLine()) {
                            case "y":
                                System.out.println("\nExiting forum application...");
                                System.exit(1);
                                break;
                            case "n":
                                loop = false;
                                break;
                            default:
                                System.out.println("Please enter 'y' or 'n");
                        }
                    }
                    break;
                case "comment":
                    writeComment(post_id);
                    break;
                case "follow":
                    follow(author, currentUser);
                    break;
                default:
                    System.out.println("Invalid command. Try again");
                    break;
            }
        }

    }

    //insert into reply(reply_id, father_id, post_id,content, stars, author) values (reply_id,-1,post_id,content,stars,account_name);
    private static void follow(String author, String currentAccount) {
        if(currentUser==null){
            System.out.println("Login first to unlock more function");
            return;
        }
        if (isFollow(author)) {
            System.out.println("You have already followed " + author + "!");
        } else {
            w.get().uri("/follow/" + author + "/" + currentAccount).retrieve().bodyToMono(Integer.class).block();
            System.out.print("You have followed " + author + "\n");
        }
    }

    private static boolean isFollow(String author) {
        return w.get().uri("/isFollow/" + author + "/" + currentUser).retrieve().bodyToMono(Boolean.class).block();
    }

    private static void unfollow(String author, String currentAccount) {
        if (isFollow(author)) {
            w.get().uri("/unFollow/" + author + "/" + currentAccount).retrieve().bodyToMono(Integer.class).block();
            System.out.print("You have unfollowed " + author + "\n");
        } else {
            System.out.println(author + " is not on your follow list");
        }
    }

    private static void viewComments(int post_id) {
        boolean loop = true;
        while (loop) {
            Flux<Reply> l = w.get().uri("/viewComments/" + post_id).retrieve().bodyToFlux(Reply.class);
            List<Reply> list = l.collectList().block();
            HashMap<Integer, String> map = new HashMap<>();
            int reply_id = 0;
            for (Reply rs : list) {
                StringBuilder sb = new StringBuilder();
                sb.append("Author: ");
                sb.append(rs.getAuthor().getName());
                sb.append("\nContent: ");
                sb.append(rs.getContent());
                sb.append("\nreplyID: ");
                sb.append(rs.getId());
                sb.append("\n");
                map.put(reply_id, sb.toString());
                System.out.println(sb);
                boolean b = w.get().uri("/checkSR/" + reply_id).retrieve().bodyToMono(Boolean.class).block();
                if (b) {
                    System.out.println("\t##There are folded reply to this comment. Enter its ID to check for detail");
                }

            }
            System.out.println("try following commands:");
            System.out.println("\t\t===============\t\t\t=================\t\t\t============\t\t\t=========");
            System.out.println("\t\t|    reply    |\t\t\t| view comments |\t\t\t|  return  |\t\t\t| block |");
            System.out.println("\t\t===============\t\t\t=================\t\t\t============\t\t\t=========");
            String option = scanner.nextLine();
            switch (option) {
                case "block":
                    boolean lo = true;
                    while (lo) {
                        System.out.println("Enter the name of user you want to block:");
                        String blocked = scanner.nextLine();
                        block(blocked);
                        System.out.println("press 'c' to continue or press 'q' to quit block mode");
                        boolean lop = true;
                        while (lop) {
                            switch (scanner.nextLine().toLowerCase()) {
                                case "c":
                                    loop = false;
                                    break;
                                case "q":
                                    loop = false;
                                    lo = false;
                                    break;
                                default:
                                    System.out.println("Please enter 'c' or 'q");
                                    break;
                            }
                        }
                    }
                case "return":
                    return;
                case "reply":
                    System.out.println("print the ID of comment you want to reply to: ");
                    int id = scanner.nextInt();
                    String s = scanner.nextLine();
                    replyToComment(post_id, id);
                    break;
                case "view comments":
                    System.out.println("Enter the ID of comment you want to check or press -1 to quit comment mode");
                    reply_id = scanner.nextInt();
                    String ss = scanner.nextLine();
                    if (reply_id == -1) {
                        return;
                    }
                    if (!map.containsKey(reply_id)) {
                        System.out.println("The comment you check doesn't exist or belong to this post");
                    } else if (checkSR(reply_id)) {
                        System.out.println(map.get(reply_id));
                        viewSR(post_id, reply_id);
                    } else {
                        System.out.println("The comment you check  has no folded content");
                    }
                    break;
                default:
                    System.out.println("Invalid input. You can try 'reply' or 'view comments' ");
                    break;
            }


        }
    }

    //Done
    private static void viewSR(int post_id, int reply_id) {
        boolean loop = true;
        while (loop) {
            Flux<Reply> l = w.get().uri("/viewSR/" + reply_id).retrieve().bodyToFlux(Reply.class);
            List<Reply> list = l.collectList().block();
            for (Reply rs : list) {
                String content = rs.getContent();
                int r_id = rs.getId();
                String author = rs.getAuthor().getName();
                System.out.println("\t===> Author: " + author + "\n" + "Content: " + content + "\n" + "replyID: " + r_id + "\n");
            }
        }


        System.out.println("Enter the ID of comment you want to check or press -1 to quit reply mode");
        int sr_id = scanner.nextInt();
        String s = scanner.nextLine();
        if (sr_id == -1) {
            return;
        }
        if (checkSR(reply_id)) {
            replyToComment(post_id, reply_id);
        } else {
            System.out.println("The comment you want to reply doesn't exist");
        }

    }


    private static boolean checkSR(int reply_id) {
        return w.get().uri("/checkSR/" + reply_id).retrieve().bodyToMono(Boolean.class).block();
    }

    private static int checkPostSize() {
        return w.get().uri("/checkPostSize").retrieve().bodyToMono(Integer.class).block();
    }

    private static void post(int post_id, String title, String content, String posting_city, String account_name) {
        w.get().uri("/post/" + post_id + "/" + title + "/" + content + "/" + posting_city + "/" + currentUser + "/false").retrieve().bodyToMono(Integer.class).block();
    }

    private static void anonymousPost(int post_id, String title, String content, String posting_city, String account_name) {
        w.get().uri("/post/" + post_id + "/" + title + "/" + content + "/" + posting_city + "/" + currentUser + "/true").retrieve().bodyToMono(Integer.class).block();
    }

    private static void writePost() {
        if (currentUser == null) {
            System.out.println("You must be logged in to write a post.");
            return;
        }
        System.out.println("\n============ Write a Post ============");
        System.out.println("Enter 'q' at any time to quit the author mode");
        System.out.print("Enter the post title: ");
        String title = scanner.nextLine();
        if (title.equals("q")) return;
        System.out.print("Enter the post content: ");
        String content = scanner.nextLine();
        if (content.equals("q")) return;
        System.out.println("Enter your posting city: ");
        String city = scanner.nextLine();
        if (city.equals("q")) return;
        System.out.println("Do you want to post anonymously? An anonymous post won't display the name of author[y/n]");
        boolean l = true;
        boolean isAnonymous = true;
        while (l) {
            switch (scanner.nextLine()) {
                case "q":
                    return;
                case "y":
                    l = false;
                    isAnonymous = true;
                    break;
                case "n":
                    isAnonymous = false;
                    l = false;
                    break;
                default:
                    System.out.println("Please enter 'y' or 'n' where 'y' stands for 'yes' and 'n' for 'no' ");
            }
        }
        if (isAnonymous) {
            anonymousPost(checkPostSize() + 1, title, content, city, currentUser);
        } else {
            post(checkPostSize() + 1, currentUser, title, content, city);
        }
        System.out.println("Post created successfully. ");
    }

    private static void writeComment(int post_id) {
        if (currentUser == null) {
            System.out.println("You must be logged in to write a comment.");
            return;
        }
        while (true) {
            System.out.println("\n===== Write a Comment =====");
            System.out.println("press 'q' to exit comment mode at any time");
            System.out.println("Enter your comment: ");
            String commentText = scanner.nextLine();
            if (commentText.equals("q")) {
                return;
            }
            int reply_id = checkReplySize() + 1;
            w.get().uri("/writeComment/" + reply_id + "/" + -1 + "/" + post_id + "/" + commentText + "/" + currentUser).retrieve().bodyToMono(Integer.class).block();
            System.out.println("Comment added successfully.");
        }
    }

    private static void replyToPost(int post_id, String content, String currentAccount) {
        int reply_id = checkReplySize() + 1;
        w.get().uri("/replyToPost/" + reply_id + "/" + post_id + "/" + content + "/" + currentAccount).retrieve().bodyToMono(Integer.class).block();
    }

    private static int checkReplySize() {
        return w.get().uri("/checkReplySize").retrieve().bodyToMono(Integer.class).block();
    }


    private static void replyToComment(int postId, int commentId) {
        if (currentUser == null) {
            System.out.println("You must be logged in to reply to a comment.");
            return;
        }
        System.out.println("\n===== Reply to a Comment =====");
        System.out.print("Enter your reply: ");
        String replyText = scanner.nextLine();
        int num = checkReplySize() + 1;
        w.get().uri("/replyToComment/" + num + "/" + commentId + "/" + postId + "/" + replyText + "/" + currentUser).retrieve().bodyToMono(Integer.class).block();
        System.out.println("Reply added successfully.");
    }

    //check user personal space  posts/reply/followers
    private static void viewPersonalSpace() {
        if(currentUser==null){
            System.out.println("Login first to unlock more function");
            return;
        }
        boolean ps = true;
        while (ps) {
            System.out.println("===========" + currentUser + "'s Personal Space=============");
            System.out.println("  Write a new post                  press [W]");
            System.out.println("  View your posts                   press [P]");
            System.out.println("  View my previous comments         press [C]");
            System.out.println("  View who I have followed          press [F]");
            System.out.println("  View posts that you liked         press [L]");
            System.out.println("  View posts that you favorite      press [V]");
            System.out.println("  View posts that you shared        press [S]");
            System.out.println("  Return to home page               press [R]");
            System.out.println("  View who I have blocked           press [B]");
            System.out.println("  Change my password                press [A]");
            System.out.println("  Change my phone number            press [N]");
            System.out.println("  Exit forum                        press [E]");

            String option = scanner.nextLine();
            switch (option.toLowerCase()) {
                case "n":
                    if (checkAuthor()) {
                        updatePhone();
                    } else {
                        System.out.println("You are not an author yet. Write your first post to unlock more function");
                    }
                    break;
                case "a":
                    updatePassword();
                    break;
                case "b":
                    listBlock();
                    break;
                case "v":
                    selectAction("favorite", currentUser);
                    break;
                case "s":
                    selectAction("share", currentUser);
                    break;
                case "l":
                    selectAction("like", currentUser);
                    break;
                case "w":
                    writePost();
                    break;
                case "p":
                    selectOwnPosts(currentUser);
                    break;
                case "c":
                    selectOwnReply(currentUser);
                    break;
                case "f":
                    selectFollowList(currentUser);
                    break;
                case "r":
                    ps = false;
                    break;
                case "e":
                    System.out.println("Are you sure to exit CS307 Forum ? [y/n]");
                    boolean loop = true;
                    while (loop) {
                        switch (scanner.nextLine()) {
                            case "y":
                                System.out.println("\nExiting forum application...");
                                System.exit(1);
                                break;
                            case "n":
                                loop = false;
                                break;
                            default:
                                System.out.println("Please enter 'y' or 'n");
                        }
                    }
                default:
                    System.out.println("Invalid request. Please follow the guidance");
            }
        }
    }

    private static void selectOwnPosts(String account_name) {
        Flux<Post> l = w.get().uri("/selectOwnPosts/" + account_name).retrieve().bodyToFlux(Post.class);
        List<Post> list = l.collectList().block();
        for (Post p : list) {
            int id = p.getId();
            String title = p.getTitle();
            String content = p.getContent();
            String posting_time = String.valueOf(p.getPostingTime());
            String posting_city = p.getPostingCity();
            String author = p.getAuthor().getAuthor();
            System.out.println("Post ID: " + id + "\n" + "Title:" + title + "\n" +
                    "Content:" + content + "\n" + "Posting_time:" + posting_time + "\n" + "Posting_city:" + posting_city + "\n" + "Author:" + author + "\n ");
        }
    }

    private static void selectOwnReply(String account_name) {
        Flux<Reply> l = w.get().uri("/selectOwnReply/" + account_name).retrieve().bodyToFlux(Reply.class);
        List<Reply> list = l.collectList().block();
        for (Reply r : list) {
            String r1_content = r.getContent();
            String replyWhat = r.getPost().getContent();
            String author = r.getPost().getAuthor().getAuthor();
            int replyPost_id = r.getPost().getId();
            System.out.println("Reply content:" + r1_content + "\t" + "Content be replied:" + replyWhat + "\t" + "Reply author:" + author + "\t" + "Post be replied:" + replyPost_id + "\t");
        }

    }

    private static void selectFollowList(String account_name) {
        StringBuilder sb = new StringBuilder();
        Flux<Follower> l = w.get().uri("/selectFollowList/" + account_name).retrieve().bodyToFlux(Follower.class);
        List<Follower> list = l.collectList().block();

        sb.append("Author you followed: \n");
        for (Follower rs : list) {
            sb.append(rs.getAuthor().getAuthor()).append("\n");
        }
        System.out.println(sb);
        boolean loop = true;
        while (loop) {
            System.out.println("Enter 'u' to cancel a author you have followed (print 'q' to quit at any time) ");
            String s = scanner.nextLine();
            switch (s.toLowerCase()) {
                case "u":
                    System.out.println("Enter the name of author you want to unfollow:");
                    String name = scanner.nextLine();
                    unfollow(name, currentUser);
                    break;
                case "q":
                    loop = false;
                    break;
                default:
                    System.out.println("Invalid input. Try other commands");
                    break;
            }
        }


    }

    private static void showRandomPosts() {
        System.out.println("\n===== We've recommended some posts to you !======");
        Random r = new Random();
        int limit = 5;
        int offset = r.nextInt(checkPostSize() - limit);

        Flux<Post> l = w.get().uri("/showRandomPosts/" + limit + "/" + offset).retrieve().bodyToFlux(Post.class);
        List<Post> list = l.collectList().block();
        for (Post rs : list) {
            int postID = rs.getId();
            String title = rs.getTitle();

            String author = rs.getAuthor().getAuthor();
            System.out.println("Post ID:" + postID + "\n" + "Title:" + title +
                    "\n" + "Author:" + author);
        }

    }

    private static void selectAction(String action, String account_name) {
        Flux<Action> l = w.get().uri("/selectAction/" + account_name + "/" + action).retrieve().bodyToFlux(Action.class);
        List<Action> list = l.collectList().block();
        System.out.println("The posts that you " + action + " :");
        for (Action rs : list) {
            int post_id = rs.getId().getPostId();
            System.out.println("post id: " + post_id);
        }

    }
    private static void search(String s){
        String[] list = s.split(" ");
        StringBuilder b = new StringBuilder();
        b.append("%( "+list[0]+" ");
        for(int i=1;i<list.length-1;i++){
            b.append("| "+list[i]+" ");
        }
        b.append(")%");
        Flux<Post> l = w.get().uri("/search/"+ b).retrieve().bodyToFlux(Post.class);
        List<Post> li = l.collectList().block();
        StringBuilder sb = new StringBuilder();
        for(Post rs:li){
            String title = rs.getTitle();
            String content = rs.getContent();
            String posting_time = String.valueOf(rs.getPostingTime());
            String posting_city = rs.getPostingCity();
            String author = rs.getAuthor().getAuthor();
            if (checkBlock(author, currentUser)) {
                System.out.println("The author of the post has blocked you from watching his/her posts");
                return;
            }
            String ss = "Title:" + title + "\n" +
                    "Content:" + content + "\n" + "Posting_time:" + posting_time + "\n" + "Posting_city:" + posting_city;

            sb.append(ss);
            if (!rs.getAnonymous()) {
                sb.append("\n" + "Author:" + author);
            } else {
                sb.append("\n## This is an anonymous post");
            }
        }

    }


}

