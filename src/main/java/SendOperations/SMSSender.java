package SendOperations;

import ClientService.IClientService;
import ClientService.NotificationClientService;
import ClientService.TemplateClientService;
import Model.Notification;
import Model.Template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SMSSender implements ISender{
    private IClientService notificationService = new NotificationClientService();
    private IClientService templateService = new TemplateClientService();
    private List<Notification> notifications;
    private List<Template> templates;

    public SMSSender(){
        HashMap<String, String> parametersFilter = new HashMap<>();
        parametersFilter.put("status", "PENDING");
        parametersFilter.put("type", "SMS");
        notifications = notificationService.GET(parametersFilter);
        getTemplate();
    }

    private void getTemplate(){
        HashMap<String, String> parametersFilter = new HashMap<>();
        templates = templateService.GET(parametersFilter);
    }

    public String templateParser(Notification notification, Template template) {
        HashMap<Notification, Template> parsed = new HashMap<>();
        String parsedSMS = null;
        parsed.put(notification, template);

        for (Map.Entry<Notification, Template> entry : parsed.entrySet()) {
            String[] values;
            String parsedTemplates = "";
            values = entry.getValue().getContent().split("%%");
            for (int i = 0; i < values.length; i++) {
                if (i % 2 != 0)
                    values[i] = entry.getKey().getParameters().get(values[i]);
                parsedTemplates += values[i];
            }
            parsedSMS += parsedTemplates;
        }
        return parsedSMS;
    }


    public void send(){
        List<String> SMS = new ArrayList<>();
        for(Notification notification : notifications){
            int random = (int)(Math.random() * ((2 - 1) + 1)) + 1;
            notification.setStatus(random == 1 ? "SUCCESS" : "FAILED");
            if(notification.getStatus().equals("SUCCESS")){
                for (Template template : templates){
                    if (template.getID().equals(notification.getTemplateID()))
                        SMS.add(templateParser(notification, template));
                }
                notificationService.PUT(notification.getID(), notification);
            } else
                notificationService.PUT(notification.getID(), notification);
        }
        for (String string : SMS){
            System.out.println(string + "\n");
        }
    }
}
