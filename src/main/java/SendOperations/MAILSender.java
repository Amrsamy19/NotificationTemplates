package SendOperations;

import ClientService.IClientService;
import ClientService.NotificationClientService;
import ClientService.TemplateClientService;
import Model.Notification;
import Model.Template;

import java.util.*;

public class MAILSender implements ISender{
    private IClientService notificationService = new NotificationClientService();
    private IClientService templateService = new TemplateClientService();
    private List<Notification> notifications;
    private List<Template> templates;

    public MAILSender(){
        HashMap<String, String> parametersFilter = new HashMap<>();
        parametersFilter.put("status", "PENDING");
        parametersFilter.put("type", "MAIL");
        notifications = notificationService.GET(parametersFilter);
        getTemplate();
    }

    private void getTemplate(){
        HashMap<String, String> parametersFilter = new HashMap<>();
        templates = templateService.GET(parametersFilter);
    }

    public String templateParser(Notification notification, Template template) {
        HashMap<Notification, Template> parsed = new HashMap<>();
        String parsedMAIL = null;
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
            parsedMAIL = "Subject: " + entry.getValue().getType() + "\n" + parsedTemplates;
        }
        return parsedMAIL;
    }

    @Override
    public void send(){
        List<String> MAILS = new ArrayList<>();
        for(Notification notification : notifications){
            int random = (int)(Math.random() * ((2 - 1) + 1)) + 1;
            notification.setStatus(random == 1 ? "SUCCESS" : "FAILED");
            if(notification.getStatus().equals("SUCCESS")){
                for (Template template : templates){
                    if (template.getID().equals(notification.getTemplateID()))
                        MAILS.add(templateParser(notification, template));
                }
                notificationService.PUT(notification.getID(), notification);
            } else
                notificationService.PUT(notification.getID(), notification);
        }
        for (String string : MAILS){
            System.out.println(string + "\n");
        }
    }
}
