package Client;

import ClientService.NotificationClientService;
import ClientService.TemplateClientService;
import Model.Notification;
import Model.Template;
import SendOperations.ISender;
import SendOperations.MAILSender;
import SendOperations.SMSSender;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private static Scanner input = new Scanner(System.in);
    private static TemplateClientService templateClientService  = new TemplateClientService();
    private static NotificationClientService notificationClientService = new NotificationClientService();
    private static ISender senderStrategy;

    private static void templateOperations(){
        System.out.println("[1] Create Template\n" +
                           "[2] Read Template\n" +
                           "[3] Update Template\n" +
                           "[4] Delete Template\n" +
                           "Choice |> ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice){
            case 1:{
                System.out.println("Enter the type of the template: ");
                String type = input.nextLine();

                System.out.println("Enter the content of the template");
                input.useDelimiter("\\t");
                String content = "";
                content += input.next();

                Template template = new Template(type, content);
                templateClientService.POST(template);
                break;
            }
            case 2:{
                HashMap<String, String> parameters = new HashMap<>();
                String id = "", type = "";
                System.out.println("Which would you like to search by?\n" +
                                    "[1] By ID\n" +
                                    "[2] By Type\n" +
                                    "[3] Both\n" +
                                    "[4] Read All\n" +
                                    "Choice |> ");
                int searchType = input.nextInt();
                input.nextLine();
                switch (searchType){
                    case 1:{
                        System.out.println("Enter ID: ");
                        id = input.nextLine();
                        parameters.put("templateID", id);
                        break;
                    }
                    case 2:{
                        System.out.println("Enter Type: ");
                        type = input.nextLine();
                        parameters.put("Type", type);
                        break;
                    }
                    case 3:{
                        System.out.println("Enter ID: ");
                        id = input.nextLine();

                        System.out.println("Enter Type: ");
                        type = input.nextLine();

                        parameters.put("templateID", id);
                        parameters.put("Type", type);
                        break;
                    }
                    case 4:{
                        break;
                    }
                    default:{
                        System.out.println("Invalid Input!!");
                        break;
                    }
                }
                for (Template template : templateClientService.GET(parameters))
                    System.out.println(template);
                break;
            }
            case 3:{
                System.out.println("Enter the id of the template");
                String ID = input.nextLine();

                System.out.println("Enter the type of the template: ");
                String type = input.nextLine();

                System.out.println("Enter the content of the template");
                input.useDelimiter("\\t");
                String content = "";
                content += input.next();

                Template template = new Template(type, content);
                templateClientService.PUT(ID, template);
                break;
            }
            case 4:{
                System.out.println("Enter the id of teh template");
                String ID = input.nextLine();

                templateClientService.DELETE(ID);
                break;
            }
            default:{
                System.out.println("Invalid Input!!");
                break;
            }
        }
    }

    private static void notificationOperations(){
        System.out.println("[1] Create Notification\n" +
                           "[2] Read Notification\n" +
                           "[3] Update Notification\n" +
                           "[4] Delete Notification\n" +
                           "Choice |> ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice){
            case 1:{
                System.out.println("Enter the template id of the notification: ");
                String templateID = input.nextLine();

                System.out.println("Enter the type of the notification: ");
                String type = input.nextLine();

                System.out.println("Enter the target of the notification: ");
                String target = input.nextLine();

                System.out.println("Enter the number of parameters: ");
                int numberOfParameters = input.nextInt();
                input.nextLine();
                Map<String, String> parameters = new HashMap<>();

                for(int i = 0; i < numberOfParameters; i++){
                    System.out.println("Enter the key of the parameter: ");
                    String key = input.nextLine();

                    System.out.println("Enter the value of the parameter: ");
                    String value = input.nextLine();

                    parameters.put(key, value);
                }

                Notification notification = new Notification(templateID, target, type, "PENDING", parameters);
                notificationClientService.POST(notification);
                break;
            }
            case 2:{
                HashMap<String, String> parameters = new HashMap<>();
                String id = "", type = "", status = "";
                System.out.println("Which would you like to search by?\n" +
                        "[1] By ID\n" +
                        "[2] By Type\n" +
                        "[3] By Status\n" +
                        "[4] Read All\n" +
                        "Choice |> ");
                int searchType = input.nextInt();
                input.nextLine();
                switch (searchType){
                    case 1:{
                        System.out.println("Enter ID: ");
                        id = input.nextLine();
                        parameters.put("notificationID", id);
                        break;
                    }
                    case 2:{
                        System.out.println("Enter Type: ");
                        type = input.nextLine();
                        parameters.put("type", type);
                        break;
                    }
                    case 3:{
                        System.out.println("Enter Status: ");
                        status = input.nextLine();
                        parameters.put("status", status);;
                        break;
                    }
                    case 4:{
                        break;
                    }
                    default:{
                        System.out.println("Invalid Input!!");
                        break;
                    }
                }
                for (Notification notification : notificationClientService.GET(parameters))
                    System.out.println(notification);
                break;
            }
            case 3:{
                System.out.println("Enter the id of the notification: ");
                String ID = input.nextLine();

                System.out.println("Enter the template id of the notification: ");
                String templateID = input.nextLine();

                System.out.println("Enter the type of the notification: ");
                String type = input.nextLine();

                System.out.println("Enter the target of the notification: ");
                String target = input.nextLine();

                System.out.println("Enter the status of the notification: ");
                String status = input.nextLine();

                System.out.println("Enter the number of parameters: ");
                int numberOfParameters = input.nextInt();
                input.nextLine();
                Map<String, String> parameters = new HashMap<>();

                for(int i = 0; i < numberOfParameters; i++){
                    System.out.println("Enter the key of the parameter: ");
                    String key = input.nextLine();

                    System.out.println("Enter the value of the parameter: ");
                    String value = input.nextLine();

                    parameters.put(key, value);
                }

                Notification notification = new Notification(templateID, target, type, status, parameters);
                notificationClientService.PUT(ID, notification);
                break;
            }
            case 4:{
                System.out.println("Enter the id of the notification");
                String ID = input.nextLine();

                notificationClientService.DELETE(ID);
                break;
            }
            default:{
                System.out.println("Invalid Input!!");
                break;
            }
        }
    }

    private static void dequeueNotifications(){
        System.out.println("[1] Dequeue Mail\n" +
                           "[2] Dequeue SMS\n" +
                           "Choice |> ");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice){
            case 1:{
                senderStrategy = new MAILSender();
                senderStrategy.send();
                break;
            }
            case 2:{
                senderStrategy = new SMSSender();
                senderStrategy.send();
                break;
            }
            default:{
                System.out.println("Invalid Input!!");
                break;
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("[1] Template Operations\n" +
                    "[2] Notification Operations\n" +
                    "[3] Dequeue Notification\n" +
                    "[4] Exit\n" +
                    "Choice |> ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1: {
                    templateOperations();
                    break;
                }
                case 2: {
                    notificationOperations();
                    break;
                }
                case 3: {
                    dequeueNotifications();
                    break;
                }
                case 4:{
                    return;
                }
                default: {
                    System.out.println("Invalid Input!!");
                    break;
                }
            }
        }
    }
}
