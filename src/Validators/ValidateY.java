package Validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("ValidateY")
public class ValidateY implements Validator {

    @Override
    public void validate(FacesContext facesContext,
                         UIComponent component, Object value)
            throws ValidatorException {
        String strValue = value.toString();
        Double yValue;
        try{
            yValue = Double.parseDouble(strValue);
        }
        catch (Exception e)
        {
            yValue = null;
            FacesMessage msg = new FacesMessage(" Некорректное значение"," Введеное значение Y не является числом");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        if (yValue != null){
            if (yValue<-5 || yValue > 3){
                FacesMessage msg = new FacesMessage(" Выход из ОДЗ"," Введеное значение Y не входит в ОДЗ [-5;3]");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }
}