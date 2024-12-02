package Exception.CustomException;

public class ParametersNotFoundException extends RuntimeException{
    private String message;
    public ParametersNotFoundException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
