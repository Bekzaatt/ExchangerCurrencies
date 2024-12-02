package Exception.CustomException;

public class CurrencyExistsException extends RuntimeException{
    private String message;
    public CurrencyExistsException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
