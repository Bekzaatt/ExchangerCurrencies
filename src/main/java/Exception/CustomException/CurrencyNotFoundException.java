package Exception.CustomException;

public class CurrencyNotFoundException extends RuntimeException{
    private String message;
    public CurrencyNotFoundException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}