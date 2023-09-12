package com.example.demo;


//import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;

import com.example.demo.entity.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


import java.sql.SQLException;
import java.util.*;

public class Client {
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
                showHomePage();
            }
        }
        System.out.println("\nExiting forum application...");

    }

    private static void showLoginPage() {
        System.out.println("Hello, my friend! Welcome to CS307Hub!");
        System.out.println("============\t\t\t============");
        System.out.println("|   Login  |\t\t\t| Register |");
        System.out.println("============\t\t\t============");
        boolean loop = true;
        while (loop) {
            String s = scanner.nextLine();
            HashSet<String> login = new HashSet<>();
            login.add("log in");
            login.add("login");
            login.add("register");
            if (login.contains(s.toLowerCase())) {
                loop = false;
                if (s.toLowerCase().equals("register")) {
                    registerUser();
                }
            } else System.out.println("Invalid request. Please enter 'login' or 'register.");
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
            System.out.println("\t\t========\t\t=========\t\t===================\t\t========\t\t\t==========");
            System.out.println("\t\t| view |\t\t| renew |\t\t| personal space  |\t\t| exit |\t\t\t| logout |");
            System.out.println("\t\t========\t\t=========\t\t===================\t\t========\t\t\t==========");
            String option = scanner.nextLine();
            switch (option) {
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
                    showRandomPosts();
                    System.out.println("try following commands:");
                    System.out.println("\t\t========\t\t=========\t\t==================\t\t========\t\t\t============");
                    System.out.println("\t\t| view |\t\t| renew |\t\t| personal space |\t\t| exit |\t\t\t|  logout  |");
                    System.out.println("\t\t========\t\t=========\t\t==================\t\t========\t\t\t============");

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

    //liked/shared/favorite
    private static void clickPost(String opcode, int post_id, String currentAccount) {
        opcode = opcode.toLowerCase(Locale.ROOT);
        String type;
        switch (opcode) {
            case "like":
                type = "like";
                break;
            case "favorite":
                type = "favorite";
                break;
            case "share":
                type = "share";
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

    /*
        private static void sharePost() {
            if (currentUser == null) {
                System.out.println("You must be logged in to share a post.");
                return;
            }
            System.out.println("\n===== Share a Post =====");
            System.out.print("Enter the post ID to share: ");
            int postId = Integer.parseInt(scanner.nextLine());
            if (checkPostExist(postId)) {
                System.out.println("Invalid post ID. Please try another post ID.");
                return;
            }
            clickPost("share", postId, currentUser);
            System.out.println("Post shared successfully.");
        }

        private static void likePost() {
            if (currentUser == null) {
                System.out.println("You must be logged in to like a post.");
                return;
            }
            System.out.println("\n===== Like a Post =====");
            System.out.print("Enter the post ID to like: ");
            int postId = Integer.parseInt(scanner.nextLine());
            if (!checkPostExist(postId)) {
                System.out.println("Invalid post ID. Please try another post ID.");
                return;
            }
            clickPost("like", postId, currentUser);
            System.out.println("Post liked successfully.");
        }

        private static void favoritePost() {
            if (currentUser == null) {
                System.out.println("You must be logged in to favorite a post.");
                return;
            }
            System.out.println("\n===== Favorite a Post =====");
            System.out.print("Enter the post ID to favorite: ");
            int postId = Integer.parseInt(scanner.nextLine());
            if (checkPostExist(postId)) {
                System.out.println("Invalid post ID. Please try another post ID.");
                return;
            }
            clickPost("favorite", postId, currentUser);
            System.out.println("Post added to favorites successfully.");
        }
    */
    private static void viewPost(int post_id) {
        boolean inViewPost = true;

        while (inViewPost) {
            Post rs = w.get().uri("/viewPost/" + post_id).retrieve().bodyToMono(Post.class).block();
            String title = rs.getTitle();
            String content = rs.getContent();
            String posting_time = String.valueOf(rs.getPostingTime());
            String posting_city = rs.getPostingCity();
            String author = rs.getAuthor().getAuthor();
            System.out.println("Title:" + title + "\n" +
                    "Content:" + content + "\n" + "Posting_time:" + posting_time + "\n" + "Posting_city:" + posting_city + "\n" + "Author:" + author);

            System.out.println("You have finished the post. Show us your opinion about it ! ");
            System.out.println("\t\t=============\t\t============\t\t==============");
            System.out.println("\t\t|   like    |\t\t|   share  |\t\t|  favorite  |");
            System.out.println("\t\t=============\t\t============\t\t==============");
            System.out.println("\t\t============\t\t\t=================");
            System.out.println("\t\t|  comment |\t\t\t| view comments |");
            System.out.println("\t\t============\t\t\t=================");
            System.out.println("\t\t=============\t\t============\t\t============");
            System.out.println("\t\t|    exit   |\t\t|  return  |\t\t|  follow  |");
            System.out.println("\t\t=============\t\t============\t\t============");

            String option = scanner.nextLine();
            switch (option.toLowerCase()) {
                case "view comments":
                    viewComments(post_id);
                    break;
                case "like":
                    clickPost("like", post_id, currentUser);
                    System.out.println("Received your like ! Thx for your support");
                    break;
                case "share":
                    clickPost("share", post_id, currentUser);
                    System.out.println("You have shared this post so more people can see that");
                    break;
                case "favorite":
                    clickPost("favorite", post_id, currentUser);
                    System.out.println("This post is added to your favorite package~");
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
        if (isFollow(author)) {
            System.out.println("You have already followed " + author + "!");
        } else {
            w.get().uri("/abc/" + author + "/" + currentAccount).retrieve().bodyToMono(Integer.class).block();
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
            System.out.println("\t\t===============\t\t\t=================\t\t\t============");
            System.out.println("\t\t|    reply    |\t\t\t| view comments |\t\t\t|  return  |");
            System.out.println("\t\t===============\t\t\t=================\t\t\t============");
            String option = scanner.nextLine();
            switch (option) {
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
            //TODO: bodyToFlux() to receive collection
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

    /*
    private static void writeComment() {
        if (currentUser == null) {
            System.out.println("You must be logged in to write a comment.");
            return;
        }
        System.out.println("\n===== Write a Comment =====");
        System.out.print("Enter the post ID to comment on: ");
        int postId = Integer.parseInt(scanner.nextLine());
        if (!posts.containsKey(postId)) {
            System.out.println("Invalid post ID. Please try again.");
            return;
        }
        System.out.print("Enter your comment: ");
        String commentText = scanner.nextLine();
        Comment comment = new Comment(commentText, currentUser);
        List<Comment> postComments = comments.getOrDefault(postId, new ArrayList<>());
        postComments.add(comment);
        comments.put(postId, postComments);
        System.out.println("Comment added successfully.");
    }
    */
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
            System.out.println("  Exit forum                        press [E]");

            String option = scanner.nextLine();
            switch (option.toLowerCase()) {
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
            String title = p.getTitle();
            String content = p.getContent();
            String posting_time = String.valueOf(p.getPostingTime());
            String posting_city = p.getPostingCity();
            String author = p.getAuthor().getAuthor();
            System.out.println("Title:" + title + "\n" +
                    "Content:" + content + "\n" + "Posting_time:" + posting_time + "\n" + "Posting_city:" + posting_city + "\n" + "Author:" + author + "\n ");
        }
    }

    //TODO: How to return a list of object from a joint table
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
            String content = rs.getContent();
            String posting_time = String.valueOf(rs.getPostingTime());
            String posting_city = rs.getPostingCity();
            String author = rs.getAuthor().getAuthor();
            System.out.println("Post ID:" + postID + "\n" + "Title:" + title + "\n" +
                    "Content:" + content + "\n" + "Posting_time:" + posting_time + "\n" + "Posting_city:" + posting_city + "\n" + "Author:" + author);
        }


        boolean exit = false;
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


}

