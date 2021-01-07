package SendOperations;

import Model.Notification;
import Model.Template;

import java.util.List;

public interface ISender {
    void send();
    String templateParser(Notification notifications, Template templates);
}
