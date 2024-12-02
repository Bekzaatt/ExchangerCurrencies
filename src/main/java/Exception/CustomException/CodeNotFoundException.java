package Exception.CustomException;

public class CodeNotFoundException extends RuntimeException{
    private String message;
    public CodeNotFoundException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
