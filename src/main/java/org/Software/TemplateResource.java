package org.Software;

import DataValidation.BaseValidator;
import DataValidation.IValidator;
import DataBase.IDatabase;
import DataBase.MongoDB;
import Model.Template;
import DataBase.MongoTemplateHandlerI;
import Service.TemplateOperation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/template")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TemplateResource {
    private IDatabase mongo = new MongoDB();
    private MongoTemplateHandlerI mongoDBHandler;
    private TemplateOperation templateOperation;
    private IValidator<Template> templateValidator;

    public TemplateResource(){
        mongo.connectToDB();
        mongoDBHandler = new MongoTemplateHandlerI(mongo);
        templateOperation = new TemplateOperation(mongoDBHandler);
        templateValidator = new BaseValidator<>();
    }

    @GET
    @Path("/get")
    public Response getTemplate(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        Map<String, String> parameters = new HashMap<>();

        for (String string : queryParameters.keySet())
            parameters.put(string, queryParameters.getFirst(string));

        return Response
                .status(Response.Status.OK)
                .entity(templateOperation.readTemplate(parameters))
                .build();
    }

    @POST
    public Response createTemplate(Template template){
        String errorMessage = templateValidator.isValid(templateValidator.validate(template));
        if(errorMessage == null)
            return Response
                    .status(Response.Status.OK)
                    .entity(templateOperation.createTemplate(template))
                    .build();

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .build();
    }

    @PUT
    @Path("/{templateID}")
    public Response updateTemplate(@PathParam("templateID") String ID, Template template){
        template.setID(ID);
        String errorMessage = templateValidator.isValid(templateValidator.validate(template));
        if(errorMessage == null)
            return Response
                    .status(Response.Status.OK)
                    .entity(templateOperation.updateTemplate(template))
                    .build();

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .build();
    }

    @DELETE
    public void deleteTemplate(@QueryParam("templateID") String ID){
        templateOperation.deleteTemplate(ID);
    }
}
