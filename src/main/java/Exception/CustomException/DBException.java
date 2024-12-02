package Exception.CustomException;

public class DBException extends RuntimeException{
    private String message;
    public DBException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
