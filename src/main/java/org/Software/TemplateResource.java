package org.Software;

import DataValidation.BaseValidator;
import DataValidation.IValidator;
import DataBase.IDatabase;
import DataBase.MongoDB;
import Model.Template;
import DataBase.MongoTemplateHandlerI;
import Service.TemplateOperation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/template")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TemplateResource {
    private IDatabase mongo = new MongoDB();
    private MongoTemplateHandlerI mongoDBHandler;
    private TemplateOperation templateOperation;

    public TemplateResource(){
        mongo.connectToDB();
        mongoDBHandler = new MongoTemplateHandlerI(mongo);
        templateOperation = new TemplateOperation(mongoDBHandler);
    }

    @GET
    @Path("/get")
    public Object getTemplate(@QueryParam("templateID") String ID) {
        Object object = templateOperation.readTemplate(ID);
        if(object == null)
            return "{\"ErrorCode\":\"404\",\"ErrorMessage\":\"The Template is not found\"}";
        return object;
    }

    @GET
    @Path("/getAll")
    public List<Object> getAllTemplates() {
        return templateOperation.readAllTemplates();
    }

    @POST
    @Path("/create")
    public Object createTemplate(Template template){
        String errorMessage;
        IValidator<Template> templateValidator = new BaseValidator<Template>();
        errorMessage = templateValidator.isValid(templateValidator.validate(template));
        if(errorMessage == null)
            return templateOperation.createTemplate(template);
        return errorMessage;
    }

    @PUT
    @Path("/{templateID}")
    public Object updateTemplate(@PathParam("templateID") String ID, Template template){
        template.setID(ID);
        String errorMessage;
        IValidator<Template> templateValidator = new BaseValidator<Template>();
        errorMessage = templateValidator.isValid(templateValidator.validate(template));
        if(errorMessage == null)
            return templateOperation.updateTemplate(template);
        return errorMessage;
    }

    @DELETE
    @Path("/delete")
    public void deleteTemplate(@QueryParam("templateID") String ID){
        templateOperation.deleteTemplate(ID);
    }
}
